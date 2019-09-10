package mil.nga.elevation.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mil.nga.elevation.ElevationExtremesFactory;
import mil.nga.elevation.dao.TerrainDataFile;
import mil.nga.elevation.exceptions.ApplicationException;
import mil.nga.elevation.model.BoundingBox;
import mil.nga.elevation.model.MinMaxElevation;
import mil.nga.elevation.utils.ConversionUtils;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.MinMaxElevationQuery;
import mil.nga.elevation_services.model.MinMaxElevationResponse;
import mil.nga.elevation_services.model.TerrainDataFileType;

/**
 * This class is essentially a translation layer between the OpenAPI/Spring
 * generated source code and the code previously developed for providing the 
 * min/max elevation data services functionality. 
 * 
 * @author L. Craig Carpenter
 */
@Component
public class ElevationExtremesService {

    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(ElevationExtremesService.class);
    
    /**
     * AutoWired reference to the data repository.
     */
	@Autowired
	TerrainDataFileService repository;
	
	/**
	 * This method will convert the calculate <code>MinMaxElevation</code>
	 * object into the generated type <code>MinMaxElevationResponse</code> 
	 * for responding to the caller.
	 * 
	 * @param minMax The calculated minimum and maximum values.
	 * @return An object to return to the caller.
	 */
	private MinMaxElevationResponse convertToResponse(MinMaxElevation minMax) {
		
		return null;
	}
	
	private MinMaxElevation compare(MinMaxElevation previous, MinMaxElevation current) {
		return null;
	}
	
	public MinMaxElevationResponse getMinMaxElevation(
			String lllon,
    		String lllat,
    		String urlon,
    		String urlat,
    		String units,
    		String source) throws ApplicationException {
		
		BoundingBox bbox = new BoundingBox.BoundingBoxBuilder()
				.lowerLeftLat(lllat)
				.lowerLeftLon(lllon)
				.upperRightLat(urlat)
				.upperRightLon(urlon)
				.build();
		
		return getMinMaxElevation(
				bbox, 
				ConversionUtils.convertHeightUnitType(units),
				ConversionUtils.convertTerrainDataFileType(source));
	}
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	public MinMaxElevationResponse getMinMaxElevation(MinMaxElevationQuery query) throws ApplicationException {
		
		BoundingBox bbox = new BoundingBox.BoundingBoxBuilder()
				.lowerLeftLat(query.getBbox().getLllat())
				.lowerLeftLon(query.getBbox().getLllon())
				.upperRightLat(query.getBbox().getUrlat())
				.upperRightLon(query.getBbox().getUrlon())
				.build();
				
		return getMinMaxElevation(bbox, query.getHeightType(), query.getSource());
		
	}
	
	public MinMaxElevationResponse getMinMaxElevation(
			BoundingBox bbox,
			HeightUnitType units,
			TerrainDataFileType source) throws ApplicationException {
		
		MinMaxElevation minMax = null;
		
		// Determine what frames intersect the incoming bounding box
        int minCellLat = Math.round((float) Math.floor(bbox.getLowerLeftLat()));
        int maxCellLat = Math.round((float) Math.ceil(bbox.getUpperLeftLat()));

        int minCellLon = Math.round((float) Math.floor(bbox.getLowerLeftLon()));
        int maxCellLon = Math.round((float) Math.ceil(bbox.getUpperRightLon()));
		
        // Loop through the latitude/longitude cells and retrieve the 
        // information associate with each cell from the repository.
        int lat = minCellLat;
        while (lat < maxCellLat) {
        	int lon = minCellLon;
        	while (lon < maxCellLon) {
        		//if (LOGGER.isDebugEnabled()) {
        			LOGGER.info("Processing lat [ " + lat + " ], lon [ " + lon + " ].");
        		//}
        		List<TerrainDataFile> files = repository.getTerrainDataFiles(lat, lon, source);
        		if ((files != null) && (files.size() > 0)) {
        			for (TerrainDataFile file : files) {
        				ElevationExtremesFactory factory = 
        						new ElevationExtremesFactory.ElevationExtremesFactoryBuilder()
        							.classificationMarking(file.getMarking())
        							.filePath(file.getUnixPath())
        							.units(units)
        							.sourceType(source)
        							.build();
        				MinMaxElevation tmpMinMax = factory.getMinMaxElevation(bbox);
            			minMax = compare(minMax, tmpMinMax);
        			}
        		}
        		else {
        			LOGGER.warn("No source data found for Latitude [ "
        					+ lat
        					+ " ], Longitude [ "
        					+ lon 
        					+ " ] and DEM source type [ "
        					+ source.toString()
        					+ " ].");
        		}
        	}
        }
        return convertToResponse(minMax);
	}
}
