package mil.nga.elevation.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mil.nga.elevation.ErrorCodes;
import mil.nga.elevation.exceptions.ApplicationException;
import mil.nga.elevation.model.GeodeticCoordinate;
import mil.nga.elevation_services.model.CoordinateType;
import mil.nga.elevation_services.model.CoordinateTypeArray;
import mil.nga.elevation_services.model.EarthModelType;
import mil.nga.elevation_services.model.ElevationQuery;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.MinMaxElevationQuery;
import mil.nga.elevation_services.model.MinMaxElevationQueryWKT;
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
                LOGGER.warn("Elevation height units not specified.  Using [ {} ].",
                        HeightUnitType.METERS.toString());
            }
        }
        catch (IllegalArgumentException iae) {
            LOGGER.error("IllegalArgumentException while parsing the input "
                    + "HeightUnitType.  User requested units of [ {} ].  "
                    + "Exception message [ {} ].", 
                    value, 
                    iae.getMessage());
            throw new ApplicationException.ApplicationExceptionBuilder()
                .errorCode(ErrorCodes.INVALID_UNITS.getErrorCode())
                .errorMessage(ErrorCodes.INVALID_UNITS.getErrorMessage())
                .build();
        }
        return units;
    }
    
    /**
     * Convert an input String into a value of type <code>EarthModelType</code>.
     * DTED files are in reference to the EGM96 geoid.
     * 
     * @param value String value to convert.
     * @return <code>EarthModelType</code> enumeration.
     * @throws ApplicationException Thrown if the input String cannot be 
     * converted to a <code>EarthModelType</code> enumeration.
     */
    public static EarthModelType convertEarthModelType(String value) 
            throws ApplicationException {
        
        EarthModelType source = EarthModelType.EGM96;
        
        try {
            if ((value != null) && (!value.isEmpty())) {
                source = EarthModelType.fromValue(value.toUpperCase().trim());
            }
            else {
                LOGGER.warn("Reference Earth model not specified.  Using [ "
                        + EarthModelType.EGM96.toString()
                        + " ].");
            }
        }
        catch (IllegalArgumentException iae) {
            LOGGER.error("IllegalArgumentException while parsing the input "
                    + "TerrainDataFileType.  Exception message [ {} ].", 
                    iae.getMessage());
            throw new ApplicationException.ApplicationExceptionBuilder()
                .errorCode(ErrorCodes.INVALID_SOURCE_TYPE.getErrorCode())
                .errorMessage(ErrorCodes.INVALID_SOURCE_TYPE.getErrorMessage())
                .build();
        }
        return source;
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
     * parameters that were submitted as part of an HTTP POST call to the 
     * <code>CoveragesAvailable</code> end-point.  This is only used for 
     * logging purposes. 
     * 
     * @param coordinateTypeArray User-submitted array of coordinates.
     * @return A human-readable String containing the list of coordinates.
     */
    public static String toString(CoordinateTypeArray coordinateTypeArray) {
        StringBuilder sb = new StringBuilder();
        sb.append("Coordinates: [ ");
        if ((coordinateTypeArray != null) && 
                (coordinateTypeArray.getCoordinates() != null) && 
                (coordinateTypeArray.getCoordinates().size() > 0)) {
            int counter = 0;
            for (CoordinateType coord : coordinateTypeArray.getCoordinates()) {
                sb.append("{Lat:[ ");
                sb.append(coord.getLat());
                sb.append(" ], lon:[ ");
                sb.append(coord.getLon());
                sb.append(" ]}");
                counter++;
                if (counter != coordinateTypeArray.getCoordinates().size()) {
                    sb.append(", ");
                }
            }
        }
        else {
            sb.append("null");
        }
        sb.append(" ]");
        return sb.toString();
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
            String pts,  
            String heightType,
            String referenceEllipsoid,
            String source) {
        StringBuilder sb = new StringBuilder();
        sb.append("Units [ ");
        if (heightType != null) {
            sb.append(heightType.trim());
        }
        sb.append(" ], Earth Model [ ");
        if (referenceEllipsoid != null) {
            sb.append(referenceEllipsoid.trim());
        }
        sb.append(" ], Source DEM [ ");
        if (source != null) {
            sb.append(source.trim());
        }
        sb.append(" ], Points [ ");
        if (pts != null) {
            sb.append(pts);
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
     * <code>MinMaxElevation</code> end-point.  This is only used for logging
     * purposes. 
     *   
     * @param query The deserialized query object.
     * @return A concatenated String of the input function arguments.
     */
    public static String toString(MinMaxElevationQuery query) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bounding Box [ lllat:");
        sb.append(query.getBbox().getLllat());
        sb.append(", lllon:");
        sb.append(query.getBbox().getLllon());
        sb.append(", urlat:");
        sb.append(query.getBbox().getUrlat());
        sb.append(", urlon:");
        sb.append(query.getBbox().getUrlon());
        sb.append(" ], Units [ ");
        sb.append(query.getHeightType().toString());
        sb.append(" ], Source [ ");
        sb.append(query.getSource().toString());
        sb.append(" ]");
        return sb.toString();
    }
    
    /**
     * This method is used to generate a String-based message from the 
     * parameters that were submitted as part of an HTTP GET call to the 
     * <code>MinMaxElevation</code> end-point.  This is only used for logging
     * purposes. 
     * 
     * @param lllon The lower-left longitude value.
     * @param lllat The lower-left latitude value.
     * @param urlon The upper-right longitude value.
     * @param urlat The upper-right latitude value.
     * @param heightType The output elevation units.
     * @param earthModel The Earth model to use.
     * @param source The source DEM type.
     * @return A concatenated String of the input function arguments.
     */
    public static String toString(
            String lllon, 
            String lllat, 
            String urlon, 
            String urlat, 
            String heightType,
            String earthModel,
            String source) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bounding Box [ lllat:");
        sb.append(lllat);
        sb.append(", lllon:");
        sb.append(lllon);
        sb.append(", urlat:");
        sb.append(urlat);
        sb.append(", urlon:");
        sb.append(urlon);
        sb.append(" ], Units [ ");
        sb.append(heightType);
        sb.append(" ], Earth model [ ");
        sb.append(earthModel);
        sb.append(" ], Source [ ");
        sb.append(source);
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
    
    /**
     * This method is used to generate a String-based message from the 
     * parameters that were submitted as part of an HTTP POST call to the 
     * <code>MinMaxElevationQueryWKT</code> end-point.  This is only used 
     * for logging purposes. 
     * 
     * @param query The user-submitted elevation min/max query.
     * @return A concatenated String of the input function arguments.
     */
    public static String toString(MinMaxElevationQueryWKT query) {
        StringBuilder sb = new StringBuilder();
        sb.append("WKT [ ");
        if (query.getWkt() != null) {
            sb.append(query.getWkt());
        }
        sb.append(" ], Units [ ");
        if (query.getHeightType() != null) {
            sb.append(query.getHeightType().toString());
        }
        sb.append(" ], Source DEM [ ");
        if (query.getSource() != null) {
            sb.append(query.getSource().toString());
        }
        return sb.toString();
    }
    
    /**
     * Method used to split a comma-separated String into a list of 
     * <code>GeodeticCoordinate</code> objects.  The method will raise 
     * exceptions if coordinate values are out of range.
     * 
     * @param pts A comma-separated list of points.
     * @return A list of <code>GeodeticCoordinate</code> objects.
     * @throws ApplicationException If input coordinates are out of range.
     */
    public static List<GeodeticCoordinate> parseCoords(String pts) 
            throws ApplicationException {
        
        List<GeodeticCoordinate> coords = new ArrayList<GeodeticCoordinate>();
        
        if ((pts != null) && (!pts.isEmpty())) {
            String[] split = pts.split(",");
            if (split.length % 2 == 0) {
                for (int i=0; i < split.length; i+=2) {
                    try {
                        GeodeticCoordinate coord = 
                                new GeodeticCoordinate.GeodeticCoordinateBuilder()
                                    .lon(split[i])
                                    .lat(split[i+1])
                                    .build();
                        coords.add(coord);
                    }
                    catch (IllegalStateException ise) {
                        LOGGER.error("IllegalStateException encountered while "
                                + "parsing input coordinates.  "
                                + "Error message => [ "
                                + ise.getMessage()
                                + " ].");
                        throw new ApplicationException.ApplicationExceptionBuilder()
                            .errorCode(
                                    ErrorCodes.INVALID_INPUT_COORDINATES.getErrorCode())
                            .errorMessage(
                                    ErrorCodes.INVALID_INPUT_COORDINATES.getErrorMessage())
                            .build();
                    }
                }
            }
            else {
                throw new ApplicationException.ApplicationExceptionBuilder()
                        .errorCode(
                                ErrorCodes.INVALID_NUMBER_OF_INPUT_COORDINATES.getErrorCode())
                        .errorMessage(
                                ErrorCodes.INVALID_NUMBER_OF_INPUT_COORDINATES.getErrorMessage())
                        .build();
            }
        }
        return coords;
    }
}
