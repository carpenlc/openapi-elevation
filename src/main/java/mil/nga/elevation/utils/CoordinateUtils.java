package mil.nga.elevation.utils;

/**
 * Class containing static methods that are used to determine what frame file
 * will contain an individual point.  It determines the southwest corner of 
 * the 1 degree cell which will contain the point. 
 * 
 * @author L. Craig Carpenter
 */
public class CoordinateUtils {

	
	
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
