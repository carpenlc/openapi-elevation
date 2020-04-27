package mil.nga.elevation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbn.openmap.dataAccess.dted.DTEDFrame;

import mil.nga.elevation.model.DEMFrameAccuracy;
import mil.nga.elevation.model.ElevationDataPoint;
import mil.nga.elevation.model.GeodeticCoordinate;
import mil.nga.elevation_services.model.EarthModelType;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.TerrainDataFileType;
import mil.nga.elevation.egm96.GeoidHeightFactory;
import mil.nga.elevation.exceptions.InvalidParameterException;

/**
 * This class is responsible for retrieving the requested elevation data 
 * from a target DEM file.  This relies on 3rd-party APIs to parse and load 
 * the target DEM files.
 * 
 * @author L. Craig Carpenter
 */
public class ElevationDataFactory implements Constants {
    
    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(ElevationDataFactory.class);
    
    private final String              filePath;
    private final String              classificationMarking;
    private final TerrainDataFileType sourceType;
    private final HeightUnitType      units;
    private final EarthModelType      earthModel;
    
    /**
     * Default constructor enforcing the builder creation pattern.
     * @param builder Object containing default values for the private final
     * internal parameters.
     */
    protected ElevationDataFactory(ElevationDataFactoryBuilder builder) {
        filePath              = builder.filePath;
        sourceType            = builder.sourceType;
        units                 = builder.units;
        earthModel            = builder.earthModel;
        classificationMarking = builder.classificationMarking;
    }
    
    /**
     * This method obtains the elevation and associated accuracy data 
     * associated with the input geodetic coordinate.  The method uses 
     * the third party com.bbn.openmap API for reading the target DEM.
     * 
     * @param coordinate The target coordinate that we wish to find the 
     * elevation value for.
     * @return A <code>ElevationDataPoint<code> data structure containing 
     * the elevation information and associated accuracy data.
     * @throws InvalidParameterException Thrown if the input coordinate 
     * is null.
     * @throws IllegalStateException Thrown if errors are encountered 
     * validating internal object fields.
     */
    public ElevationDataPoint getElevationAt(GeodeticCoordinate coordinate) 
            throws IOException, ClassNotFoundException, InvalidParameterException, IllegalStateException {
        
        long               startTime = System.currentTimeMillis();
        DTEDFrame          frame     = null;
        ElevationDataPoint result    = null;
        
        if (coordinate != null) {
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
                
                // -- test code --
                //System.out.println(coordinate.toString());
                //System.out.println("Elevation (SW Post): " + 
                //        frame.elevationAt(
                //                (float)coordinate.getLat(), 
                //                (float)coordinate.getLon()));
                //System.out.println("Elevation (Interpolated): " + 
                //        frame.interpElevationAt(
                //                (float)coordinate.getLat(), 
                //                (float)coordinate.getLon()));
                //System.out.println("Rel Horz Acc : " + frame.acc.rel_horz_acc);
                // -- test code --
                    
                // Get the producer code
                String producerCode = DEFAULT_PRODUCER;
                if (frame.dsi != null) {
                    producerCode = frame.dsi.prod_code;
                    // Some of the producer codes are padded with spaces.
                    if (producerCode != null) {
                        producerCode = producerCode.trim();
                    }
                }
                
                // When comparing results to the legacy application we found 
                // that the call to DTEDFrame.interpElevationAt() was yielding
                // unexpected results.  A review of the openmap source code 
                // shows that due to what appears to be a bug, the 
                // interpolation algorithm does not do an interpolation at all
                // but an average of two nearby points.  Code has been 
                // changed to utilize DTEDFrame.elevationAt() until we can 
                // get the openmap source code corrected.
                // -- Update 20191009 --
                // The openmap code was forked and corrected.  The code below 
                // was changed to utilize interpolation for the output 
                // elevation value.
                int elevation = frame.biLinearInterpElevationAt(
                        (float)coordinate.getLat(), 
                        (float)coordinate.getLon());
                
                // If the caller requested the WGS-84 ellipsoid, handle the 
                // offset here.
                if (getEarthModel() != EarthModelType.EGM96) {
                    // Get the distance between the Geoid and Ellipsoid at the 
                    // requested lat/lon.
                    double egm96Offset = GeoidHeightFactory.getInstance().getHeight(
                            coordinate.getLat(), 
                            coordinate.getLon());
                    elevation = elevation + (int)egm96Offset;
                }
                
                result = new ElevationDataPoint.ElevationDataPointBuilder()
                        .units(getUnits())
                        .earthModel(getEarthModel())
                        .source(getSourceType())
                        .classificationMarking(classificationMarking)
                        .producerCode(producerCode)
                        .withGeodeticCoordinate(coordinate)
                        .withDEMFrameAccuracy(
                                new DEMFrameAccuracy.DEMFrameAccuracyBuilder()
                                    .absHorzAccuracy(frame.acc.abs_horz_acc)
                                    .absVertAccuracy(frame.acc.abs_vert_acc)
                                    .relHorzAccuracy(frame.acc.rel_horz_acc)
                                    .relVertAccuracy(frame.acc.rel_vert_acc)
                                    .units(getUnits())
                                    .build())
                        .elevation(elevation)
                        .build();
            }
            finally {
                if (frame != null) {
                    frame.close(true);
                }
            }
            
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Elevation data for type [ "
                        + getSourceType().name()
                        + " ] retrieved in [ "
                        + (System.currentTimeMillis() - startTime)
                        + " ] ms.");
            }
        }
        else {
            LOGGER.error("The input geodetic coordinate is null.  Throwing a "
                    + "InvalidParameterException to the caller.");
            throw new InvalidParameterException(
                    "Input geodetic coordinate is null.");
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
     * Getter method for the Earth model type.
     * @return The reference Earth model.
     */
    public EarthModelType getEarthModel() {
        return earthModel;
    }
    
    /** 
     * Getter method for the output length (height) units.
     * @return The client-requested output units.
     */
    public HeightUnitType getUnits() {
        return units;
    }
    
    /**
     * Getter method for the source DEM type data.
     * return The source DEM type.
     */
    public TerrainDataFileType getSourceType() {
        return sourceType;
    }
    
    /**
     * Static inner class implementing the builder creation pattern for 
     * objects of type <code>ElevationDataFactory</code>.
     * 
     * @author L. Craig Carpenter
     */
    public static class ElevationDataFactoryBuilder {
        
        private String              filePath;
        private String              classificationMarking = "";
        private TerrainDataFileType sourceType;
        private HeightUnitType      units = HeightUnitType.METERS;
        private EarthModelType      earthModel = EarthModelType.EGM96;
        
        /**
         * Setter method for the path to the target DEM file.
         * 
         * @param value The path to the target DEM file.
         * @return Reference to the builder object.
         * @throws IllegalStateException Thrown if the input data is 
         * invalid (null or empty).
         */
        public ElevationDataFactoryBuilder filePath(String value) {
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
        public ElevationDataFactoryBuilder classificationMarking(String value) {
            classificationMarking = value;
            return this;
        }
        
        /**
         * Setter method for the Earth model type.
         * @param value The Earth model type.
         * @return Reference to the builder object.
         */
        public ElevationDataFactoryBuilder earthModel(EarthModelType value) {
            earthModel = value;
            return this;
        }
        
        /**
         * Setter method for the source DEM type data.
         * @param value The source DEM type.
         * @return Reference to the builder object.
         */
        public ElevationDataFactoryBuilder sourceType(TerrainDataFileType value) {
            sourceType = value;
            return this;
        }
        
        /**
         * Setter method for the units associated with the height and accuracy 
         * data.
         * @param value The units for the height and accuracy data.
         * @return Reference to the builder object.
         */
        public ElevationDataFactoryBuilder units(HeightUnitType value) {
            units = value;
            return this;
        }
        
        /**
         * Build the target <code>ElevationDataFactory</code> object.
         * 
         * @return A constructed <code>ElevationDataFactory</code>.
         * @throws IllegalStateException Thrown if the constructed object 
         * fails any validation checks.
         */
        public ElevationDataFactory build() throws IllegalStateException {
            ElevationDataFactory factory = new ElevationDataFactory(this);
            validate(factory);
            return factory;
        }
        
        /**
         * Validate the required input date.
         * @param coord Candidate <code>ElevationDataFactory</code> object.
         */
        private void validate(ElevationDataFactory obj) {
            Path p = Paths.get(obj.getFilePath());
            if (!Files.exists(p)) {
                throw new IllegalStateException("Target DEM file => [ "
                        + obj.getFilePath()
                        + " ] does not exist.");
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            ElevationDataFactory egm96Factory = new ElevationDataFactory.ElevationDataFactoryBuilder()
                    .filePath("/opt/input/elevation/dted1/dted1p1/dted/w069/n66.dt1")
                    .units(HeightUnitType.METERS)
                    .earthModel(EarthModelType.EGM96)
                    .sourceType(TerrainDataFileType.DTED1)
                    .build();
            GeodeticCoordinate coord = new GeodeticCoordinate.GeodeticCoordinateBuilder()
                    .lat(66.555)
                    .lon(-68.555)
                    .build();
            
            ElevationDataFactory wgs84Factory = new ElevationDataFactory.ElevationDataFactoryBuilder()
                    .filePath("/opt/input/elevation/dted1/dted1p1/dted/w069/n66.dt1")
                    .units(HeightUnitType.METERS)
                    .earthModel(EarthModelType.WGS84)
                    .sourceType(TerrainDataFileType.DTED1)
                    .build();
            GeodeticCoordinate coord2 = new GeodeticCoordinate.GeodeticCoordinateBuilder()
                    .lat(66.555)
                    .lon(-68.555)
                    .build();
            ElevationDataPoint point2 = wgs84Factory.getElevationAt(coord);
            System.out.println(point2.toString());
            
            ElevationDataPoint point = egm96Factory.getElevationAt(coord);
            System.out.println(point.toString());
            coord = new GeodeticCoordinate.GeodeticCoordinateBuilder()
                    .lat(66.0)
                    .lon(-69.0)
                    .build();
            point = egm96Factory.getElevationAt(coord);
            System.out.println(point.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}