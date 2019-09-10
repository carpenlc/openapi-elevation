package mil.nga.elevation.utils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import mil.nga.elevation.exceptions.ApplicationException;
import mil.nga.elevation.model.GeodeticCoordinate;

public class TestCoordinateUtils {

	@Test
	public void testTruncate() {
		Assert.assertEquals(1, CoordinateUtils.truncate(1.5));
		Assert.assertEquals(-100, CoordinateUtils.truncate(-99.5));
		Assert.assertEquals(99, CoordinateUtils.truncate(99.7));
		Assert.assertEquals(0, CoordinateUtils.truncate(0.999));
		Assert.assertEquals(-1, CoordinateUtils.truncate(-0.999));
	}
	
	@Test
	public void testConvertLat() {
		Assert.assertEquals("n09",  CoordinateUtils.convertLat(9.5));
		Assert.assertEquals("n38",  CoordinateUtils.convertLat(38.1));
		Assert.assertEquals("n37",  CoordinateUtils.convertLat(37.7));
		Assert.assertEquals("s39",  CoordinateUtils.convertLat(-38.1));
		Assert.assertEquals("s38",  CoordinateUtils.convertLat(-37.7));
		Assert.assertEquals("s01",  CoordinateUtils.convertLat(-0.5));
	}
	
	@Test
	public void testConvertLon() {
		Assert.assertEquals("e009",  CoordinateUtils.convertLon(9.5));
		Assert.assertEquals("e038",  CoordinateUtils.convertLon(38.1));
		Assert.assertEquals("e037",  CoordinateUtils.convertLon(37.7));
		Assert.assertEquals("e117",  CoordinateUtils.convertLon(117.7));
		Assert.assertEquals("w039",  CoordinateUtils.convertLon(-38.1));
		Assert.assertEquals("w038",  CoordinateUtils.convertLon(-37.7));
		Assert.assertEquals("w001",  CoordinateUtils.convertLon(-0.5));
		Assert.assertEquals("w117",  CoordinateUtils.convertLon(-116.7));
	}
}
