package mil.nga.elevation.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import mil.nga.elevation.ErrorCodes;
import mil.nga.elevation.exceptions.ApplicationException;
import mil.nga.elevation.model.GeodeticCoordinate;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.TerrainDataFileType;

public class TestConversionUtils {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void testConvertHeightUnitType() throws ApplicationException {
        Assert.assertEquals(HeightUnitType.FEET, ConversionUtils.convertHeightUnitType("FEET"));
        Assert.assertEquals(HeightUnitType.FEET, ConversionUtils.convertHeightUnitType("feet"));
        Assert.assertEquals(HeightUnitType.METERS, ConversionUtils.convertHeightUnitType("METERS"));
        Assert.assertEquals(HeightUnitType.METERS, ConversionUtils.convertHeightUnitType("meters"));
        Assert.assertEquals(HeightUnitType.METERS, ConversionUtils.convertHeightUnitType(null));
    }
    
    @Test
    public void testConvertHeightUnitTypeFailure() throws ApplicationException {
        thrown.expect(ApplicationException.class);
        thrown.expectMessage(ErrorCodes.INVALID_UNITS.getErrorMessage());
        ConversionUtils.convertHeightUnitType("BLAH");
    }
    
    @Test
    public void testConvertTerrainDataFileType() throws ApplicationException {
        Assert.assertEquals(TerrainDataFileType.DTED0, ConversionUtils.convertTerrainDataFileType(null));
        Assert.assertEquals(TerrainDataFileType.DTED0, ConversionUtils.convertTerrainDataFileType(""));
        Assert.assertEquals(TerrainDataFileType.DTED0, ConversionUtils.convertTerrainDataFileType("DTED0"));
        Assert.assertEquals(TerrainDataFileType.DTED0, ConversionUtils.convertTerrainDataFileType("dted0"));
        Assert.assertEquals(TerrainDataFileType.DTED1, ConversionUtils.convertTerrainDataFileType("DTED1"));
        Assert.assertEquals(TerrainDataFileType.DTED1, ConversionUtils.convertTerrainDataFileType("dted1"));
        Assert.assertEquals(TerrainDataFileType.DTED2, ConversionUtils.convertTerrainDataFileType("DTED2"));
        Assert.assertEquals(TerrainDataFileType.DTED2, ConversionUtils.convertTerrainDataFileType("dted2"));
        Assert.assertEquals(TerrainDataFileType.BEST, ConversionUtils.convertTerrainDataFileType("BEST"));
        Assert.assertEquals(TerrainDataFileType.BEST, ConversionUtils.convertTerrainDataFileType("best"));
        Assert.assertEquals(TerrainDataFileType.SRTM1, ConversionUtils.convertTerrainDataFileType("SRTM1"));
        Assert.assertEquals(TerrainDataFileType.SRTM1, ConversionUtils.convertTerrainDataFileType("srtm1"));
        Assert.assertEquals(TerrainDataFileType.SRTM2, ConversionUtils.convertTerrainDataFileType("SRTM2"));
        Assert.assertEquals(TerrainDataFileType.SRTM2, ConversionUtils.convertTerrainDataFileType("srtm2"));
        Assert.assertEquals(TerrainDataFileType.SRTM1F, ConversionUtils.convertTerrainDataFileType("SRTM1F"));
        Assert.assertEquals(TerrainDataFileType.SRTM1F, ConversionUtils.convertTerrainDataFileType("srtm1f"));
        Assert.assertEquals(TerrainDataFileType.SRTM2F, ConversionUtils.convertTerrainDataFileType("SRTM2F"));
        Assert.assertEquals(TerrainDataFileType.SRTM2F, ConversionUtils.convertTerrainDataFileType("srtm2f"));
    }
    
    @Test
    public void testConvertTerrainDataFileTypeFailure() throws ApplicationException {
        thrown.expect(ApplicationException.class);
        thrown.expectMessage(ErrorCodes.INVALID_SOURCE_TYPE.getErrorMessage());
        ConversionUtils.convertTerrainDataFileType("BLAHBLAH");
    }
    
    @Test
    public void testParseInputCoordinateList() throws ApplicationException {
        List<String> pts = new ArrayList<String>(); 
        pts.add("106 23 38 W");
        pts.add("37 20 19 N");
        pts.add("9.3");
        pts.add("9.3");
        pts.add("106 20 00 W");
        pts.add("37 20 00 N");
        List<GeodeticCoordinate> parsed = ConversionUtils.parseInputCoordinateList(pts);
        Assert.assertEquals(3, parsed.size());
    }
    
    @Test
    public void testParseInputCoordinateListFailure1() throws ApplicationException {
        thrown.expect(ApplicationException.class);
        thrown.expectMessage(ErrorCodes.INVALID_NUMBER_OF_INPUT_COORDINATES.getErrorMessage());
        List<String> pts = new ArrayList<String>(); 
        pts.add("106 23 38 W");
        pts.add("37 20 19 N");
        pts.add("9.3");
        pts.add("9.3");
        pts.add("106 20 00 W");
        ConversionUtils.parseInputCoordinateList(pts);
    }
    
    @Test
    public void testParseInputCoordinateListFailure2() throws ApplicationException {
        thrown.expect(ApplicationException.class);
        thrown.expectMessage(ErrorCodes.INVALID_INPUT_COORDINATES.getErrorMessage());
        List<String> pts = new ArrayList<String>(); 
        ConversionUtils.parseInputCoordinateList(pts);
    }

    @Test
    public void testParseCoords() throws ApplicationException {
        String coords1 = "9.3,9.3";
        List<GeodeticCoordinate> coordList1 = ConversionUtils.parseCoords(coords1);
        Assert.assertEquals(coordList1.size(), 1);
        Assert.assertEquals(coordList1.get(0).getLat(), 9.3, 0);
        Assert.assertEquals(coordList1.get(0).getLon(), 9.3, 0);
        String coords2 = "9.3,9.3,10.1,10.1";
        List<GeodeticCoordinate> coordList2 = ConversionUtils.parseCoords(coords2);
        Assert.assertEquals(coordList2.size(), 2);
        Assert.assertEquals(coordList2.get(0).getLat(), 9.3, 0);
        Assert.assertEquals(coordList2.get(0).getLon(), 9.3, 0);
        Assert.assertEquals(coordList2.get(1).getLat(), 10.1, 0);
        Assert.assertEquals(coordList2.get(1).getLon(), 10.1, 0);
    }
    /*
    @Test
    public void testToString1() {
        List<String> pts = new ArrayList<String>(); 
        pts.add("106 23 38 W");
        pts.add("37 20 19 N");
        pts.add("9.3");
        pts.add("9.3");
        pts.add("106 20 00 W");
        Assert.assertEquals(
                "Units [ FEET ], Source DEM [ DTED0 ], Points [ 106 23 38 W, 37 20 19 N, 9.3, 9.3, 106 20 00 W ]",
                ConversionUtils.toString(pts, "FEET", "DTED0"));
    }
    */
}
