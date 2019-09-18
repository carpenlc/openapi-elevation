package mil.nga.elevation.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mil.nga.elevation.Constants;
import mil.nga.elevation.ElevationExtremesFactory;
import mil.nga.elevation.ErrorCodes;
import mil.nga.elevation.dao.TerrainDataFile;
import mil.nga.elevation.exceptions.ApplicationException;
import mil.nga.elevation.model.BoundingBox;
import mil.nga.elevation.model.ElevationDataPoint;
import mil.nga.elevation.model.MinMaxElevation;
import mil.nga.elevation.utils.ConversionUtils;
import mil.nga.elevation_services.model.CoordinateTypeDouble;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.MinMaxElevationQuery;
import mil.nga.elevation_services.model.MinMaxElevationResponse;
import mil.nga.elevation_services.model.MinMaxElevationType;
import mil.nga.elevation_services.model.SecurityType;
import mil.nga.elevation_services.model.TerrainDataFileType;

/**
 * This class is essentially a translation layer between the OpenAPI/Spring
 * generated source code and the code previously developed for providing the 
 * min/max elevation data services functionality. 
 * 
 * @author L. Craig Carpenter
 */
@Component
public class ElevationExtremesService implements Constants {

    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(ElevationExtremesService.class);
    
    /**
     * AutoWired reference to the data repository.
     */
	@Autowired
	TerrainDataFileService repository;
	
	/**
	 * This method will convert the calculate <code>MinMaxElevation</code>
	 * object into the generated type <code>MinMaxElevationResponse</code> 
	 * for responding to the caller.
	 * 
	 * @param minMax The calculated minimum and maximum values.
	 * @return An object to return to the caller.
	 */
	private MinMaxElevationResponse convertToResponse(MinMaxElevation minMax) {
	
		CoordinateTypeDouble    minCoordinate = new CoordinateTypeDouble();
		CoordinateTypeDouble    maxCoordinate = new CoordinateTypeDouble();
		MinMaxElevationType     minElevation  = new MinMaxElevationType();
		MinMaxElevationType     maxElevation  = new MinMaxElevationType();
		MinMaxElevationResponse response      = new MinMaxElevationResponse();
		
		// Set the min elevation data
		minCoordinate.setLat(
				minMax.getMinElevation().getGeodeticCoordinate().getLat());
		minCoordinate.setLon(
				minMax.getMinElevation().getGeodeticCoordinate().getLon());
		minElevation.setCoordinate(minCoordinate);
		minElevation.setAbsHorizontalAccuracy(
				minMax.getMinElevation().getAccuracy().getAbsHorzAccuracy());
		minElevation.setAbsVerticalAccuracy(
				minMax.getMinElevation().getAccuracy().getAbsVertAccuracy());
		minElevation.setRelHorizontalAccuracy(
				minMax.getMinElevation().getAccuracy().getRelHorzAccuracy());
		minElevation.setRelVerticalAccuracy(
				minMax.getMinElevation().getAccuracy().getRelVertAccuracy());
		minElevation.setElevation(
				minMax.getMinElevation().getElevation());
		minElevation.setSource(
				minMax.getMinElevation().getSource());
		
		// Set the maximum elevation data
		maxCoordinate.setLat(
				minMax.getMaxElevation().getGeodeticCoordinate().getLat());
		maxCoordinate.setLon(
				minMax.getMaxElevation().getGeodeticCoordinate().getLon());
		maxElevation.setCoordinate(maxCoordinate);
		maxElevation.setAbsHorizontalAccuracy(
				minMax.getMaxElevation().getAccuracy().getAbsHorzAccuracy());
		maxElevation.setAbsVerticalAccuracy(
				minMax.getMaxElevation().getAccuracy().getAbsVertAccuracy());
		maxElevation.setRelHorizontalAccuracy(
				minMax.getMaxElevation().getAccuracy().getRelHorzAccuracy());
		maxElevation.setRelVerticalAccuracy(
				minMax.getMaxElevation().getAccuracy().getRelVertAccuracy());
		maxElevation.setElevation(
				minMax.getMaxElevation().getElevation());
		maxElevation.setSource(
				minMax.getMaxElevation().getSource());
		
		// Set the response data
		response.setMinElevation(minElevation);
		response.setMaxElevation(maxElevation);
		response.setHeightType(minMax.getMaxElevation().getUnits());
		
		// Set up the overall security information
		SecurityType security = new SecurityType();
		if ((minMax.getMaxElevation().getClassificationMarking() != null) && 
				(!minMax.getMaxElevation().getClassificationMarking().isEmpty())) { 
			security.setClassification(minMax.getMaxElevation().getClassificationMarking());
		}
		else if ((minMax.getMinElevation().getClassificationMarking() != null) && 
				(!minMax.getMinElevation().getClassificationMarking().isEmpty())) {
			security.setClassification(minMax.getMinElevation().getClassificationMarking());
		}
		else {
			security.setClassification(Constants.DEFAULT_CLASSIFICATION_MARKING);
		}
		
		// Code added to handle the producer code.  If the producer code 
		// retrieved from the source DEM is not the default, ensure the 
		// output is set to the modified value.
		if (minMax.getMaxElevation().getProducerCode().equalsIgnoreCase(DEFAULT_PRODUCER)) {
			if (minMax.getMinElevation().getProducerCode().equalsIgnoreCase(DEFAULT_PRODUCER)) {
				security.setOwnerProducer(Constants.DEFAULT_PRODUCER);
			}
			else {
				security.setOwnerProducer(minMax.getMinElevation().getProducerCode());
			}
		}
		else {
			security.setOwnerProducer(minMax.getMaxElevation().getProducerCode());
		}
		response.setSecurity(security);
		return response;
		
	}
	
	/**
	 * This method will compare the input objects and return a composite 
	 * <code>MinMaxElevation</code> object that contains the lowest min and 
	 * highest max from the two input objects.
	 *   
	 * @param previous Assumed to be the previous accumulator.
	 * @param current The current min/max elevation.
	 * @return The min/max from the input objects.
	 */
	private MinMaxElevation compare(
			MinMaxElevation previous, 
			MinMaxElevation current) {
		
		MinMaxElevation.MinMaxElevationBuilder builder = 
				new MinMaxElevation.MinMaxElevationBuilder();
		
		if (current != null) {
			if (previous == null) {
				builder.maxElevation(current.getMaxElevation().clone());
				builder.minElevation(current.getMinElevation().clone());
			}
			else {
				
				// Compare the maximum elevations.
				if (current.getMaxElevation().getElevation() > 
					previous.getMaxElevation().getElevation()) {
					builder.maxElevation(current.getMaxElevation().clone());
				}
				else {
					builder.maxElevation(previous.getMaxElevation().clone());
				}
				
				// Compare the minimum elevations
				if (current.getMinElevation().getElevation() < 
						previous.getMinElevation().getElevation()) {
					builder.minElevation(current.getMinElevation().clone());
				}
				else {
					builder.minElevation(previous.getMinElevation().clone());
				}
			}
		}
		else {
			if (previous != null) {
				builder.maxElevation(previous.getMaxElevation().clone());
				builder.minElevation(previous.getMinElevation().clone());
			}
			else {
				// Return defaults.
				builder.minElevation(
						new ElevationDataPoint.ElevationDataPointBuilder()
							.elevation(MAX_ELEVATION)
							.build());
				builder.maxElevation(
						new ElevationDataPoint.ElevationDataPointBuilder()
							.elevation(INVALID_ELEVATION_VALUE)
							.build());
			}
		}
		return builder.build();
	}
	
	/**
	 * Convenience method used to convert the user-supplied parameters
	 * from the HTTP GET call to the MinMaxElevation end point into a 
	 * format expected by the ElevationExtremesFactory class.
	 * 
	 * @param lllon lower-left longitude.
	 * @param lllat lower-left latitude.
	 * @param urlon upper-right longitude.
	 * @param urlat upper-right latitude.
	 * @param units The output units for the elevation values.
	 * @param source The source DEM type.
	 * @return Response object to be serialized and sent to the caller.
	 * @throws ApplicationException Thrown for all known issues.
	 */
	public MinMaxElevationResponse getMinMaxElevation(
			String lllon,
    		String lllat,
    		String urlon,
    		String urlat,
    		String units,
    		String source) throws ApplicationException {
		
		// If there are any issues with the input coordinates an 
		// ApplicationException will be thrown by the following call.
		BoundingBox bbox = new BoundingBox.BoundingBoxBuilder()
				.lowerLeftLat(lllat)
				.lowerLeftLon(lllon)
				.upperRightLat(urlat)
				.upperRightLon(urlon)
				.build();
		
		return getMinMaxElevation(
				bbox, 
				ConversionUtils.convertHeightUnitType(units),
				ConversionUtils.convertTerrainDataFileType(source));
	}
	
	/**
	 * Convenience method used to convert the user-supplied parameters
	 * from the HTTP POST call to the MinMaxElevation end point into a 
	 * format expected by the ElevationExtremesFactory class.
	 *  
	 * @param query Deserialized query object.
	 * @return Response object to be serialized and sent to the caller.
	 * @throws ApplicationException Thrown for all known issues.
	 */
	public MinMaxElevationResponse getMinMaxElevation(
			MinMaxElevationQuery query) throws ApplicationException {
		if ((query != null) && (query.getBbox() != null)) {  
			BoundingBox bbox = new BoundingBox.BoundingBoxBuilder()
					.lowerLeftLat(query.getBbox().getLllat())
					.lowerLeftLon(query.getBbox().getLllon())
					.upperRightLat(query.getBbox().getUrlat())
					.upperRightLon(query.getBbox().getUrlon())
					.build();
			return getMinMaxElevation(bbox, query.getHeightType(), query.getSource());
		}
		else {
			throw new ApplicationException.ApplicationExceptionBuilder()
				.errorCode(ErrorCodes.INVALID_QUERY.getErrorCode())
				.errorMessage(ErrorCodes.INVALID_QUERY.getErrorMessage())
				.build();
		}
	}
	
	/**
	 * This method calculates the cells that intersect the user-defined 
	 * bounding box, then retrieves the cell information from the backing
	 * data repository.  The <code>ElevationExtremesFactory</code> is then
	 * called to determine the minimum and maximum elevation values 
	 * within user-defined bounding box.
	 * 
	 * @param bbox User-defined bounding box.
	 * @param units The output units for the elevation values. 
	 * @param source The source DEM type.
	 * @return Response object to be serialized and sent to the caller.
	 * @throws ApplicationException
	 */
	public MinMaxElevationResponse getMinMaxElevation(
			BoundingBox         bbox,
			HeightUnitType      units,
			TerrainDataFileType source) throws ApplicationException {
		
		MinMaxElevation minMax = null;
		
		LOGGER.info("Processing bounding box [ "
				+ bbox.toString()
				+ " ].");
		
		// Determine what frames intersect the incoming bounding box
        int minCellLat = Math.round((float) Math.floor(bbox.getLowerLeftLat()));
        int maxCellLat = Math.round((float) Math.ceil(bbox.getUpperLeftLat()));

        int minCellLon = Math.round((float) Math.floor(bbox.getLowerLeftLon()));
        int maxCellLon = Math.round((float) Math.ceil(bbox.getUpperRightLon()));
		
        // Loop through the latitude/longitude cells and retrieve the 
        // information associate with each cell from the repository.
        int lat = minCellLat;
        while (lat < maxCellLat) {
        	int lon = minCellLon;
        	while (lon < maxCellLon) {
        		if (LOGGER.isDebugEnabled()) {
        			LOGGER.debug("Processing lat [ " + lat + " ], lon [ " + lon + " ].");
        		}
        		List<TerrainDataFile> files = repository.getTerrainDataFiles(lat, lon, source);
        		if ((files != null) && (files.size() > 0)) {
        			for (TerrainDataFile file : files) {
        				try {
	        				ElevationExtremesFactory factory = 
	        						new ElevationExtremesFactory.ElevationExtremesFactoryBuilder()
	        							.classificationMarking(file.getMarking())
	        							.filePath(file.getUnixPath())
	        							.units(units)
	        							.sourceType(ConversionUtils.convertTerrainDataFileType(file.getSource()))
	        							.build();
	        				MinMaxElevation tmpMinMax = factory.getMinMaxElevation(bbox);
	            			minMax = compare(minMax, tmpMinMax);
        				}
        				catch (IllegalStateException ise) {
        					// We found that it is not uncommon for the 
        					// database to be out of sync with the filesystem.
        					// Rather than propagate the exception all the 
        					// back to the REST interface, catch it here so 
        					// processing can continue. 
        					LOGGER.warn("Unexpected IllegalStateException "
        							+ "raised while processing DEM data record [ "
        							+ file.toString()
        							+ " ].  Error message => [ "
        							+ ise.getMessage()
        							+ " ].");
        				}
        			}
        		}
        		else {
        			LOGGER.warn("No source data found for Latitude [ "
        					+ lat
        					+ " ], Longitude [ "
        					+ lon 
        					+ " ] and DEM source type [ "
        					+ source.toString()
        					+ " ].");
        		}
        		lon += 1; // Increment the longitude counter
        	}
        	lat += 1; // Increment the latitude counter
        }
        
        return convertToResponse(minMax);
	}
}
