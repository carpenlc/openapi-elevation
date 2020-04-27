package mil.nga.elevation.model;

import java.io.Serializable;

import mil.nga.elevation.Constants;
import mil.nga.elevation_services.model.EarthModelType;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.TerrainDataFileType;

public class ElevationDataPoint implements Serializable, Cloneable, Constants {

    /**
     * Eclipse-generated serialVersionUID
     */
    private static final long serialVersionUID = -2774969956804341499L;
    
    private final int                 elevation;
    private final String              classificationMarking;
    private final String              producerCode;
    private final GeodeticCoordinate  coordinate;
    private final DEMFrameAccuracy    accuracy;
    private final HeightUnitType      units;
    private final TerrainDataFileType source;
    private final EarthModelType      earthModel;
    
    /**
     * Default constructor enforcing the builder creation pattern.
     * @param builder Object containing default values for the private final
     * internal parameters.
     */
    protected ElevationDataPoint(ElevationDataPointBuilder builder) {
        classificationMarking = builder.classificationMarking;
        producerCode          = builder.producerCode;
        elevation             = builder.elevation;
        coordinate            = builder.coordinate;
        accuracy              = builder.accuracy;
        units                 = builder.units;
        earthModel            = builder.earthModel;
        source                = builder.source;
    }
    
    /**
     * Getter method for the object containing the accuracy data 
     * the source DEM.
     * @return The classification marking.
     */
    public DEMFrameAccuracy getAccuracy() {
        return accuracy;
    }
    
    /**
     * Getter method for the classification marking that was associated with
     * the source DEM.
     * @return The classification marking.
     */
    public String getClassificationMarking() {
        return classificationMarking;
    }
    
    /**
     * Getter method for the producer code that was associated with
     * the source DEM.
     * @return The producer code.
     */
    public String getProducerCode() {
        return producerCode;
    }
    
    /**
     * Getter method for the elevation value associated with a given lat/lon
     * pair.
     * @return The elevation value.
     */
    public int getElevation() {
        return elevation;
    }
    
    /**
     * Getter method for the Geodetic coordinate.
     * @return The Geodetic coordinate object.
     */
    public GeodeticCoordinate getGeodeticCoordinate() {
        return this.coordinate;
    }
    
    /**
     * Getter method for the latitude value.
     * @return The latitude value.
     */
    public double getLat() {
        return coordinate.getLat();
    }
    
    /** 
     * Getter method for the longitude value.
     * @return The longitude value.
     */
    public double getLon() {
        return coordinate.getLon();
    }
    
    /** 
     * Getter method for the source data type.
     * @return The source data type.
     */
    public TerrainDataFileType getSource() {
        return source;
    }
    
    /**
     * Getter method for the length units associated with the elevation  
     * value associated with a given lat/lon pair.
     * @return The elevation value.
     */
    public HeightUnitType getUnits() {
        return units;
    }
    
    /**
     * Getter method for the Earth model the height is associated with.
     * @return The Earth model.
     */
    public EarthModelType getEarthModel() {
        return earthModel;
    }
    
    /**
     * Perform a deep clone of the object.
     */
    public ElevationDataPoint clone() {
        return new ElevationDataPoint.ElevationDataPointBuilder()
                .classificationMarking(getClassificationMarking())
                .producerCode(getProducerCode())
                .elevation(getElevation())
                .source(getSource())
                .withGeodeticCoordinate(getGeodeticCoordinate().clone())
                .withDEMFrameAccuracy(getAccuracy().clone())
                .units(getUnits())
                .earthModel(getEarthModel())
                .build();
    }
    
    /**
     * Convert to a printable String.
     */
     public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ElevationDataPoint : Lat => [ ");
        sb.append(getLat());
        sb.append(" ], Lon => [ ");
        sb.append(getLon());
        sb.append(" ], elevation => [ ");
        sb.append(getElevation()); 
        sb.append(" ], source => [ ");
        sb.append(getSource());
        sb.append(" ], elevation units => [ ");
        sb.append(getUnits().name());
        sb.append(" ], earth model => [ ");
        sb.append(getEarthModel().name());
        sb.append(" ], classification marking => [ ");
        sb.append(getClassificationMarking());
        sb.append(" ], producer code => [ ");
        sb.append(getProducerCode());
        sb.append(" ].");
        sb.append(accuracy.toString());
        return sb.toString();
    }
     
    /**
     * Static inner class implementing the builder creation pattern for 
     * objects of type <code>ElevationDataPoint</code>.
     * 
     * @author L. Craig Carpenter
     */
    public static class ElevationDataPointBuilder {
        
        private int                 elevation;
        private String              classificationMarking = "";
        private String              producerCode = DEFAULT_PRODUCER;
        private GeodeticCoordinate  coordinate;
        private DEMFrameAccuracy    accuracy;
        private HeightUnitType      units        = HeightUnitType.METERS;
        private TerrainDataFileType source       = TerrainDataFileType.DTED0;
        private EarthModelType      earthModel   = EarthModelType.EGM96;
        /**
         * Setter method for the source classification marking.
         * @param value The source classification marking.
         * @return Reference to the builder object.
         */
        public ElevationDataPointBuilder classificationMarking(String value) {
            classificationMarking = value;
            return this;
        }
        
        /**
         * Setter method for the source classification marking.
         * @param value The source classification marking.
         * @return Reference to the builder object.
         */
        public ElevationDataPointBuilder producerCode(String value) {
            if ((value != null) && (!value.isEmpty())) {
                producerCode = value;
            }
            return this;
        }
        
        /**
         * Setter method for the Earth model type.
         * @param value The Earth model type.
         * @return The builder object.
         */
        public ElevationDataPointBuilder earthModel (EarthModelType value) {
            if (value != null) {
                earthModel = value;
            }
            return this;
        }
        
        /**
         * Setter method for the units associated with any length 
         * data.
         * @param value The unit information.
         * @return The builder object.
         */
        public ElevationDataPointBuilder units(
                HeightUnitType value) {
            if (value != null) {
                units = value;
            }
            return this;
        }
        
        /**
         * Setter method for the units associated with any length 
         * data.
         * @param value The unit information.
         * @return The builder object.
         */
        public ElevationDataPointBuilder source(
                TerrainDataFileType value) {
            if (value != null) {
                source = value;
            }
            return this;
        }
        
        /**
         * Setter method for the data structure containing the lat/lon 
         * geodetic point information.
         * @param value The location data.
         * @return The builder object.
         */
        public ElevationDataPointBuilder withGeodeticCoordinate(
                GeodeticCoordinate value) {
            coordinate = value;
            return this;
        }
        
        /**
         * Setter method for the data structure containing the accuracy 
         * data associated with the overall DEM frame.
         * @param value The accuracy data.
         * @return The builder object.
         */
        public ElevationDataPointBuilder withDEMFrameAccuracy(
                DEMFrameAccuracy value) {
            accuracy = value;
            return this;
        }
        
        /**
         * Setter method for the elevation value associated with a given lat/lon
         * pair.
         * @param value The elevation value.
         */
        public ElevationDataPointBuilder elevation(int value) {
            elevation = value;
            return this;
        }
        
        /**
         * Method used to construct a <code>ElevationDataPoint</code>
         * object,
         * @return A constructed/validated object.
         * @throws IllegalStateException Thrown if the object fails 
         * any validation tests.
         */
        public ElevationDataPoint build() {
            if (units == HeightUnitType.FEET) {
                elevation = Constants.convertToFeet(elevation);
            }
            if ((classificationMarking == null) || 
                    classificationMarking.isEmpty()) {
                classificationMarking = DEFAULT_CLASSIFICATION_MARKING;
            }
            if ((producerCode == null) || 
                    producerCode.isEmpty()) {
                producerCode = DEFAULT_PRODUCER;
            }
            if (accuracy == null) {
                accuracy = new DEMFrameAccuracy.DEMFrameAccuracyBuilder().build();
            }
            if (coordinate == null) {
                coordinate = new GeodeticCoordinate.GeodeticCoordinateBuilder().build();
            }
            ElevationDataPoint point = new ElevationDataPoint(this);
            validate(point);
            return point;
        }
        
        /**
         * The only thing we have to validate is the input elevation data.
         * The rest of the objects were already validated by their respective 
         * builder objects.
         * 
         * @throws IllegalStateException Thrown if the elevation value is out 
         * of range.
         */
        private void validate(ElevationDataPoint obj) throws IllegalStateException {
            if (units == HeightUnitType.FEET) {
                if (elevation > Constants.convertToFeet(MAX_ELEVATION)) { 
                    throw new IllegalStateException("Invalid value for the "
                            + "elevation.  The elevation must be between [ 0.."
                            + Constants.convertToFeet(MAX_ELEVATION)
                            + " ].");
                }
                else {
                    if (elevation > MAX_ELEVATION) { 
                        throw new IllegalStateException("Invalid value for the "
                                + "elevation.  The elevation must be between [ 0.."
                                + MAX_ELEVATION
                                + " ].");
                    }
                }
            }
        }
    }
}
