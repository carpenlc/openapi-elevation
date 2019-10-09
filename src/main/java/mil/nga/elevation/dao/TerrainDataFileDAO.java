package mil.nga.elevation.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The Spring Data JPA interface class defining the database repository 
 * containing data associated with the available terrain data files.  
 * The database configuration parameters are stored in the Spring Boot 
 * <code>application.properties<code> file.
 * 
 * @author L. Craig Carpenter
 */
@Repository
public interface TerrainDataFileDAO 
        extends JpaRepository<TerrainDataFile, Long> {

    /**
     * Method used to retrieve a list of terrain data files based on the 
     * caller supplied latitude and longitude and source type.  We found
     * during testing that the database column containing the source DEM
     * type contains trailing spaces.  That means we cannot query as an
     * enumeration type and we have to include wildcards in the JPQL query
     * definition.
     * 
     * @param lat The latitude value.
     * @param lon The longitude value.
     * @param source The DEM source to use for the calculation of elevation 
     * model data.
     * @return A <code>List</code> of terrain data files matching the input 
     * latitude/longitude coordinate pair.
     */
    @Query("SELECT T FROM TerrainDataFile T WHERE T.lat = :latitude AND T.lon = :longitude AND T.source LIKE '%' || UPPER(:source) || '%'")
    public List<TerrainDataFile> findByLatAndLonAndSource (
            @Param("latitude")  String lat, 
            @Param("longitude") String lon, 
            @Param("source")    String source);
    
    /**
     * Method used to retrieve a list of terrain data files based on the 
     * caller supplied latitude and longitude.  This method assumes that the 
     * caller asked for the best available source DEM.  Results are sorted 
     * by the <code>BEST</code> column. 
     * 
     * @param lat The latitude value.
     * @param lon The longitude value.
     * @return A <code>List</code> of terrain data files matching the input 
     * latitude/longitude coordinate pair.
     */
    @Query("SELECT T FROM TerrainDataFile T WHERE T.lat = :latitude AND T.lon = :longitude order by T.best")
    public List<TerrainDataFile> findByLatAndLon (
            @Param("latitude")  String lat, 
            @Param("longitude") String lon);
    
    /**
     * Method used to retrieve a list of source DEM types available for a
     * caller supplied latitude and longitude.  
     * 
     * @param lat The latitude value.
     * @param lon The longitude value.
     * @return A <code>List</code> of DEM types available for a given 
     * latitude/longitude coordinate pair.
     */
    @Query("SELECT T.source FROM TerrainDataFile T WHERE T.lat = :latitude AND T.lon = :longitude order by T.best")
    public List<String> findCoverageByLatAndLon(
            @Param("latitude")  String lat, 
            @Param("longitude") String lon);
}
