package mil.nga.elevation.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mil.nga.elevation.ErrorCodes;
import mil.nga.elevation.dao.TerrainDataFileDAO;
import mil.nga.elevation.exceptions.ApplicationException;
import mil.nga.elevation.utils.CoordinateUtils;
import mil.nga.elevation_services.model.CoordinateType;
import mil.nga.elevation_services.model.CoordinateTypeArray;
import mil.nga.elevation_services.model.CoverageAvailableType;
import mil.nga.elevation_services.model.CoveragesAvailableType;

/**
 * This class is essentially a translation layer between the OpenAPI/Spring
 * generated source code and the code previously developed for providing the 
 * Elevation data services functionality.  This class is associated with the 
 * coverage services API.
 * 
 * @author L. Craig Carpenter
 */
@Component
public class CoverageService {

    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(CoverageService.class);
    
	/**
	 * Injected reference to the persistence layer.
	 */
	@Autowired
	TerrainDataFileDAO repository;
	
	/**
	 * Private method added to apply a trim to the source data
	 * retrieved from the database.  We found that the 
	 * <code>TYP</code> field in the database is padded with extra
	 * spaces which are forwarded to the caller.  This method strips off
	 * the extra spaces.
	 * @param data Raw array of Strings retrieved from the data source.
	 * @return The input array stripped of extraneous spaces.
	 */
	private List<String> trim(List<String> data) {
		List<String> response = null;
		if (data != null) {
			if (data.size() == 0) {
				response = new ArrayList<String>();
			}
			else {
				response = new ArrayList<String>();
				for (String value: data) {
					response.add(value.trim());
				}
			}
		}
		return response;
	}
	
	/**
	 * Retrieve the coverages available for a specific latitude/longitude 
	 * point.  This method is called when the user sends a GET request to the
	 * <code>CoverageAvailable</code> endpoint.
	 * 
	 * @param lat Latitude portion of a coordinate.
	 * @param lon Longitude portion of a coordinate.
	 * @return The types of coverages available for the given point.
	 * @throws ApplicationException Thrown if there are any errors retrieving 
	 * the available coverages.
	 */
	public CoverageAvailableType getCoveragesAvailable(
			String lat, 
			String lon) throws ApplicationException {
		
		CoverageAvailableType response = new CoverageAvailableType();

		if ((lat != null) && (!lat.isEmpty())) {
			if ((lon != null) && (!lon.isEmpty())) {
				CoordinateType coord = new CoordinateType();
				coord.setLat(lat);
				coord.setLon(lon);
				response.setCoordinate(coord);
				List<String> data = repository.findCoverageByLatAndLon(
						CoordinateUtils.convertLat(
								CoordinateUtils.parseLat(lat)), 
						CoordinateUtils.convertLon(
								CoordinateUtils.parseLon(lon)));
				data = trim(data);
				if ((data != null) && (data.size() > 0)) {
					response.setCoverages(data);
				}
				else {
					LOGGER.warn("No coverages available for Lat [ "
							+ lat
							+ " ], lon [ "
							+ lon
							+ " ].");
				}
			}
			else {
				LOGGER.error("Caller failed to supply a valid longitude.");
				throw new ApplicationException.ApplicationExceptionBuilder()
					.errorCode(ErrorCodes.INVALID_QUERY_NO_LON.getErrorCode())
					.errorMessage(ErrorCodes.INVALID_QUERY_NO_LON.getErrorMessage())
					.build();
			}
		}
		else {
			LOGGER.error("Caller failed to supply a valid latitude.");
			throw new ApplicationException.ApplicationExceptionBuilder()
				.errorCode(ErrorCodes.INVALID_QUERY_NO_LAT.getErrorCode())
				.errorMessage(ErrorCodes.INVALID_QUERY_NO_LAT.getErrorMessage())
				.build();
		}
		return response;
	}
	
	/**
	 * Retrieve the coverages available for an array of latitude/longitude 
	 * points.  This method is called when the user sends a POST request to the
	 * <code>CoverageAvailable</code> endpoint.
	 * 
	 * @param coordinateTypeArray Data structure containing an array of geographic 
	 * points.
	 * @return A list of coverages available for each geographic point.
	 * @throws ApplicationException Thrown if there are any errors retrieving 
	 * the available coverages.
	 */
	public CoveragesAvailableType getCoveragesAvailable(
			CoordinateTypeArray coordinateTypeArray) throws ApplicationException {

		CoveragesAvailableType response = new CoveragesAvailableType();
		
		if ((coordinateTypeArray != null) && 
				(coordinateTypeArray.getCoordinates() != null) && 
				(coordinateTypeArray.getCoordinates().size() > 0)) {
			for (CoordinateType coord : coordinateTypeArray.getCoordinates()) { 
				CoverageAvailableType coverages = getCoveragesAvailable(
						coord.getLat(),
						coord.getLon());
				response.addCoveragesAvailableItem(coverages);
			}
		}
		else {
			LOGGER.error("Invalid query.  Client did not provide an input "
					+ "array of coordinates.");
			throw new ApplicationException.ApplicationExceptionBuilder()
				.errorCode(ErrorCodes.INVALID_QUERY_NO_COORDINATES.getErrorCode())
				.errorMessage(ErrorCodes.INVALID_QUERY_NO_COORDINATES.getErrorMessage())
				.build();
		}
		return response;
	}
}
