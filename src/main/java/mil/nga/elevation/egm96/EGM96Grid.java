/**
 * UNCLASSIFIED
 */
package mil.nga.elevation.egm96;

import java.io.Serializable;

/**
 * Class holding the contents of EGM96 spherical harmonic potential 
 * coefficient set.  The grid is distributed by NGA is computed at 15 arc 
 * minute spacings in north/south and east/west direction arranged as
 * follows:
 *
 *              90.00 N  +------------------+
 *                       |                  |
 *                       | 15' spacing N/S  |
 *                       |                  |
 *                       |                  |
 *                       | 15' spacing E/W  |
 *                       |                  |
 *             -90.00 N  +------------------+
 *                      0.00 E           360.00 E
 * 
 * The grid of elevation values generated and stored in this class is a 2D 
 * array of double values arranged as follows:
 * 
 *              90.00 N (721) +------------------+
 *                            |                  |
 *                            | 15' spacing N/S  |
 *                            |                  |
 *                            |                  |
 *                            | 15' spacing E/W  |
 *                            |                  |
 *             -90.00 S  (0)  +------------------+
 *                           (0)               (1441)
 * 
 * This class is populated by the <code>ParseEGM96Grid</code> class and 
 * serialized to disk.  It is then desserialized from disk and used by the 
 * <code>GeoidHeightFactory</code> class to produce height values based on 
 * lat/lon values.
 * 
 * @author L. Craig Carpenter
 *
 */
public class EGM96Grid implements Serializable {

    /**
     * Eclipse-generated serialVersionUID
     */
    private static final long serialVersionUID = -6630436209092467241L;
    private final double minLat;
    private final double maxLat;
    private final double minLon;
    private final double maxLon;
    private final double lonSpacing;
    private final double latSpacing;
    
    /**
     * Internal 2D array of height values
     */
    private final double [][] grid;
    
    /**
     * Protected constructor enforcing the builder creation pattern.
     * @param builder Object containing values for the private internal
     * members.
     */
    protected EGM96Grid (EGM96GridBuilder builder) {
        minLat     = builder.minLat;
        maxLat     = builder.maxLat;
        minLon     = builder.minLon;
        maxLon     = builder.maxLon;
        lonSpacing = builder.lonSpacing;
        latSpacing = builder.latSpacing;
        grid       = builder.grid;
    }
    
    /**
     * Accessor method for a single elevation value looked up by row/column.
     * 
     * @param row 2D row value.
     * @param column 2D column value.
     * @return The requested point.
     */
    public double getGridValue(int row, int column) {
        if (row < grid.length) {
            if (column < grid[0].length) {
                return grid[row][column];
            }
            else {
                throw new IllegalArgumentException("Column value out of range.");
            }
        }
        else {
            throw new IllegalArgumentException("Row value out of range.");
        }
    }
    
    /**
     * Getter method for the number of columns in the 2D grid.
     * @return The number of columns in the 2D grid.
     */
    public int getNumColumns() {
        return grid[0].length;
    }
    
    /**
     * Getter method for the number of rows in the 2D grid.
     * @return The number of rows in the 2D grid.
     */
    public int getNumRows() {
        return grid.length;
    }
    
    
    /**
     * Getter method for the minimum latitude value covered by the grid.
     * @return The minimum latitude.
     */
    public double getMinLat() {
        return minLat;
    }
    
    /**
     * Getter method for the minimum latitude value covered by the grid.
     * @return The minimum latitude.
     */
    public double getMaxLat() {
        return maxLat;
    }
       
    /**
     * Getter method for the minimum longitude value covered by the grid.
     * @return The minimum longitude.
     */
    public double getMinLon() {
        return minLon;
    }
    
    /**
     * Getter method for the maximum longitude value covered by the grid.
     * @return The maximum longitude.
     */
    public double getMaxLon() {
        return maxLon;
    }
    
    /**
     * Getter method for the spacing in the longitude direction.
     * @return The longitude spacing.
     */
    public double getLonSpacing() {
        return lonSpacing;
    }
    
    /**
     * Getter method for the spacing in the latitude direction.
     * @return The latitude spacing.
     */
    public double getLatSpacing() {
        return latSpacing;
    }
    
    /**
     * Convert to printable <code>String</code>.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Grid Parameters : min lat = [ ");
        sb.append(getMinLat());
        sb.append(" ], max lat = [ ");
        sb.append(getMaxLat());
        sb.append(" ], lat spacing = [ ");
        sb.append(getLatSpacing());
        sb.append(" ], min lon = [ ");
        sb.append(getMinLon());
        sb.append(" ], max lon = [ ");
        sb.append(getMaxLon());
        sb.append(" ], lon spacing = [ ");
        sb.append(getLonSpacing());
        sb.append(" ], num rows = [ ");
        sb.append(getNumRows());
        sb.append(" ], num columns = [ ");
        sb.append(getNumColumns());
        sb.append(" ].");
        return sb.toString();
    }
    
    /**
     * Static inner class implementing the builder creation pattern for 
     * objects of type <code>EGM96Grid</code>
     * 
     * @author L. Craig Carpenter
     */
    public static class EGM96GridBuilder {
        
        private double minLat;
        private double maxLat;
        private double minLon;
        private double maxLon;
        private double lonSpacing;
        private double latSpacing;
        private double [][] grid;
        
        // Internal variables
        private int    width;
        private int    height;
        private int    curCol;
        private int    curRow;
        
        /**
         * Setter method for the minimum latitude.
         * @param value String value of the min latitude. 
         */
        public EGM96GridBuilder minLat(String value) {
           if ((value == null) || (value.isEmpty())) {
               throw new IllegalStateException("Invalid minimum latitude value.");
           }
           minLat = Double.parseDouble(value);
           return this;
        }
        
        /**
         * Setter method for the minimum latitude.
         * @param value String value of the min latitude. 
         */
        public EGM96GridBuilder maxLat(String value) {
           if ((value == null) || (value.isEmpty())) {
               throw new IllegalStateException("Invalid maximum latitude value.");
           }
           maxLat = Double.parseDouble(value);
           return this;
        }
        
        /**
         * Setter method for the minimum longitude.
         * @param value String value of the min longitude. 
         */
        public EGM96GridBuilder minLon(String value) {
           if ((value == null) || (value.isEmpty())) {
               throw new IllegalStateException("Invalid minimum longitude value.");
           }
           minLon = Double.parseDouble(value);
           return this;
        }
        
        /**
         * Setter method for the minimum longitude.
         * @param value String value of the min longitude. 
         */
        public EGM96GridBuilder maxLon(String value) {
           if ((value == null) || (value.isEmpty())) {
               throw new IllegalStateException("Invalid maximum longitude value.");
           }
           maxLon = Double.parseDouble(value);
           return this;
        }
        
        /**
         * Setter method for the latitude spacing.
         * @param value Spacing of entries in the latitude direction. 
         */
        public EGM96GridBuilder latSpacing(String value) {
           if ((value == null) || (value.isEmpty())) {
               throw new IllegalStateException("Invalid latitude spacing value.");
           }
           latSpacing = Double.parseDouble(value);
           return this;
        }
        
        /**
         * Setter method for the longitude spacing.
         * @param value Spacing of entries in the longitude direction. 
         */
        public EGM96GridBuilder lonSpacing(String value) {
           if ((value == null) || (value.isEmpty())) {
               throw new IllegalStateException("Invalid longitude spacing value.");
           }
           lonSpacing = Double.parseDouble(value);
           return this;
        }
        
        /**
         * Add an elevation value to the internal 2D array. 
         * 
         * Note: This method loads the rows in reverse so the output 2D array is 
         * latitudes ordered from [0] (i.e. -90) to [721] (i.e. +90).
         * 
         * @param value A single elevation value.
         */
        public EGM96GridBuilder setHeightValue(String value) {
            if ((value == null) || (value.isEmpty())) {
                throw new IllegalStateException("Invalid height value.");
            }
            if (grid == null) {
                width  = (int)((Math.abs(minLon) + Math.abs(maxLon))/lonSpacing)+1;
                height = (int)((Math.abs(minLat) + Math.abs(maxLat))/latSpacing)+1;
                grid   = new double[height][width];
                curRow = height-1;
            }
            grid[curRow][curCol] = Double.parseDouble(value);
            curCol++;
            if (curCol==width) {
                curCol=0;
                curRow--;
            }
            return this;
        }
        
        /**
         * Construct a new <code>EGM96Grid</code> object.
         * @return the constructed <code>EGM96Grid</code> object.
         * @throws IllegalArgumentException Thrown if any values are out of 
         * allowable range.
         */
        public EGM96Grid build() {
            return new EGM96Grid(this);
        }
    }
}
