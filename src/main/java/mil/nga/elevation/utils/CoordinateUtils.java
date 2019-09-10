package mil.nga.elevation.utils;

import mil.nga.CoordsParse;
import mil.nga.ErrorMessageType;
import mil.nga.elevation.exceptions.ApplicationException;

/**
 * Class containing static methods that are used to manipulate coordinates
 * as needed for further processing within the elevation services applications.
 * 
 * @author L. Craig Carpenter
 */
public class CoordinateUtils {
    
	/**
	 * This method is a wrapper around the CoordsParse algorithm to perform
	 * a coordinate conversion and handle any potential errors.  If errors
	 * occur they are forwarded to the caller via exception.
	 * 
	 * @param value A candidate latitude value.
	 * @return double representing the input latitude value.
	 * @throws ApplicationException thrown if the input String cannot be 
	 * parsed into a valid latitude.
	 */
	public static double parseLat(String value) throws ApplicationException {
		CoordsParse parser = CoordsParse.getInstance();
		double parsed = parser.parseCoordString(value, true);
		if (parsed < -900.0) {
			throw new ApplicationException.ApplicationExceptionBuilder()
				.errorCode((int)parsed)
				.errorMessage(ErrorMessageType.getErrorMessage(parsed))
				.build();
		}
		return parsed;
	}
	
	/**
	 * This method is a wrapper around the CoordsParse algorithm to perform
	 * a coordinate conversion and handle any potential errors.  If errors
	 * occur they are forwarded to the caller via exception.
	 * 
	 * @param value A candidate longitude value.
	 * @return double representing the input longitude value.
	 * @throws ApplicationException thrown if the input String cannot be 
	 * parsed into a valid longitude.
	 */
	public static double parseLon(String value) throws ApplicationException {
		CoordsParse parser = CoordsParse.getInstance();
		double parsed = parser.parseCoordString(value, false);
		if (parsed < -900.0) {
			throw new ApplicationException.ApplicationExceptionBuilder()
				.errorCode((int)parsed)
				.errorMessage(ErrorMessageType.getErrorMessage(parsed))
				.build();
		}
		return parsed;
	}
	
	/**
	 * Safely truncate the input double value to get the southwest
	 * corner.
	 * @param value The input double value.
	 * @return A truncated representation of the input double value.
	 */
	public static int truncate(double value) {
		return (int)Math.floor(value);
	}
	
    /**
     * Latitude values are stored in the database as a truncated String 
     * containing a hemisphere designator.  This method converts the 
     * input double into the correct String format.
     * 
     * @param lat Target latitude value as a double.
     * @return String-based latitude data as it appears in the data store.
     */
    public static String convertLat(double lat) {
    	
    	StringBuilder  sb         = new StringBuilder();
    	String         hemisphere = (lat < 0) ? "s" : "n";
        String         latitude   = String.valueOf(Math.abs(truncate(lat)));
       
        if (Math.abs(lat) < 10) {
        	sb.append(hemisphere);
        	sb.append("0");
        	sb.append(latitude);
        }
        else {
        	sb.append(hemisphere);
        	sb.append(latitude);
        }
        return sb.toString();
    }
    
    /**
     * Longitude values are stored in the database as a truncated String 
     * containing a hemisphere designator.  This method converts the 
     * input double into the correct String format.
     * 
     * @param lon Target longitude value as a double.
     * @return String-based longitude data as it appears in the data store.
     */
    public static String convertLon(double lon) {
    	
    	StringBuilder  sb         = new StringBuilder();
    	String         hemisphere = (lon < 0) ? "w" : "e";
    	String         longitude  = String.valueOf(Math.abs(truncate(lon)));
        
    	if (Math.abs(lon) < 10) {
            sb.append(hemisphere);
         	sb.append("00");
         	sb.append(longitude);
        } else if (Math.abs(lon) < 100) {
         	sb.append(hemisphere);
         	sb.append("0");
         	sb.append(longitude);
        }
        else {
            sb.append(hemisphere);
          	sb.append(longitude); 
        }
    	return sb.toString();
    }
}
