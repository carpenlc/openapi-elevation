package mil.nga.elevation.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mil.nga.elevation.ErrorCodes;
import mil.nga.elevation.Constants;
import mil.nga.elevation.ElevationDataFactory;
import mil.nga.elevation.dao.TerrainDataFile;
import mil.nga.elevation.exceptions.ApplicationException;
import mil.nga.elevation.exceptions.InvalidParameterException;
import mil.nga.elevation.model.ElevationDataPoint;
import mil.nga.elevation.model.GeodeticCoordinate;
import mil.nga.elevation.utils.ConversionUtils;
import mil.nga.elevation_services.model.CoordinateType;
import mil.nga.elevation_services.model.ElevationQuery;
import mil.nga.elevation_services.model.ElevationResponse;
import mil.nga.elevation_services.model.ElevationType;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.SecurityType;
import mil.nga.elevation_services.model.TerrainDataFileType;

/**
 * This class is essentially a translation layer between the OpenAPI/Spring
 * generated source code and the code previously developed for providing the 
 * Elevation data services functionality. 
 * 
 * @author L. Craig Carpenter
 */
@Component
public class ElevationDataService implements Constants {
	
    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(ElevationDataService.class);
    
    /**
     * AutoWired reference to the data repository.
     */
	@Autowired
	TerrainDataFileService repository;
	
	/**
	 * Convert a list of <code>ElevationDataPoint</code> objects into an 
	 * object of type <code>ElevationResponse</code> for return to the caller.
	 * 
	 * @param elevations A list of elevations.
	 * @return The object that will be serialized and sent to the caller.
	 */
	private ElevationResponse convertToResponse(
			List<ElevationDataPoint> elevations) {
		
		ElevationResponse response = new ElevationResponse();
		String marking = "";
		String producer = "";
		
		for (ElevationDataPoint point : elevations) {
			ElevationType responseElevation = new ElevationType();
			responseElevation.setElevation(point.getElevation());
			CoordinateType coord = new CoordinateType();
			
			// Determine if we need to respond with the String-based lat/lon
			// or the Double version
			if (point.getGeodeticCoordinate().getLatStr() != null) {
				coord.setLat(point.getGeodeticCoordinate().getLatStr());
			}
			else {
				coord.setLat(String.valueOf(point.getLat()));
			}
			if (point.getGeodeticCoordinate().getLonStr() != null) {
				coord.setLon(point.getGeodeticCoordinate().getLonStr());
			}
			else {
				coord.setLon(String.valueOf(point.getLon()));
			}

			responseElevation.setCoordinate(coord);
			responseElevation.setAbsHorizontalAccuracy(
					point.getAccuracy().getAbsHorzAccuracy());
			responseElevation.setAbsVerticalAccuracy(
					point.getAccuracy().getAbsVertAccuracy());
			responseElevation.setRelHorizontalAccuracy(
					point.getAccuracy().getRelHorzAccuracy());
			responseElevation.setRelVerticalAccuracy(
					point.getAccuracy().getRelVertAccuracy());
			
			responseElevation.setSource(point.getSource());
			response.addElevationsItem(responseElevation);
			
			if (!point.getProducerCode().equalsIgnoreCase(DEFAULT_PRODUCER)) {
				producer = point.getProducerCode();
			}
			else {
				if (producer.isEmpty()) {
					producer = DEFAULT_PRODUCER;
				}
			}
			
			if ((point.getClassificationMarking() != null) && 
					(!point.getClassificationMarking().isEmpty())) {
				marking = point.getClassificationMarking();
			}
			else {
				if (marking.isEmpty()) {
					marking = Constants.DEFAULT_CLASSIFICATION_MARKING;
				}
			}
		}
		
		// Set up the overall security information
		SecurityType security = new SecurityType();
		security.setClassification(marking);
		security.setOwnerProducer(producer);
		response.setSecurity(security);
		return response;
	}
    
	/**
	 * Method used to parse/convert the input String data from the OpenAPI/REST
	 * GET parameters.  Once the parameters are converted calls are made to the
	 * application code that actually performs the elevation calculation.
	 * 
	 * @param pts A list of coordinate pairs in lon, lat order.
	 * @param heightType String-based representation of the elevation units.
	 * @param source The source DEM type
	 * @return The calculated elevation.
	 * @throws ApplicationException Contains error code/message associated with
	 * any exceptions encountered throughout processing.
	 */
	public ElevationResponse getElevationAt(
			String pts,  
    		String heightType,
            String source) throws ApplicationException {
		
		// Return object
		List<ElevationDataPoint> elevations = new ArrayList<ElevationDataPoint>();
		
		// Any of the below declarations can raise an ApplicationException
		HeightUnitType           units     = 
				ConversionUtils.convertHeightUnitType(heightType);
		TerrainDataFileType      sourceDEM = 
				ConversionUtils.convertTerrainDataFileType(source);
		List<GeodeticCoordinate> coords    = 
				ConversionUtils.parseCoords(pts);
		
		if ((coords != null) && (coords.size() > 0)) { 
			LOGGER.info("Processing [ "
					+ coords.size()
					+ " ] requested elevation points.");
			try {
				// Loop through the parsed coordinate list.
				for (GeodeticCoordinate coord : coords) {
					
					List<TerrainDataFile> files = repository.getTerrainDataFiles(
							coord, 
							sourceDEM);
					
					if ((files != null) && (files.size() > 0)) {
						if (LOGGER.isDebugEnabled()) {
							System.out.println("Using source file [ "
									+ files.get(0).getUnixPath()
									+ " ].");
						}
						ElevationDataFactory factory = 
								new ElevationDataFactory.ElevationDataFactoryBuilder()
									.filePath(files.get(0).getUnixPath())
									.units(units)
									.sourceType(
											TerrainDataFileType.fromValue(
													files.get(0).getSource().trim()))
									.build();
						
						elevations.add(factory.getElevationAt(coord));
					}
					else {
						LOGGER.error("There is no coverage available for the "
								+ "requested coordinate.");
						throw new ApplicationException.ApplicationExceptionBuilder()
								.errorCode(ErrorCodes.NO_SOURCE_AVAILABLE.getErrorCode())
								.errorMessage(ErrorCodes.NO_SOURCE_AVAILABLE.getErrorMessage())
								.build();
					}
				}
			}
			// The call to getElevationAt() can raise two different 
			// exceptions.  Catch them here and re-raise as an 
			// ApplicationException.
			catch (InvalidParameterException ipe) { 
				// We should never see this exception.  Re-throw as a generic
				// application exception.
				LOGGER.error("Unexpected InvalidParameterException "
						+ "encountered while attempting to calculate "
						+ "an elevation value.  Error message => [ "
						+ ipe.getMessage()
						+ " ].  Rethrowing as an internal exception.");
				throw new ApplicationException.ApplicationExceptionBuilder()
					.errorCode(ErrorCodes.INTERNAL_EXCEPTION.getErrorCode())
					.errorMessage(ErrorCodes.INTERNAL_EXCEPTION.getErrorMessage())
					.build();
			}
			catch (IllegalStateException ise) {
				LOGGER.error("Unexpected IllegalStateException "
						+ "encountered while attempting to calculate "
						+ "an elevation value.  Error message => [ "
						+ ise.getMessage()
						+ " ].  Rethrowing as an internal exception.");
				throw new ApplicationException.ApplicationExceptionBuilder()
					.errorCode(ErrorCodes.INTERNAL_EXCEPTION.getErrorCode())
					.errorMessage(ErrorCodes.INTERNAL_EXCEPTION.getErrorMessage())
					.build();
			}
		}
		return convertToResponse(elevations);
	}
	
	/**
	 * Method used to parse/convert the input String data from the OpenAPI/REST
	 * POST body.  Once the parameters are converted calls are made to the
	 * application code that actually performs the elevation calculation.
	 * 
	 * @param query Object containing all of the elevation query parameters.
	 * @return The calculated elevation.
	 * @throws ApplicationException Contains error code/message associated with
	 * any exceptions encountered throughout processing.
	 */
	public ElevationResponse getElevationAt(ElevationQuery query) 
			throws ApplicationException {
		
		List<ElevationDataPoint> elevations = new ArrayList<ElevationDataPoint>();
		
		if (query != null) {
			if ((query.getCoordinates() != null) && 
					(query.getCoordinates().size() > 0)) {
				
				LOGGER.info("Processing [ "
						+ query.getCoordinates().size()
						+ " ] requested elevation points.");
				try {
					// Loop through the parsed coordinate list.
					for (CoordinateType coord : query.getCoordinates()) {
	
						// Check the input coordinate for consistency.
						GeodeticCoordinate geoCoord = 
								new GeodeticCoordinate.GeodeticCoordinateBuilder()
									.lat(coord.getLat())
									.lon(coord.getLon())
									.build();
						
						List<TerrainDataFile> files = repository.getTerrainDataFiles(
								geoCoord, 
								query.getSource());
						
						if ((files != null) && (files.size() > 0)) {
							System.out.println("Using file [ "
									+ files.get(0).getUnixPath()
									+ " ].");
							ElevationDataFactory factory = 
									new ElevationDataFactory.ElevationDataFactoryBuilder()
										.filePath(files.get(0).getUnixPath())
										.units(query.getHeightType())
										.sourceType(
												TerrainDataFileType.fromValue(
														files.get(0).getSource().trim()))
										.build();
							elevations.add(factory.getElevationAt(geoCoord));
						}
						else {
							LOGGER.error("There is no coverage available for the "
									+ "requested coordinate.");
							throw new ApplicationException.ApplicationExceptionBuilder()
									.errorCode(ErrorCodes.NO_SOURCE_AVAILABLE.getErrorCode())
									.errorMessage(ErrorCodes.NO_SOURCE_AVAILABLE.getErrorMessage())
									.build();
						}
					}
				}
				// The call to getElevationAt() can raise two different 
				// exceptions.  Catch them here and re-raise as an 
				// ApplicationException.
				catch (InvalidParameterException ipe) { 
					// We should never see this exception.  Re-throw as a generic
					// application exception.
					LOGGER.error("Unexpected InvalidParameterException "
							+ "encountered while attempting to calculate "
							+ "an elevation value.  Error message => [ "
							+ ipe.getMessage()
							+ " ].  Rethrowing as an internal exception.");
					throw new ApplicationException.ApplicationExceptionBuilder()
						.errorCode(ErrorCodes.INTERNAL_EXCEPTION.getErrorCode())
						.errorMessage(ErrorCodes.INTERNAL_EXCEPTION.getErrorMessage())
						.build();
				}
				catch (IllegalStateException ise) {
					LOGGER.error("Unexpected IllegalStateException "
							+ "encountered while attempting to calculate "
							+ "an elevation value.  Error message => [ "
							+ ise.getMessage()
							+ " ].  Rethrowing as an internal exception.");
					throw new ApplicationException.ApplicationExceptionBuilder()
						.errorCode(ErrorCodes.INTERNAL_EXCEPTION.getErrorCode())
						.errorMessage(ErrorCodes.INTERNAL_EXCEPTION.getErrorMessage())
						.build();
				}
			}
			else {
				LOGGER.error("The input ElevationQuery object does not "
						+ "contain any coordinates for processing.");
				throw new ApplicationException.ApplicationExceptionBuilder()
						.errorCode(ErrorCodes.INVALID_QUERY.getErrorCode())
						.errorMessage(ErrorCodes.INVALID_QUERY.getErrorMessage())
						.build();
			}
		}
		else {
			LOGGER.error("The input elevation query object is null.  The POST "
					+ "request body was empty.");
			throw new ApplicationException.ApplicationExceptionBuilder()
					.errorCode(ErrorCodes.INVALID_QUERY.getErrorCode())
					.errorMessage(ErrorCodes.INVALID_QUERY.getErrorMessage())
					.build();
			
		}
		return convertToResponse(elevations);
	}
}
