package mil.nga.elevation.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import mil.nga.elevation_services.model.TerrainDataFileType;

public interface TerrainDataFileDAO extends CrudRepository<TerrainDataFile, Long>{

	/**
	 * Method used to retrieve a list of terrain data files based on the 
	 * caller supplied latitude and longitude values.  
	 * 
	 * @param lat The latitude value.
	 * @param lon The longitude value.
	 * @param source The DEM source to use for the calculation of elevation 
	 * model data.
	 * @return A <code>List</code> of terrain data files matching the input 
	 * latitude/longitude coordinate pair.
	 */
    public List<TerrainDataFile> getTerrainDataFiles(
    		double lat, 
    		double lon, 
    		TerrainDataFileType source);
}
