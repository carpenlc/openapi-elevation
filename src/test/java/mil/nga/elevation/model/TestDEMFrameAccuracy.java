package mil.nga.elevation.model;

import org.junit.Test;
import org.locationtech.jts.util.Assert;

import mil.nga.elevation_services.model.HeightUnitType;

public class TestDEMFrameAccuracy {

	/**
	 * Test that construction without setting internal members returns the 
	 * expected default object.
	 */
	@Test
	public void testDefaultConstruction() {
		DEMFrameAccuracy accuracy = new DEMFrameAccuracy.DEMFrameAccuracyBuilder().build();
		Assert.equals(-1, accuracy.getAbsHorzAccuracy());
		Assert.equals(-1, accuracy.getAbsVertAccuracy());
		Assert.equals(-1, accuracy.getRelHorzAccuracy());
		Assert.equals(-1, accuracy.getRelVertAccuracy());
		Assert.equals(HeightUnitType.METERS, accuracy.getUnits());
	}
}
