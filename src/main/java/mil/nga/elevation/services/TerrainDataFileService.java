package mil.nga.elevation.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mil.nga.elevation.ErrorCodes;
import mil.nga.elevation.dao.TerrainDataFile;
import mil.nga.elevation.dao.TerrainDataFileDAO;
import mil.nga.elevation.exceptions.ApplicationException;
import mil.nga.elevation.model.GeodeticCoordinate;
import mil.nga.elevation.utils.CoordinateUtils;
import mil.nga.elevation_services.model.TerrainDataFileType;

/**
 * Spring service class encapsulating the business logic required to
 * translate incoming client-supplied queries into the format expected
 * by the persistence layer.  This service provides information on the 
 * on disk DEMs that will be used to calculate elevation-related data.
 * 
 * @author L. Craig Carpenter
 */
@Component
public class TerrainDataFileService {
	
    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(TerrainDataFileService.class);
    
	/**
	 * Injected reference to the persistence layer.
	 */
	@Autowired
	TerrainDataFileDAO repository;

    /**
     * Convenience method used to obtain a list of DEMs that will provide a 
     * list of terrain data files that will provide the coverage for the 
     * input latitude/longitude coordinate pair.
     * 
     * @param lat The requested latitude value.
     * @param lon The requested longitude value.
     * @param source The source DEM type requested.
     * @return The list of terrain data files.
     * @throws ApplicationException Capture error code/error message to provide 
     * feedback to the user in the event of known error conditions.
     */
	public List<TerrainDataFile> getTerrainDataFiles(
			GeodeticCoordinate  coord,
			TerrainDataFileType source) throws ApplicationException {
		return getTerrainDataFiles(coord.getLat(), coord.getLon(), source);
	}
    
	/**
	 * Method used to retrieve a list of source DEM types available for a
	 * caller supplied latitude and longitude.
	 * 
     * @param lat The requested latitude value.
     * @param lon The requested longitude value.
     * @return The list of source DEM types available.
     * @throws ApplicationException Capture error code/error message to provide 
     * feedback to the user in the event of known error conditions.
	 */
	public List<String> getCoverageAvailable(
			double lat, 
			double lon) throws ApplicationException {
		
		long         startTime = System.currentTimeMillis();
		List<String> data      = null;
		
		if (repository != null) {
			data = repository.findCoverageByLatAndLon(
					CoordinateUtils.convertLat(lat), 
					CoordinateUtils.convertLon(lon));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Coverage query for lat [ "
						+ CoordinateUtils.convertLat(lat)
						+ " ] and lon [ "
						+ CoordinateUtils.convertLon(lon)
						+ " ] completed in [ "
						+ (System.currentTimeMillis() - startTime)
						+ " ].");
			}
		}
		else {
			LOGGER.error("The TerrainDataFile repository is not available.  "
					+ "It was not AutoWired into the [ "
					+ this.getClass().getCanonicalName()
					+ " ] class.");
			throw (new ApplicationException.ApplicationExceptionBuilder()
					.errorCode(ErrorCodes.DB_CONNECTION_ERROR.getErrorCode())
					.errorMessage(ErrorCodes.DB_CONNECTION_ERROR.getErrorMessage())
					.build());
		}
		return data;
	}
	
	/**
	 * Convenience method allowing clients to call using an int value
	 * for the latitude/longitude data.
	 * 
	 * @param lat The requested latitude value (int)
	 * @param lon The requested longitude value (int)
	 * @param source The source DEM type requested.
	 * @return The list of terrain data files.
	 * @throws ApplicationException Capture error code/error message to provide 
     * feedback to the user in the event of known error conditions.
	 */
	public List<TerrainDataFile> getTerrainDataFiles(
			int lat, 
			int lon, 
			TerrainDataFileType source) throws ApplicationException {
		return getTerrainDataFiles((double)lat, (double)lon, source);
	}
	
    /**
     * This method obtains a list of DEMs that will provide a list of terrain
     * data files that will provide the coverage for the input 
     * latitude/longitude coordinate pair.
     * 
     * @param lat The requested latitude value.
     * @param lon The requested longitude value.
     * @param source The source DEM type requested.
     * @return The list of terrain data files.
     * @throws ApplicationException Capture error code/error message to provide 
     * feedback to the user in the event of known error conditions.
     */
	public List<TerrainDataFile> getTerrainDataFiles(
			double lat, 
			double lon, 
			TerrainDataFileType source) throws ApplicationException {
		
		long                  startTime = System.currentTimeMillis();
		List<TerrainDataFile> data      = null;
		
		if (repository != null) {
			if (source == TerrainDataFileType.BEST) {
				data = repository.findByLatAndLon(
						CoordinateUtils.convertLat(lat), 
						CoordinateUtils.convertLon(lon));
			}
			else {
				data = repository.findByLatAndLonAndSource(
						CoordinateUtils.convertLat(lat), 
						CoordinateUtils.convertLon(lon), 
						source.toString().trim());
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Query for lat [ "
						+ CoordinateUtils.convertLat(lat)
						+ " ] and lon [ "
						+ CoordinateUtils.convertLon(lon)
						+ " ] completed in [ "
						+ (System.currentTimeMillis() - startTime)
						+ " ].");
			}
		}
		else {
			LOGGER.error("The TerrainDataFile repository is not available.  "
					+ "It was not AutoWired into the [ "
					+ this.getClass().getCanonicalName()
					+ " ] class.");
			throw (new ApplicationException.ApplicationExceptionBuilder()
					.errorCode(ErrorCodes.DB_CONNECTION_ERROR.getErrorCode())
					.errorMessage(ErrorCodes.DB_CONNECTION_ERROR.getErrorMessage())
					.build());
		}
		return data;
	}
}
