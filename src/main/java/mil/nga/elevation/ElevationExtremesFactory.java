package mil.nga.elevation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.algorithm.locate.SimplePointInAreaLocator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Location;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbn.openmap.dataAccess.dted.DTEDFrame;

import mil.nga.elevation.egm96.GeoidHeightFactory;
import mil.nga.elevation.model.BoundingBox;
import mil.nga.elevation.model.DEMFrameAccuracy;
import mil.nga.elevation.model.ElevationDataPoint;
import mil.nga.elevation.model.GeodeticCoordinate;
import mil.nga.elevation.model.MinMaxElevation;
import mil.nga.elevation_services.model.EarthModelType;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.TerrainDataFileType;

/**
 * This class is responsible for retrieving the elevation extremes from a 
 * from a target DEM file.  It will query a DEM file, or subset of a DEM 
 * file (defined by polygon) to find the minimum and maximum elevation posts
 * contained.
 */
public class ElevationExtremesFactory implements Constants {

    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(ElevationExtremesFactory.class);
    
    private final String              filePath;
    private final String              classificationMarking;
    private final HeightUnitType      units;
    private final TerrainDataFileType sourceType;
    private final EarthModelType      earthModel;
    
    /**
     * Default constructor enforcing the builder creation pattern.
     * @param builder Object containing default values for the private final
     * internal parameters.
     */
    protected ElevationExtremesFactory(
            ElevationExtremesFactoryBuilder builder) {
        filePath              = builder.filePath;
        sourceType            = builder.sourceType;
        units                 = builder.units;
        earthModel            = builder.earthModel;
        classificationMarking = builder.classificationMarking;
    }
    
    /**
     * Get the elevation value in reference to the requested Earth model.  If 
     * the Earth model is EGM96, nothing is done.  If it is WGS84 the height 
     * is converted. 
     * 
     * @param lat Latitude associated with the height.
     * @param lon Longitude associated with the height.
     * @param value input height value.
     * @return The requested height value.
     */
    protected int getElevation(double lat, double lon, short value) {
        int result = value;
        // If the caller requested the WGS-84 ellipsoid, handle the 
        // offset here.
        if (getEarthModel() != EarthModelType.EGM96) {
            try {
                // Get the distance between the Geoid and Ellipsoid at the 
                // requested lat/lon.
                double egm96Offset = GeoidHeightFactory.getInstance().getHeight(
                        lat, 
                        lon);
                result = result + (int)egm96Offset;
            }
            catch (Exception e) {
                LOGGER.warn("Exception raised while attempting to obtain the "
                        + "Geoid height.  Exception message => [ {} ].", 
                        e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * The classification marking associated with the target DEM file.
     * @return The classification marking.
     */
    public String getClassificationMarking() {
        return classificationMarking;
    }
    
    /**
     * Utility method used to convert a bounding box into a 
     * <code>Polygon</code> Geometry object.
     * @param bbox Bounding box object.
     * @return A Polygon geometry object representing the input Bounding Box.
     */
    public Polygon getPolygon(BoundingBox bbox) {
        Polygon poly = null;
        if (bbox != null) { 
            Coordinate[] coordinates = new Coordinate[5];
            coordinates[0] = new Coordinate(bbox.getLowerLeftLon(),  
                    bbox.getLowerLeftLat());
            coordinates[1] = new Coordinate(bbox.getUpperLeftLon(),  
                    bbox.getUpperLeftLat());
            coordinates[2] = new Coordinate(bbox.getUpperRightLon(), 
                    bbox.getUpperRightLat());
            coordinates[3] = new Coordinate(bbox.getLowerRightLon(), 
                    bbox.getLowerRightLat());
            coordinates[4] = new Coordinate(bbox.getLowerLeftLon(),  
                    bbox.getLowerLeftLat());
            GeometryFactory factory = new GeometryFactory();
            poly = factory.createPolygon(coordinates);
        }
        else {
            LOGGER.warn("The input Bounding Box was null.  The output Polygon "
                    + "will also be null.");
        }
        return poly;
    }
    
    /**
     * This method will construct a <code>BoundingBox</code> object that 
     * represents the intersection of the two input <code>BoundingBox</code>
     * objects.  If there is no intersection the return value will be null.
     * 
     * @param bbox1
     * @param bbox2
     * @return The intersection of the two input bounding boxes.  Null if the
     * boxes do not intersect.
     */
    public BoundingBox getIntersection(
            BoundingBox bbox1, 
            BoundingBox bbox2) throws IllegalStateException {
        
        BoundingBox intersection = null;
        
        if ((bbox1 != null) && (bbox2 != null)) {
            if ((bbox2.getLowerLeftLon() >= bbox1.getUpperRightLon()) ||
                     (bbox2.getLowerLeftLat() >= bbox1.getUpperRightLat()) ||
                     (bbox2.getUpperRightLon() <= bbox1.getUpperLeftLon()) ||
                     (bbox2.getUpperRightLat() <= bbox1.getLowerLeftLat())) {
                LOGGER.warn("Bounding boxes do not overlap.");
            }
            else {
                intersection = new BoundingBox.BoundingBoxBuilder()
                        .lowerLeftLon(
                                Math.max(
                                        bbox1.getLowerLeftLon(), 
                                        bbox2.getLowerLeftLon()))
                        .lowerLeftLat(
                                Math.max(
                                        bbox1.getLowerLeftLat(), 
                                        bbox2.getLowerLeftLat()))
                        .upperRightLon(
                                Math.min(
                                        bbox1.getUpperRightLon(), 
                                        bbox2.getUpperRightLon()))
                        .upperRightLat(
                                Math.min(
                                        bbox1.getUpperRightLat(), 
                                        bbox2.getUpperRightLat()))
                        .build();
            }
        }
        else {
            LOGGER.error("Input bounding box is null.");
        }
        return intersection;
    }
    
    /**
     * Test method.
     * @param elevation
     * @return
     */
    public List<?> getPointsWithElevation(int elevation) {
        
        double    currentLat      = 0.0;
        double    currentLon      = 0.0;
        long      startTime       = System.currentTimeMillis();
        DTEDFrame       frame  = null;
        List<ElevationDataPoint> response = new ArrayList<ElevationDataPoint>();
        
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Loading DEM frame file [ "
                        + getFilePath()
                        + " ].");
            }
            frame = new DTEDFrame(getFilePath());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("DEM frame file [ "
                        + getFilePath()
                        + " ] loaded in [ "
                        + (System.currentTimeMillis() - startTime)
                        + " ] ms.");
            }
            
            // Get the producer code
            String producerCode = DEFAULT_PRODUCER;
            if (frame.dsi != null) {
                producerCode = frame.dsi.prod_code;
                // Some of the producer codes are padded with spaces.
                if (producerCode != null) {
                    producerCode = producerCode.trim();
                }
            }
            
            // Reset the time
            startTime = System.currentTimeMillis();
            
            // The lat/lon post spacing allows us to calculate the lat/lon 
            // position of each elevation post.
            double latPostSpacing = frame.getOMGrid().getVerticalResolution();
            double lonPostSpacing = frame.getOMGrid().getHorizontalResolution();
            
            BoundingBox frameBounds = new BoundingBox.BoundingBoxBuilder()
                    .lowerLeftLat(frame.getOMGrid().getLatitude())
                    .lowerLeftLon(frame.getOMGrid().getLongitude())
                    .upperRightLat(frame.getOMGrid().getLatitude()+1.0)
                    .upperRightLon(frame.getOMGrid().getLongitude()+1.0)
                    .build();
            
            // Retrieve the posts from the DEM frame as a 2D array of shorts
            short[][] posts = frame.getElevations(
                    (float)frameBounds.getUpperLeftLat(),
                    (float)frameBounds.getUpperLeftLon(),
                    (float)frameBounds.getLowerRightLat(),
                    (float)frameBounds.getLowerRightLon());
            
            
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Elevation posts loaded in [ "
                        + (System.currentTimeMillis() - startTime)
                        + " ] ms.");
            }
            
            // Reset the time
            startTime = System.currentTimeMillis();
                        
            // Loop through the 2D array and look for the min/max
            for (int lon_index=0; lon_index < posts.length; lon_index++) {
                
                currentLon = frameBounds.getLowerLeftLon() + 
                        (lon_index * lonPostSpacing);
                
                for (int lat_index=0; lat_index < posts[lon_index].length; lat_index++) {
                    currentLat = frameBounds.getLowerLeftLat() + 
                            (lat_index * latPostSpacing);
                    
                    if (posts[lon_index][lat_index] == elevation) {
                        ElevationDataPoint point = 
                                new ElevationDataPoint.ElevationDataPointBuilder()
                                    .elevation(posts[lon_index][lat_index])
                                    .classificationMarking(getClassificationMarking())
                                    .producerCode(producerCode)
                                    .withGeodeticCoordinate(
                                            new GeodeticCoordinate.GeodeticCoordinateBuilder()
                                                .lat(currentLat)
                                                .lon(currentLon)
                                                .build())
                                    .build();
                        response.add(point);
                        
                    }

                }
            }
            
        }
        finally {
            if (frame != null) {
                frame.close(true);
            }
        }
        
        for (ElevationDataPoint point : response) {
            System.out.println(point.toString());
        }
        return response;
    }
    
    
    /**
     * This method will determine the minimum and maximum elevation points 
     * that fall within the DEM frame identified by the input file path.
     * 
     * @return Data structure containing the miniumum and maximum elevation
     * points.
     * @throws IllegalStateException Thrown if there are any validation 
     * problems with the any of the return data.  Callers should check the
     * exception message for more information.
     */
    public MinMaxElevation getMinMaxElevation() {
        
        int       minElevation    = MAX_ELEVATION;
        int       maxElevation    = INVALID_ELEVATION_VALUE;
        int       postCounter     = 0;
        long      startTime       = System.currentTimeMillis();
        double    maxElevationLat = 0.0;
        double    maxElevationLon = 0.0;
        double    minElevationLat = 0.0;
        double    minElevationLon = 0.0;
        double    currentLat      = 0.0;
        double    currentLon      = 0.0;
        
        DTEDFrame       frame  = null;
        MinMaxElevation result = null;
        
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Loading DEM frame file [ "
                        + getFilePath()
                        + " ].");
            }
            frame = new DTEDFrame(getFilePath());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("DEM frame file [ "
                        + getFilePath()
                        + " ] loaded in [ "
                        + (System.currentTimeMillis() - startTime)
                        + " ] ms.");
            }
            
            // Reset the time
            startTime = System.currentTimeMillis();
            
            // Generate the bounding box that contains the entire target
            // frame
            BoundingBox frameBounds = new BoundingBox.BoundingBoxBuilder()
                    .lowerLeftLat(frame.getOMGrid().getLatitude())
                    .lowerLeftLon(frame.getOMGrid().getLongitude())
                    .upperRightLat(frame.getOMGrid().getLatitude()+1.0)
                    .upperRightLon(frame.getOMGrid().getLongitude()+1.0)
                    .build();
            
            // Collect the frame accuracy data
            DEMFrameAccuracy accuracy = 
                    new DEMFrameAccuracy.DEMFrameAccuracyBuilder()
                        .absHorzAccuracy(frame.acc.abs_horz_acc)
                        .absVertAccuracy(frame.acc.abs_vert_acc)
                        .relHorzAccuracy(frame.acc.rel_horz_acc)
                        .relVertAccuracy(frame.acc.rel_vert_acc)
                        .units(getUnits())
                        .build();
            
            // Get the producer code
            String producerCode = DEFAULT_PRODUCER;
            if (frame.dsi != null) {
                producerCode = frame.dsi.prod_code;
                // Some of the producer codes are padded with spaces.
                if (producerCode != null) {
                    producerCode = producerCode.trim();
                }
            }
            
            // The lat/lon post spacing allows us to calculate the lat/lon 
            // position of each elevation post.
            double latPostSpacing = frame.getOMGrid().getVerticalResolution();
            double lonPostSpacing = frame.getOMGrid().getHorizontalResolution();
            
            // Retrieve the posts from the DEM frame as a 2D array of shorts
            short[][] posts = frame.getElevations(
                    (float)frameBounds.getUpperLeftLat(),
                    (float)frameBounds.getUpperLeftLon(),
                    (float)frameBounds.getLowerRightLat(),
                    (float)frameBounds.getLowerRightLon());
            
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Elevation posts loaded in [ "
                        + (System.currentTimeMillis() - startTime)
                        + " ] ms.");
            }
            
            // Reset the time
            startTime = System.currentTimeMillis();
                        
            // Loop through the 2D array and look for the min/max
            for (int lon_index=0; lon_index < posts.length; lon_index++) {
                
                currentLon = frameBounds.getLowerLeftLon() + 
                        (lon_index * lonPostSpacing);
                
                for (int lat_index=0; lat_index < posts[lon_index].length; lat_index++) {
                    currentLat = frameBounds.getLowerLeftLat() + 
                            (lat_index * latPostSpacing);
                    
                    postCounter++;
                    
                    int elevation = getElevation(currentLat, currentLon, posts[lon_index][lat_index]);
                    
                    // Check to see if the elevation is lower than the current
                    // max elevation.
                    if ((elevation < (short)minElevation) && 
                            (elevation != INVALID_ELEVATION_VALUE)) {
                        
                        // Save the minimum elevation and associated point
                        minElevation    = elevation;
                        minElevationLat = currentLat;
                        minElevationLon = currentLon;
                        
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("New min elevation [ "
                                    + minElevation 
                                    + " ] at lat/lon [ "
                                    + minElevationLat
                                    + " , "
                                    + minElevationLon 
                                    + " ].");
                        }
                    }
                    
                    // Check to see if the elevation is higher than the current
                    // max elevation.
                    if ((elevation > (short)maxElevation) && 
                            (elevation != INVALID_ELEVATION_VALUE)) {
                        
                        // Save the max elevation and associated point
                        maxElevation    = elevation;
                        maxElevationLat = currentLat;
                        maxElevationLon = currentLon;
                        
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("New max elevation [ "
                                    + maxElevation 
                                    + " ] at lat/lon [ "
                                    + maxElevationLat
                                    + " , "
                                    + maxElevationLon 
                                    + " ].");
                        }
                    }
                }
            }
            
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Completed processing [ "
                        + postCounter
                        + " ] elevation posts in [ "
                        + (System.currentTimeMillis() - startTime)
                        + " ].");
            }
            
            // Construct the return data structure.  This could result
            // in a IllegalStateException exception.
            result = new MinMaxElevation.MinMaxElevationBuilder()
                    .maxElevation(
                            new ElevationDataPoint.ElevationDataPointBuilder()
                                .elevation(maxElevation)
                                .classificationMarking(getClassificationMarking())
                                .producerCode(producerCode)
                                .units(getUnits())
                                .earthModel(getEarthModel())
                                .withDEMFrameAccuracy(accuracy)
                                .withGeodeticCoordinate(
                                        new GeodeticCoordinate.GeodeticCoordinateBuilder()
                                            .lat(maxElevationLat)
                                            .lon(maxElevationLon)
                                        .build())
                                .build())
                    .minElevation(
                            new ElevationDataPoint.ElevationDataPointBuilder()
                            .elevation(minElevation)
                            .classificationMarking(getClassificationMarking())
                            .producerCode(producerCode)
                            .units(getUnits())
                            .earthModel(getEarthModel())
                            .withDEMFrameAccuracy(accuracy)
                            .withGeodeticCoordinate(
                                    new GeodeticCoordinate.GeodeticCoordinateBuilder()
                                        .lat(minElevationLat)
                                        .lon(minElevationLon)
                                    .build())
                            .build())
                    .build();
            
        }
        finally {
            if (frame != null) {
                frame.close(true);
            }
        }
        return result;
    }
    
    
    /**
     * This method will determine the minimum and maximum elevation points 
     * that fall within the bounding box supplied.  
     * 
     * @param bbox client-defined bounding box.
     * @return Data structure containing the miniumum and maximum elevation
     * points.
     * @throws IllegalStateException Thrown if there are any validation 
     * problems with the any of the return data.  Callers should check the
     * exception message for more information.
     */
    public MinMaxElevation getMinMaxElevation(BoundingBox bbox) {
        
        long      startTime       = System.currentTimeMillis();
        
        int              minElevation    = 32767;
        int              maxElevation    = -32767;
        int              postCounter = 0;
        DTEDFrame        frame       = null;
        DEMFrameAccuracy accuracy    = null;
        MinMaxElevation  result      = null;

        double           currentLat      = 0.0;
        double           currentLon      = 0.0;
        double           maxElevationLat = 0.0;
        double           maxElevationLon = 0.0;
        double           minElevationLat = 0.0;
        double           minElevationLon = 0.0;
        
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Loading DEM frame file [ "
                        + getFilePath()
                        + " ].");
            }
            frame = new DTEDFrame(getFilePath());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("DTED frame file [ "
                        + getFilePath()
                        + " ] loaded in [ "
                        + (System.currentTimeMillis() - startTime)
                        + " ] ms.");
            }
            
            // Reset the time
            startTime = System.currentTimeMillis();
            
            // Generate the bounding box that contains the entire target
            // frame
            BoundingBox frameBounds = new BoundingBox.BoundingBoxBuilder()
                        .lowerLeftLat(frame.getOMGrid().getLatitude())
                        .lowerLeftLon(frame.getOMGrid().getLongitude())
                        .upperRightLat(frame.getOMGrid().getLatitude()+1.0)
                        .upperRightLon(frame.getOMGrid().getLongitude()+1.0)
                    .build();
            
            // Save the producer code
            String producerCode = DEFAULT_PRODUCER;
            if (frame.dsi != null) {
                producerCode = frame.dsi.prod_code;
                // Some of the producer codes are padded with spaces.
                if (producerCode != null) {
                    producerCode = producerCode.trim();
                }
            }
            
            // Get the intersection of the DTED frame with the user-defined 
            // bounding box.
            BoundingBox intersection = getIntersection(frameBounds, bbox);
            
            if (intersection != null) {
                
                // Generate a Polygon for testing whether a point falls inside 
                // the area of interest.
                Polygon p = getPolygon(intersection);
                
                // Construct an object to test for point location.
                // SimplePointInAreaLocator locator = 
                //        new SimplePointInAreaLocator(p);
                
                // Collect the frame accuracy data
                accuracy = new DEMFrameAccuracy.DEMFrameAccuracyBuilder()
                            .absHorzAccuracy(frame.acc.abs_horz_acc)
                            .absVertAccuracy(frame.acc.abs_vert_acc)
                            .relHorzAccuracy(frame.acc.rel_horz_acc)
                            .relVertAccuracy(frame.acc.rel_vert_acc)
                            .units(getUnits())
                            .build();
                
                // The lat/lon post spacing allows us to calculate the lat/lon 
                // position of each elevation post.
                // Test
                double latPostSpacing = frame.getOMGrid().getVerticalResolution();
                double lonPostSpacing = frame.getOMGrid().getHorizontalResolution();
                
                // Retrieve the posts from the DEM frame as a 2D array of shorts
                short[][] posts = frame.getElevations(
                        (float)frameBounds.getUpperLeftLat(),
                        (float)frameBounds.getUpperLeftLon(),
                        (float)frameBounds.getLowerRightLat(),
                        (float)frameBounds.getLowerRightLon());

                // Reset the time
                startTime = System.currentTimeMillis();
                
                if ((posts.length > 0) && (posts[0].length > 0)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Loaded [ " 
                                + posts.length 
                                + " ] rows and [ "
                                + posts[0].length
                                + " ] columns of elevation posts in [ "
                                + (System.currentTimeMillis() - startTime)
                                + " ] ms.");
                    }
                    
                    // Process the posts.
                    // Loop through the 2D array.
                    for (int lon_index=0; lon_index < posts.length; lon_index++) {
                        
                        currentLon = (float)frameBounds.getLowerLeftLon() + 
                                (lon_index * lonPostSpacing);
                        
                        // Loop through the columns
                        for (int lat_index=0; lat_index < posts[lon_index].length; lat_index++) {
                            
                            currentLat = frameBounds.getLowerLeftLat() + 
                                    (lat_index * latPostSpacing);
                            postCounter++;
                            
                            // There are multiple mechanisms for testing whether 
                            // a point falls within the area of interest.  If 
                            // the point lies within the target bounding box, 
                            // check the elevation.
                            if (SimplePointInAreaLocator.containsPointInPolygon(
                                    new Coordinate(currentLon, currentLat), p)) {
                            // Using preconstructed SimplePointInAreaLocator object 
                            // if (locator.locate(
                            //        new Coordinate(currentLon, currentLat)) 
                            //            != Location.EXTERIOR) {
                                
                                int elevation = getElevation(currentLat, currentLon, posts[lon_index][lat_index]);
                                // Check to see if the elevation is lower than the current
                                // max elevation.
                                if ((elevation < (short)minElevation) && 
                                        (elevation != INVALID_ELEVATION_VALUE)) {
                                    
                                    // Save the minimum elevation and associated point
                                    minElevation    = elevation;
                                    minElevationLat = currentLat;
                                    minElevationLon = currentLon;
                                    
                                    if (LOGGER.isDebugEnabled()) {
                                        LOGGER.debug("New min elevation [ "
                                                + minElevation 
                                                + " ] at lat/lon [ "
                                                + minElevationLat
                                                + " , "
                                                + minElevationLon 
                                                + " ].");
                                    }
                                }
                                
                                // Check to see if the elevation is higher than the current
                                // max elevation.
                                if ((elevation > (short)maxElevation) && 
                                        (elevation != INVALID_ELEVATION_VALUE)) {
                                    
                                    // Save the max elevation and associated point
                                    maxElevation    = elevation;
                                    maxElevationLat = currentLat;
                                    maxElevationLon = currentLon;
                                    
                                    if (LOGGER.isDebugEnabled()) {
                                        LOGGER.debug("New max elevation [ "
                                                + maxElevation 
                                                + " ] at lat/lon [ "
                                                + maxElevationLat
                                                + " , "
                                                + maxElevationLon 
                                                + " ].");
                                    }
                                }
                            } // End point included in BBOX
                        } // End column loop
                    } // End row loop
                    
                    LOGGER.info("Processed a total of [ "
                            + postCounter
                            + " ] elevation posts in [ "
                            + (System.currentTimeMillis() - startTime)
                            + " ] ms.");
                    
                    // Construct the return data structure.  This could result
                    // in a IllegalStateException exception.
                    result = new MinMaxElevation.MinMaxElevationBuilder()
                            .maxElevation(
                                    new ElevationDataPoint.ElevationDataPointBuilder()
                                        .elevation(maxElevation)
                                        .classificationMarking(getClassificationMarking())
                                        .producerCode(producerCode)
                                        .units(getUnits())
                                        .earthModel(getEarthModel())
                                        .source(getSourceType())
                                        .withDEMFrameAccuracy(accuracy)
                                        .withGeodeticCoordinate(
                                                new GeodeticCoordinate.GeodeticCoordinateBuilder()
                                                    .lat(maxElevationLat)
                                                    .lon(maxElevationLon)
                                                .build())
                                        .build())
                            .minElevation(
                                    new ElevationDataPoint.ElevationDataPointBuilder()
                                        .elevation(minElevation)
                                        .classificationMarking(getClassificationMarking())
                                        .producerCode(producerCode)
                                        .units(getUnits())
                                        .earthModel(getEarthModel())
                                        .source(getSourceType())
                                        .withDEMFrameAccuracy(accuracy)
                                        .withGeodeticCoordinate(
                                                new GeodeticCoordinate.GeodeticCoordinateBuilder()
                                                    .lat(minElevationLat)
                                                    .lon(minElevationLon)
                                                .build())
                                    .build())
                            .build();
                } // End post check
                else {
                    LOGGER.error("Unable to retrieve posts from the target DEM cell.  "
                            + "The result object will be null.");
                }
            }
            else {
                LOGGER.warn("The input bounding box [ "
                        + bbox.toString()
                        + " ] does not overlap the DTED frame [ "
                        + frameBounds.toString()
                        + " ].  The returned MinMaxElevation object "
                        + "will be null.");
            }
        }
        finally {
            if (frame != null) {
                frame.close(true);
            }
        }
        return result;
    }
    
    /**
     * Getter method for the path to the target DEM file.
     * @return The path to the target DEM file.
     */
    public String getFilePath() {
        return filePath;
    }
    
    /**
     * Getter method for the source DEM type data.
     * return The source DEM type.
     */
    public TerrainDataFileType getSourceType() {
        return sourceType;
    }
    
    /** 
     * Getter method for the output length (height) units.
     * @return The client-requested output units.
     */
    public HeightUnitType getUnits() {
        return units;
    }
    
    
    /** 
     * Getter method for the Earth model.
     * @return The client-requested Earth model.
     */
    public EarthModelType getEarthModel() {
        return earthModel;
    }
    
    /**
     * Static inner class implementing the builder creation pattern for 
     * objects of type <code>ElevationExtremesFactory</code>.
     * 
     * @author L. Craig Carpenter
     */
    public static class ElevationExtremesFactoryBuilder {
        
        private String              filePath;
        private String              classificationMarking = "";
        private TerrainDataFileType sourceType;
        private HeightUnitType      units      = HeightUnitType.METERS;
        private EarthModelType      earthModel = EarthModelType.EGM96;
        
        /**
         * Setter method for the path to the target DEM file.
         * 
         * @param value The path to the target DEM file.
         * @return Reference to the builder object.
         * @throws IllegalStateException Thrown if the input data is 
         * invalid (null or empty).
         */
        public ElevationExtremesFactoryBuilder filePath(String value) {
            if ((value == null) || (value.isEmpty())) {
                throw new IllegalStateException(
                        "Invalid value for target file path.  Input value "
                        + "is null or empty.");
            }
            filePath = value;
            return this;
        }
        
        /**
         * Setter method for the source classification marking.
         * @param value The source classification marking.
         * @return Reference to the builder object.
         */
        public ElevationExtremesFactoryBuilder classificationMarking(String value) {
            classificationMarking = value;
            return this;
        }
        
        
        /**
         * Setter method for the reference Earth model.
         * @param value The reference Earth Model.
         * @return Reference to the builder object.
         */
        public ElevationExtremesFactoryBuilder earthModel(EarthModelType value) {
            earthModel = value;
            return this;
        }
        
        /**
         * Setter method for the source DEM type data.
         * @param value The source DEM type.
         * @return Reference to the builder object.
         */
        public ElevationExtremesFactoryBuilder sourceType(TerrainDataFileType value) {
            sourceType = value;
            return this;
        }
        
        /**
         * Setter method for the units associated with the height and accuracy 
         * data.
         * @param value The units for the height and accuracy data.
         * @return Reference to the builder object.
         */
        public ElevationExtremesFactoryBuilder units(HeightUnitType value) {
            units = value;
            return this;
        }
        
        /**
         * Build the target <code>ElevationExtremesFactory</code> object.
         * 
         * @return A constructed <code>ElevationExtremesFactory</code>.
         * @throws IllegalStateException Thrown if the constructed object 
         * fails any validation checks.
         */
        public ElevationExtremesFactory build() throws IllegalStateException {
            ElevationExtremesFactory factory = 
                    new ElevationExtremesFactory(this);
            validate(factory);
            return factory;
        }
        
        /**
         * Validate the required input date.
         * @param obj Candidate <code>ElevationExtremesFactory</code> object.
         */
        private void validate(ElevationExtremesFactory obj) {
            Path p = Paths.get(obj.getFilePath());
            if (!Files.exists(p)) {
                throw new IllegalStateException("Target DEM file => [ "
                        + obj.getFilePath()
                        + " ] does not exist.");
            }
        }
    }
    
    public static void main(String[] args) {
        ElevationExtremesFactory factory = 
                new ElevationExtremesFactory.ElevationExtremesFactoryBuilder()
                .filePath("/mnt/terrain/srtm/srtmf/srt2f_1/srtf280/dted/w069/s15.dt2")
                .units(HeightUnitType.METERS)
                .sourceType(TerrainDataFileType.SRTM2F)
                .build();
        System.out.println(factory.getMinMaxElevation().toString());
        ElevationExtremesFactory factory2 = 
                new ElevationExtremesFactory.ElevationExtremesFactoryBuilder()
                .filePath("/mnt/terrain/srtm/srtmf/srt2f_1/srtf280/dted/w069/s15.dt2")
                .units(HeightUnitType.METERS)
                .sourceType(TerrainDataFileType.SRTM2F)
                .build();
        factory2.getMinMaxElevation(new BoundingBox.BoundingBoxBuilder()
                .lowerLeftLat(-14.9)
                .lowerLeftLon(-68.9)
                .upperRightLat(-14.1)
                .upperRightLon(-68.1)
                .build());
    }
}
