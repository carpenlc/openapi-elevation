package mil.nga.elevation.services;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mil.nga.elevation.ErrorCodes;
import mil.nga.elevation.exceptions.ApplicationException;
import mil.nga.elevation.model.BoundingBox;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.MinMaxElevationQueryWKT;
import mil.nga.elevation_services.model.MinMaxElevationResponse;
import mil.nga.elevation_services.model.TerrainDataFileType;

/**
 * This class is essentially a translation layer between the OpenAPI/Spring
 * generated source code and the code previously developed for providing the 
 * min/max elevation data services functionality. This class is very similar
 * to the <code>ElevationExtremesService</code> class with the added complexity
 * that it must decode the input WKT string.
 * 
 * @author L. Craig Carpenter
 */
@Component
public class ElevationExtremesServiceWKT extends ElevationExtremesService {

    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(ElevationExtremesServiceWKT.class);
    
    /**
     * AutoWired reference to the data repository.
     */
	@Autowired
	TerrainDataFileService repository;
	
	public BoundingBox getBoundingBox(String wkt) throws ApplicationException {
		BoundingBox.BoundingBoxBuilder builder = 
				new BoundingBox.BoundingBoxBuilder();
		
		if ((wkt != null) && (!wkt.isEmpty())) {

			try {
				WKTReader reader = new WKTReader();
				Polygon polygon = (Polygon)reader.read(wkt);
				if (polygon != null) {
					Envelope env = polygon.getEnvelopeInternal();
					if (env != null) {
						builder.lowerLeftLat(env.getMinY());
						builder.lowerLeftLon(env.getMinX());
						builder.upperRightLat(env.getMaxY());
						builder.upperRightLon(env.getMaxX());
					}
					else {
						LOGGER.error("Unable to retrieve envelope from WKT "
								+ "Polygon.  Envelope is null.  WKT [ "
								+ wkt
								+ " ].");
					    throw new ApplicationException.ApplicationExceptionBuilder()
							.errorCode(ErrorCodes.INVALID_QUERY_INVALID_WKT.getErrorCode())
							.errorMessage(ErrorCodes.INVALID_QUERY_INVALID_WKT.getErrorMessage())
							.build();
					}
				}
				else {
					LOGGER.error("Unable to convert WKT to a Polygon.  "
							+ "Resulting Polygon is null.  WKT [ "
							+ wkt
							+ " ].");
				    throw new ApplicationException.ApplicationExceptionBuilder()
						.errorCode(ErrorCodes.INVALID_QUERY_INVALID_WKT.getErrorCode())
						.errorMessage(ErrorCodes.INVALID_QUERY_INVALID_WKT.getErrorMessage())
						.build();
				}
			} 
			catch (ParseException pe) {
				LOGGER.error("ParseError encountered while parsing the input "
						+ "WKT String [ "
						+ wkt
						+ " ].  Error message => [ "
						+ pe.getMessage()
						+ " ].");
			    throw new ApplicationException.ApplicationExceptionBuilder()
					.errorCode(ErrorCodes.INVALID_QUERY_INVALID_WKT.getErrorCode())
					.errorMessage(ErrorCodes.INVALID_QUERY_INVALID_WKT.getErrorMessage())
					.build();
			}
		}
		else {
			throw new ApplicationException.ApplicationExceptionBuilder()
				.errorCode(ErrorCodes.INVALID_QUERY_NO_WKT.getErrorCode())
				.errorMessage(ErrorCodes.INVALID_QUERY_NO_WKT.getErrorMessage())
				.build();
		}
		return builder.build();
	}
	
	/**
	 * Convenience method used to convert the user-supplied parameters
	 * from the HTTP POST call to the <code>MinMaxElevationWKT</code> 
	 * end point into a format expected by the ElevationExtremesFactory 
	 * class.
	 *  
	 * @param query Deserialized query object.
	 * @return Response object to be serialized and sent to the caller.
	 * @throws ApplicationException Thrown for all known issues.
	 */
	public MinMaxElevationResponse getMinMaxElevation(
			MinMaxElevationQueryWKT query) 
					throws ApplicationException {
		if ((query != null) && 
				(query.getWkt() != null) && 
				(!query.getWkt().isEmpty())) {  
			return getMinMaxElevation(
					query.getWkt(), 
					query.getHeightType(), 
					query.getSource());
		}
		else {
			throw new ApplicationException.ApplicationExceptionBuilder()
				.errorCode(ErrorCodes.INVALID_QUERY.getErrorCode())
				.errorMessage(ErrorCodes.INVALID_QUERY.getErrorMessage())
				.build();
		}
	}
	
	/**
	 * This method calculates the cells that intersect the user-defined 
	 * WKT polygon.  It builds the polygon, then uses the internal 
	 * bounding box of that polygon as the target area.  The code then 
	 * retrieves the cell information from the backing data repository.  
	 * The <code>ElevationExtremesFactory</code> is then
	 * called to determine the minimum and maximum elevation values 
	 * within user-defined bounding box.
	 * 
	 * The third-party Java Topology Suite (JTS) is utilized for the 
	 * parsing of the WKT string and the associated geospatial processing.
	 * 
	 * @param wkt The "well-known" text String.
	 * @param units The output units for the elevation values. 
	 * @param source The source DEM type.
	 * @return Response object to be serialized and sent to the caller.
	 * @throws ApplicationException
	 */
	public MinMaxElevationResponse getMinMaxElevation(
			String              wkt,
			HeightUnitType      units,
			TerrainDataFileType source) throws ApplicationException {
		
		return getMinMaxElevation(
				getBoundingBox(wkt), 
				units, 
				source);
	}
}
