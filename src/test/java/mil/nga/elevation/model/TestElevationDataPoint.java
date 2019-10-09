package mil.nga.elevation.model;

import org.junit.Test;
import org.junit.Assert;

import mil.nga.elevation.Constants;
import mil.nga.elevation_services.model.HeightUnitType;

public class TestElevationDataPoint implements Constants {

    /**
     * Test that construction without setting internal members returns the 
     * expected default object.
     */
    @Test
    public void testDefaultConstruction() {
        ElevationDataPoint pt = new ElevationDataPoint.ElevationDataPointBuilder()
                .elevation(MAX_ELEVATION)
                .build();
        Assert.assertEquals(-1, pt.getAccuracy().getAbsHorzAccuracy());
        Assert.assertEquals(-1, pt.getAccuracy().getAbsVertAccuracy());
        Assert.assertEquals(-1, pt.getAccuracy().getRelHorzAccuracy());
        Assert.assertEquals(-1, pt.getAccuracy().getRelVertAccuracy());
        Assert.assertEquals(HeightUnitType.METERS, pt.getAccuracy().getUnits());
        Assert.assertEquals(HeightUnitType.METERS, pt.getUnits());
        Assert.assertEquals(MAX_ELEVATION, pt.getElevation());
        Assert.assertEquals(0.0, pt.getGeodeticCoordinate().getLat(), 0.0);
        Assert.assertEquals(0.0, pt.getGeodeticCoordinate().getLat(), 0.0);
    }
}
