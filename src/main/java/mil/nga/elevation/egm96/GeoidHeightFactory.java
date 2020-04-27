/**
 * UNCLASSIFIED
 */
package mil.nga.elevation.egm96;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Determine the EGM96 height (i.e. difference between geoid and ellipsoid) at 
 * a given latitude/longitude location.  The EGM96 height is obtained via 
 * interpolation based on an input grid of elevation points.  The grid is 
 * supplied by NGA and includes a height at 15-minute intervals ranging from 
 * -90..90 latitude and 0..360 longitude.  This class implements the code to 
 * look up and interpolate for given latitude/longitude values.
 * 
 * This class expects the zipped serialized grid file to be on the classpath.  
 * If it is not an exception will be thrown when attempts are made to obtain 
 * a reference to the singleton object.
 *  
 * @author L. Craig Carpenter
 */
public class GeoidHeightFactory {

    /**
     * Set up the logback system for use throughout the class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
            GeoidHeightFactory.class.getName());
    
    /**
     * Name of the serialized object file containing the EGM96 grid file.
     */
    public static final String DEFAULT_EGM96_OBJ_FILE = "EGM96Obj.zip";
    
    /**
     * EGM96 Grid data
     */
    protected EGM96Grid grid = null;
    
    /**
     * The default constructor will load the pre-serialized object 
     * containing the EGM96 grid data.
     * @throws ClassNotFoundException Thrown if the project is configured 
     * incorrectly.
     * @throws IOException Thrown if there are problems reading the target 
     * object file.
     */
    private GeoidHeightFactory() throws ClassNotFoundException, IOException {
        loadGridFile();
    }
    
    /**
     * Load the EGM96 grid from the classpath.
     * @throws ClassNotFoundException Thrown if the project is configured 
     * incorrectly.
     * @throws IOException Thrown if there are problems reading the target 
     * object file.
     */
    protected void loadGridFile() throws ClassNotFoundException, IOException {
        long        start       = System.currentTimeMillis();
        ClassLoader cLoader     = getClass().getClassLoader();
        URL         gridFileUrl = cLoader.getResource(DEFAULT_EGM96_OBJ_FILE);
        
        if (gridFileUrl == null) {
            throw new IllegalArgumentException("Target grid file not on "
                    + "classpath [ "
                    + DEFAULT_EGM96_OBJ_FILE
                    + " ].");
        } else {
            File gridFile = new File(gridFileUrl.getFile());
            if ((!gridFile.exists()) || (!gridFile.canRead())) {
                throw new IllegalStateException("Unable to access target "
                        + "grid file [ "
                        + gridFile.getAbsolutePath()
                        + " ].");
            }
            try (ObjectInputStream o = new ObjectInputStream(
                    new GZIPInputStream(
                            new FileInputStream(gridFile)))) {
                grid = (EGM96Grid)o.readObject();
            }
            catch (ClassNotFoundException cnfe) {
                LOGGER.error("loadGridFile: ClassNotFoundException raised "
                        + "while deserializing the EGM96 grid object from "
                        + "a file.  Target filename => [ {} ].  "
                        + "Exception message => [ {} ].", 
                        gridFile.getAbsolutePath(), 
                        cnfe.getMessage());
            }
            catch (IOException ioe) {
                LOGGER.error("loadGridFile: Unexpected IOException raised "
                        + "while deserializing the EGM96 grid object from a "
                        + "file.  Target filename => [ {} ].  Exception "
                        + "message => [ {} ].", 
                        gridFile.getAbsolutePath(), 
                        ioe.getMessage());
            }
        }
        LOGGER.info("Grid file loaded in [ {} ] ms.", 
                (System.currentTimeMillis()-start));
    }
    
    /**
     * Normalize the latitude for use in calculating the row needed for 
     * an input point value.
     * 
     * @param lon The input latitude value.
     * @return The normalized latitude.
     */
    protected double normalizeLat(double lat) {
        return 90.0+lat;
    }
    
    /**
     * Most of the time longitude coordinates are supplied from -180..180.
     * The EGM96 grid is laid out from 0..360.  This method converts between
     * the two frames.
     * 
     * @param lon The input longitude value.
     * @return The normalized longitude.
     */
    protected double normalizeLon(double lon) {
        if (lon < 0) {
            return 360.0+lon;
        }
        else {
            return lon;
        }
    }
    
    /**
     * Perform a bilinear interpolation to obtain the height at a specific
     * point.  
     * 
     * @param lowerLeft The nearest lower left height value.
     * @param lowerRight The nearest lower right height value.
     * @param upperLeft The nearest upper left height value.
     * @param upperRight The nearest upper right height value.
     * @param deltaLat Distance (in lat) from the lower-left corner.
     * @param deltaLon Distance (in lon) from the lower-left corner.
     * @return The interpolated height value.
     */
    public double interpolate(
            double lowerLeft, 
            double lowerRight, 
            double upperLeft, 
            double upperRight,
            double deltaLat,
            double deltaLon) {
        
        double R1=((grid.getLonSpacing()-deltaLon)/grid.getLonSpacing())*lowerLeft +
                (deltaLon/grid.getLonSpacing())*lowerRight;
        
        double R2=((grid.getLonSpacing()-deltaLon)/grid.getLonSpacing())*upperLeft +
                (deltaLon/grid.getLonSpacing())*upperRight;
        
        double result = ((grid.getLatSpacing()-deltaLat)/grid.getLatSpacing())*R1 +
                (deltaLat/grid.getLatSpacing())*R2;
        
        return result;
    }
    
    /**
     * Obtain the EGM96 height (i.e. difference between geoid and ellipsoid) 
     * at the location specified by the input latitude/longitude. 
     * 
     * @param lat latitude value in decimal degrees (-90..90).
     * @param lon longitude value in decimal degrees (-180..180).
     * @return The height at the requested location.
     */
    public double getHeight(double lat, double lon) {
        
        if ((lat < -90.0) || (lat > 90.0)) { 
            throw new IllegalArgumentException("Latitude value out of range.  "
                    + "Must be in the range -90.0..90.0.");
        }
        if ((lon < -180.0) || (lon > 180.0)) {
            throw new IllegalArgumentException("Longitude value out of range.  "
                    + "Must be in the range -180.0..180.0.");
        }
        
        int    lonIndex = (int)Math.floor(normalizeLon(lon)/grid.getLonSpacing());
        double deltaLon = (normalizeLon(lon) - lonIndex*grid.getLonSpacing());
        int    latIndex = (int)Math.floor(normalizeLat(lat)/grid.getLatSpacing());
        double deltaLat = (normalizeLat(lat) - latIndex*grid.getLatSpacing());

        return interpolate(
                grid.getGridValue(latIndex, lonIndex),
                grid.getGridValue(latIndex, lonIndex+1),
                grid.getGridValue(latIndex+1, lonIndex),
                grid.getGridValue(latIndex+1, lonIndex+1),
                deltaLat,
                deltaLon);
    }
    
    /**
     * Return an instance of the <code>GeoidHeightFactory</code> singleton object.
     * 
     * @return Reference to the <code>GeoidHeightFactory</code> singleton object.
     * @throws ClassNotFoundException Thrown if the project is configured 
     * incorrectly.
     * @throws IOException Thrown if there are problems reading the target 
     * object file.
     */
    public static GeoidHeightFactory getInstance() 
            throws ClassNotFoundException, IOException {
        return GeoidHeightFactoryHolder.getSingleton();
    }
    
    /**
     * Static inner class used to construct the singleton instance.
     * @author L. Craig Carpenter
     */
    public static class GeoidHeightFactoryHolder {
        
        /**
         * Hide the constructor
         */
        private GeoidHeightFactoryHolder() { }
        
        /**
         * Reference to the singleton object.
         */
        private static GeoidHeightFactory singleton;
        
        /**
         * Accessor method for the singleton.
         * @return The singleton instance of the coordinate transformer.
         * @throws IOException 
         * @throws ClassNotFoundException 
         */
        public static GeoidHeightFactory getSingleton() throws ClassNotFoundException, IOException {
            if (singleton == null) {
                singleton = new GeoidHeightFactory();
            }
            return singleton;
        }
    }
    
    
    public static void main(String[] args) {
        
        try {
            //EGM96Grid grid = (new ParseEGM96Grid()).parse();
        
            GeoidHeightFactory factory = new GeoidHeightFactory();
            System.out.println(factory.grid.toString());
            //System.out.println(factory.getHeight(38.628155,269.779155));  
            //System.out.println(factory.getHeight(-14.621217, 305.021114));  
            System.out.println(factory.getHeight(46.874319, 102.448729));  
            System.out.println(factory.getHeight(-23.617446, 133.874712));  
            //System.out.println(factory.getHeight(38.625473, 359.999500));  
            System.out.println(factory.getHeight(-00.466744, 0.002300));
            System.out.println(factory.getHeight(0.0, 0.0));
            System.out.println(factory.getHeight(-10.0, 20.0));
            System.out.println(factory.getHeight(52.0126447, -0.96737327));
            System.out.println(factory.getHeight(51.89386995, -1.24038138));
            System.out.println(factory.getHeight(51.88021891, -0.96713243));
            System.out.println(factory.getHeight(52.02569738, -1.24033818));
            System.out.println(factory.getHeight(51.973496, -1.066919)); 
            System.out.println(factory.getHeight(51.973496, -1.062408)); 
            System.out.println(factory.getHeight(51.98, -1.062408));
            System.out.println(factory.getHeight(51.974, -1.06584));
            System.out.println(factory.getHeight(66.555, -68.555));
           
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
