package mil.nga.elevation.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mil.nga.elevation.ErrorCodes;
import mil.nga.elevation.exceptions.ApplicationException;
import mil.nga.elevation.model.GeodeticCoordinate;
import mil.nga.elevation_services.model.CoordinateType;
import mil.nga.elevation_services.model.ElevationQuery;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.TerrainDataFileType;

/**
 * This class contains a couple of static methods that were segregated out of 
 * the Spring beans classes to facilitate unit testing.
 * 
 * @author L. Craig Carpenter
 */
public class ConversionUtils {

    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(ConversionUtils.class);
    
	/**
	 * Convert an input String into a value of type <code>HeightUnitType</code>.
	 * 
	 * @param value String value to convert.
	 * @return <code>HeightUnitType</code> enumeration.
	 * @throws ApplicationException Thrown if the input String cannot be 
	 * converted to a <code>HeightUnitType</code> enumeration.
	 */
	public static HeightUnitType convertHeightUnitType(String value) 
			throws ApplicationException {
		
		HeightUnitType units = HeightUnitType.METERS;
		
		// Process the user-requested height units.
		try {
			if ((value != null) && (!value.isEmpty())) {
				units = HeightUnitType.fromValue(value.toUpperCase().trim());
			}
			else {
				LOGGER.warn("Elevation height units not specified.  Using [ "
						+ HeightUnitType.METERS.toString()
						+ " ].");
			}
		}
		catch (IllegalArgumentException iae) {
			LOGGER.error("IllegalArgumentException while parsing the input "
					+ "HeightUnitType.  User requested units of [ "
					+ value
					+ " ].  Exception message [ "
					+ iae.getMessage()
					+ " ].");
			throw new ApplicationException.ApplicationExceptionBuilder()
				.errorCode(ErrorCodes.INVALID_UNITS.getErrorCode())
				.errorMessage(ErrorCodes.INVALID_UNITS.getErrorMessage())
				.build();
		}
		return units;
	}
	
	/**
	 * Convert an input String into a value of type <code>TerrainDataFileType</code>.
	 * 
	 * @param value String value to convert.
	 * @return <code>TerrainDataFileType</code> enumeration.
	 * @throws ApplicationException Thrown if the input String cannot be 
	 * converted to a <code>TerrainDataFileType</code> enumeration.
	 */
	public static TerrainDataFileType convertTerrainDataFileType(String value) 
			throws ApplicationException {
		
		TerrainDataFileType source = TerrainDataFileType.DTED0;
		
		try {
			if ((value != null) && (!value.isEmpty())) {
				source = TerrainDataFileType.fromValue(value.toUpperCase().trim());
			}
			else {
				LOGGER.warn("Source DEM not specified.  Using [ "
						+ TerrainDataFileType.DTED0.toString()
						+ " ].");
			}
		}
		catch (IllegalArgumentException iae) {
			LOGGER.error("IllegalArgumentException while parsing the input "
					+ "TerrainDataFileType.  Exception message [ "
					+ iae.getMessage()
					+ " ].");
			throw new ApplicationException.ApplicationExceptionBuilder()
				.errorCode(ErrorCodes.INVALID_SOURCE_TYPE.getErrorCode())
				.errorMessage(ErrorCodes.INVALID_SOURCE_TYPE.getErrorMessage())
				.build();
		}
		return source;
	}
	
	/**
	 * Convert the input list of coordinates into a list of 
	 * <code>GeodeticCoordinates</code> for further processing.
	 * 
	 * @param pts Input list of points from the REST call.
	 * @return A corresponding list of <code>GeodeticCoordinates</code>
	 * @throws ApplicationException Thrown to the caller for any problems 
	 * parsing the input coordinates.
	 */
	public static List<GeodeticCoordinate> parseInputCoordinateList(List<String> pts) 
			throws ApplicationException {
		
		List<GeodeticCoordinate> coords = new ArrayList<GeodeticCoordinate>();
		
		try {
			// Ensure that there are points to process
			if ((pts != null) && (pts.size() > 0)) { 
				// Ensure the number of coords is divisible by 2 to ensure pairs.
				if (pts.size() % 2 == 0) {
					int counter = 0;
			        while (counter < pts.size()) {
			        	GeodeticCoordinate coord = 
			        			new GeodeticCoordinate
			        			.GeodeticCoordinateBuilder()
			        				.lat(pts.get(counter+1).trim())
			        				.lon(pts.get(counter).trim())
			        				.build();
			        	coords.add(coord);
			            counter += 2;
			        } 
	            }
				else {
					LOGGER.error("Invalid value for "
	                     + "the list of input points. The number of elements is [ "
	                     + pts.size()
	                     + " ].  Parameter must be a list of pairs ordered "
	                     + "by longitude, latitude.");
					throw new ApplicationException.ApplicationExceptionBuilder()
						.errorCode(ErrorCodes.INVALID_NUMBER_OF_INPUT_COORDINATES
									.getErrorCode())
						.errorMessage(
								ErrorCodes.INVALID_NUMBER_OF_INPUT_COORDINATES
									.getErrorMessage())
						.build();
				}                  
			}
			else {
				LOGGER.error("Invalid list of input points.  List is empty.");
				throw new ApplicationException.ApplicationExceptionBuilder()
					.errorCode(ErrorCodes.INVALID_INPUT_COORDINATES
							.getErrorCode())
					.errorMessage(ErrorCodes.INVALID_INPUT_COORDINATES
							.getErrorMessage())
					.build();
			}
		}
		catch (IllegalStateException ise) {
			LOGGER.error("IllegalStateException raised while parsing input "
					+ "coordinates.  Exception message => [ "
					+ ise.getMessage()
					+ " ].");
			throw new ApplicationException.ApplicationExceptionBuilder()
				.errorCode(ErrorCodes.INVALID_INPUT_COORDINATES
					.getErrorCode())
				.errorMessage(ErrorCodes.INVALID_INPUT_COORDINATES
					.getErrorMessage())
				.build();
		}
		return coords;
	}
	
	/**
	 * This method is used to generate a String-based message from the 
	 * parameters that were submitted as part of an HTTP GET call to the 
	 * <code>ElevationAt</code> end-point.  This is only used for logging
	 * purposes. 
	 *   
	 * @param pts List of user-submitted points.
	 * @param heightType The Units for the elevation height.
	 * @param source The source DEM.
	 * @return A concatenated String of the input function arguments.
	 */
	public static String toString(
			List<String> pts,  
    		String heightType,
            String source) {
		StringBuilder sb = new StringBuilder();
		sb.append("Units [ ");
		if (heightType != null) {
			sb.append(heightType.trim());
		}
		sb.append(" ], Source DEM [ ");
		if (source != null) {
			sb.append(source.trim());
		}
		sb.append(" ], Points [ ");
		if ((pts != null) && (pts.size() > 0)) { 
			int counter = 0;
			for (String pt : pts) {
				sb.append(pt);
				counter++;
				if (counter != pts.size()) {
					sb.append(", ");
				}
			}
		}
		else {
			sb.append("none");
		}
		sb.append(" ]");
		return sb.toString();
	}
	
	/**
	 * This method is used to generate a String-based message from the 
	 * parameters that were submitted as part of an HTTP POST call to the 
	 * <code>ElevationAt</code> end-point.  This is only used for logging
	 * purposes. 
	 * 
	 * @param query The user-submitted elevation query.
	 * @return A concatenated String of the input function arguments.
	 */
	public static String toString(ElevationQuery query) {
		StringBuilder sb = new StringBuilder();
		sb.append("Units [ ");
		if (query.getHeightType() != null) {
			sb.append(query.getHeightType().toString());
		}
		sb.append(" ], Source DEM [ ");
		if (query.getSource() != null) {
			sb.append(query.getSource().toString());
		}
		sb.append(" ], Points [ ");
		if ((query.getCoordinates() != null) && (query.getCoordinates().size() > 0)) {
			int counter = 0;
			for (CoordinateType pt : query.getCoordinates()) {
				sb.append("{lat=");
				sb.append(pt.getLat());
				sb.append(", lon=");
				sb.append(pt.getLon());
				sb.append("}");
				counter++;
				if (counter != query.getCoordinates().size()) {
					sb.append(", ");
				}
			}
		}
		return sb.toString();
	}
}
