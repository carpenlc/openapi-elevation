package mil.nga.elevation.model;

import java.io.Serializable;

import mil.nga.CoordsParse;
import mil.nga.ErrorMessageType;
import mil.nga.elevation.exceptions.ApplicationException;

/**
 * Simple data structure holding a single geodetic coordinate (lat/lon).  
 * This class also has simple methods for converting to/from the WSDL 
 * generated <code>GeodeticCoordinateBean</code>.
 * 
 * @author L. Craig Carpenter
 */
public class GeodeticCoordinate implements Serializable, Cloneable {

    /**
     * Eclipse-generated serialVersionUID
     */
    private static final long serialVersionUID = -7545583693002322759L;
    
    private final double lat;
    private final double lon;
    private final String latStr;
    private final String lonStr;
    
    /**
     * Default constructor enforcing the builder creation pattern.
     * @param builder Object containing default values for lat/lon.
     */
    protected GeodeticCoordinate(GeodeticCoordinateBuilder builder) {
        lat    = builder.lat;
        lon    = builder.lon;
        latStr = builder.latStr;
        lonStr = builder.lonStr;
    }
    
    /**
     * Getter method for the latitude value.
     * @return The latitude value of the geodetic coordinate.
     */
    public double getLat() {
        return lat;
    }

    /**
     * Getter method for the String representation of the latitude value.
     * @return The latitude value of the geodetic coordinate.
     */
    public String getLatStr() {
        return latStr;
    }
    
    /**
     * Getter method for the longitude value.
     * @return The longitude value of the geodetic coordinate.
     */
    public double getLon() {
        return lon;
    }
    
    /**
     * Getter method for the String representation of the longitude value.
     * @return The longitude value of the geodetic coordinate.
     */
    public String getLonStr() {
        return lonStr;
    }
    
    /**
     * Perform a deep clone of the object.
     */
    public GeodeticCoordinate clone() {
        return new GeodeticCoordinate.GeodeticCoordinateBuilder()
                .lat(getLat())
                .lon(getLon())
                .build();
    }
    
    /**
     * Convert to a human readable String.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Latitude => [ ");
        sb.append(lat);
        sb.append(" ], Longitude => [ ");
        sb.append(lon);
        sb.append(" ]");
        if ((latStr != null) && (!latStr.isEmpty())) { 
            sb.append(", Latitude (String) => [ ");
            sb.append(latStr);
            sb.append(" ]");
        }
        if ((lonStr != null) && (!lonStr.isEmpty())) { 
            sb.append(", Longitude (String) => [ ");
            sb.append(lonStr);
            sb.append(" ]");
        }
        return sb.toString();
    }
    
    /**
     * Static inner class implementing the builder creation pattern for 
     * objects of type <code>GeodeticCoordinate</code>.
     * 
     * @author L. Craig Carpenter
     */
    public static class GeodeticCoordinateBuilder {
        
        private double lat    = 0.0;
        private double lon    = 0.0;
        private String latStr = null;
        private String lonStr = null;
        
        /**
         * Setter method for the latitude value.
         * @param lat Double value for the latitude value.
         * @return The builder object.
         */
        public GeodeticCoordinateBuilder lat(double lat) {
            this.lat = lat;
            return this;
        }
        
        /**
         * Setter method for the latitude value.
         * @param lat String representing the latitude value.
         * @return The builder object.
         * @throws IllegalStateException Thrown if the input latitude 
         * String data is null or empty.
         */
        public GeodeticCoordinateBuilder lat(String lat) 
                throws IllegalStateException {
            CoordsParse parser = CoordsParse.getInstance();
            if ((lat != null) && (!lat.isEmpty())) { 
                this.latStr = lat.trim();
                double parsed = parser.parseCoordString(this.latStr, true);
                if (parsed < -900.0) {
                    throw new IllegalStateException("Unable to parse "
                            + "latitude coordinate.  Input [ "
                            + lat
                            + " ].  Error code [ "
                            + (int)parsed
                            + " ], error message [ "
                            + ErrorMessageType.getErrorMessage(parsed)
                            + " ].");
                }
                else {
                    this.lat = parsed;
                }
            }
            else {
                throw new IllegalStateException(
                        "Input latitude String value is null or empty.");                
            }
            return this;
        }
        
        /**
         * Setter method for the latitude value.
         * @param lat Double value for the latitude value.
         * @return The builder object.
         */
        public GeodeticCoordinateBuilder lon(double lon) {
            this.lon = lon;
            return this;
        }
        
        /**
         * Setter method for the longitude value.  If the user specifies the 
         * coordinate in a String-based format save the string version as well.
         * 
         * @param lat String representing the longitude value.
         * @return The builder object.
         * @throws IllegalStateException Thrown if the input longitude 
         * String data is null or empty.
         */
        public GeodeticCoordinateBuilder lon(String lon) 
                throws IllegalStateException {
            
            CoordsParse parser = CoordsParse.getInstance();
            
            if ((lon != null) && (!lon.isEmpty())) {
                this.lonStr = lon.trim();
                double parsed = parser.parseCoordString(this.lonStr, false);
                if (parsed < -900.0) {
                    throw new IllegalStateException("Unable to parse "
                            + "longitude coordinate.  Input [ "
                            + lat
                            + " ].  Error code [ "
                            + (int)parsed
                            + " ], error message [ "
                            + ErrorMessageType.getErrorMessage(parsed)
                            + " ].");
                }
                else {
                    this.lon = parsed;
                }
            }
            else {
                throw new IllegalStateException(
                        "Input longitude String value is null or empty.");
            }
            return this;
        }
        
        /**
         * Build the target <code>GeodeticCoordinate</code> object.
         * 
         * @return A constructed <code>GeodeticCoordinate</code>.
         * @throws IllegalStateException Thrown if the constructed object 
         * fails any validation checks.
         */
        public GeodeticCoordinate build() throws IllegalStateException {
            GeodeticCoordinate coord = new GeodeticCoordinate(this);
            validate(coord);
            return coord;
        }
        
        /**
         * Validate the coordinate.
         * @param coord Candidate <code>GeodeticCoordinate</code> object.
         */
        private void validate(GeodeticCoordinate coord) {
            // First check to see if the coordinates failed conversion
            if (coord.getLat() < -1000) {
                throw new IllegalStateException(
                        "Invalid latitude value => [ " 
                        + coord.getLat()
                        + " ].  Conversion error message => [ " 
                        + ErrorMessageType.getErrorMessage(coord.getLat())
                        + " ].");
            }
            if (coord.getLon() < -1000) {
                throw new IllegalStateException(
                        "Invalid longitude value => [ " 
                        + coord.getLon()
                        + " ].  Conversion error message => [ " 
                        + ErrorMessageType.getErrorMessage(coord.getLon())
                        + " ].");
            }
            // Next, make sure the coordinates are within valid ranges.
            if (coord.getLon() > 180) {
                throw new IllegalStateException(
                        "Invalid longitude value => [ " 
                        + coord.getLon()
                        + " ].  Longitude must be less than 180."); 
            }
            if (coord.getLon() < -180) {
                throw new IllegalStateException(
                        "Invalid longitude value => [ " 
                        + coord.getLon()
                        + " ].  Longitude must be greater than -180."); 
            }
            if (coord.getLat() > 90) {
                throw new IllegalStateException(
                        "Invalid latitude value => [ " 
                        + coord.getLat()
                        + " ].  Latitude must be less than 90."); 
            }
            if (coord.getLat() < -90) {
                throw new IllegalStateException(
                        "Invalid latitude value => [ " 
                        + coord.getLat()
                        + " ].  Latitude must be greater than -90."); 
            }
        }
    }
}