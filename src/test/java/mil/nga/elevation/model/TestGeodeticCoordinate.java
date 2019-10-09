package mil.nga.elevation.model;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Rule;

import mil.nga.TestCoordinates;

public class TestGeodeticCoordinate extends TestCoordinates {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void testConstruction() {
        
        Iterator<String> latIterator = validLatitudes.keySet().iterator();
        Iterator<String> lonIterator = validLongitudes.keySet().iterator();
        
        while (latIterator.hasNext() && lonIterator.hasNext()) {
            
            String lat = latIterator.next();
            String lon = lonIterator.next();
            GeodeticCoordinate coord = new GeodeticCoordinate.GeodeticCoordinateBuilder()
                    .lat(lat)
                    .lon(lon)
                    .build();
            Assert.assertEquals(
                    (double)validLatitudes.get(lat), 
                    coord.getLat(),
                    0.0);
            Assert.assertEquals(
                    (double)validLongitudes.get(lon), 
                    coord.getLon(),
                    0.0);
        }
    }
    
    @Test
    public void testBadLatitude() {
        thrown.expect(IllegalStateException.class);
        GeodeticCoordinate coord = new GeodeticCoordinate.GeodeticCoordinateBuilder()
                .lat(invalidLatitudes[0])
                .lon(0.0)
                .build();
    }
    
    @Test
    public void testBadLongitude() {
        thrown.expect(IllegalStateException.class);
        GeodeticCoordinate coord = new GeodeticCoordinate.GeodeticCoordinateBuilder()
                .lat(0.0)
                .lon(invalidLongitudes[0])
                .build();
    }
    
    @Test
    public void testDefaultConstruction() {
        GeodeticCoordinate coord = new GeodeticCoordinate.GeodeticCoordinateBuilder()
                .build();
        Assert.assertEquals(coord.getLat(), 0.0, 0.0);
        Assert.assertEquals(coord.getLon(), 0.0, 0.0);
    }
}
