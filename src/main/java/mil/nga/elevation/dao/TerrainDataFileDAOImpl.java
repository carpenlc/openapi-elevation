package mil.nga.elevation.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import mil.nga.elevation_services.model.TerrainDataFileType;

@Repository
public class TerrainDataFileDAOImpl implements TerrainDataFileDAO {

	/**
     * Set up the Log4j system for use throughout the class
     */        
    private static final Logger LOGGER = LoggerFactory.getLogger(
    		TerrainDataFileDAOImpl.class);
    
    /**
     * Injected JPA EntityManager object used to actually obtain a database 
     * connection.
     */
	@PersistenceContext
	protected EntityManager em;

	@Override
	public List<TerrainDataFile> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TerrainDataFile> findAllById(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends TerrainDataFile> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <S extends TerrainDataFile> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<TerrainDataFile> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(TerrainDataFile entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends TerrainDataFile> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

    /**
     * Latitude values are stored in the database as a truncated String 
     * containing a hemisphere designator.  This method converts the 
     * input double into the correct String format.
     * 
     * @param lat Target latitude value as a double.
     * @return String-based latitude data as it appears in the data store.
     */
    public static String convertLat(double lat) {
    	
    	StringBuilder  sb         = new StringBuilder();
    	String         hemisphere = (lat < 0) ? "s" : "n";
        String         latitude   = String.valueOf(Math.abs(lat));
        
        if (Math.abs(lat) < 10) {
        	sb.append(hemisphere);
        	sb.append("0");
        	sb.append(latitude);
        }
        else {
        	sb.append(hemisphere);
        	sb.append(latitude);
        }
        return sb.toString();
    }
    
    /**
     * Longitude values are stored in the database as a truncated String 
     * containing a hemisphere designator.  This method converts the 
     * input double into the correct String format.
     * 
     * @param lon Target longitude value as a double.
     * @return String-based longitude data as it appears in the data store.
     */
    public static String convertLon(double lon) {
    	
    	StringBuilder  sb         = new StringBuilder();
    	String         hemisphere = (lon < 0) ? "e" : "w";
    	String         longitude  = String.valueOf(Math.abs(lon));
        
    	if (Math.abs(lon) < 10) {
            sb.append(hemisphere);
         	sb.append("00");
         	sb.append(longitude);
        } else if (Math.abs(lon) < 100) {
         	sb.append(hemisphere);
         	sb.append("0");
         	sb.append(longitude);
        }
        else {
            sb.append(hemisphere);
          	sb.append(longitude); 
        }
    	return sb.toString();
    }
    
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
    		TerrainDataFileType source) {
    	
    	long                  startTime = System.currentTimeMillis();
    	List<TerrainDataFile> result    = null;
    	
    	String latString = convertLat(lat);
    	String lonString = convertLon(lon);
    	
    	if (em != null) {
    		CriteriaBuilder cb = em.getCriteriaBuilder();
    		CriteriaQuery<TerrainDataFile> cq = cb.createQuery(TerrainDataFile.class);
    		Root<TerrainDataFile> root = cq.from(TerrainDataFile.class);
    		cq.select(root);
    		ParameterExpression<String> latExpression = cb.parameter(String.class, "LAT");
    		ParameterExpression<String> lonExpression = cb.parameter(String.class, "LON");
    		
    		if (source == TerrainDataFileType.BEST) {
    			cq.where(
    		        cb.like(root.get("LAT"), latExpression),
    				cb.like(root.get("LON"), lonExpression)
    		    );
    			cq.orderBy(cb.asc(root.get("TYP")));
    		}
    		else {
    			ParameterExpression<String> sourceExpression = cb.parameter(String.class, "TYP");
    			cq.where(
        		    cb.like(root.get("LAT"), latExpression),
        		    cb.like(root.get("LON"), lonExpression),
        		    cb.like(root.get("TYP"), sourceExpression)
        		);    			
    		}
    		Query query = em.createQuery(cq);
    		
    		// Set the query parameters
    		if (source == TerrainDataFileType.BEST) {
    			query.setParameter("LAT", latString);
    			query.setParameter("LON", lonString);
    		}
    		else {
    			query.setParameter("LAT", latString);
    			query.setParameter("LON", lonString);
    			query.setParameter("TYP", source.name());
    		}
    		
    		result = query.getResultList();
    		
    		if (LOGGER.isDebugEnabled()) {
    			if (result != null) {
    				LOGGER.debug("[ "
    						+ result.size() 
    						+ " ] TerrainDataFile records selected in [ "
    						+ (System.currentTimeMillis() - startTime)
    						+ " ] ms.");
    			}
    			
    		}
    	}
    	else {
    		LOGGER.error("Hibernate not initialized.  "
    				+ "Output list will be null.");
    	}
    	return result;
}

}
