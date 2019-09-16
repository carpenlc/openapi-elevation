package mil.nga.elevation_services.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import mil.nga.elevation.ErrorCodes;
import mil.nga.elevation.exceptions.ApplicationException;
import mil.nga.elevation.services.ElevationExtremesService;
import mil.nga.elevation.utils.ConversionUtils;
import mil.nga.elevation_services.model.Error;
import mil.nga.elevation_services.model.MinMaxElevationQuery;
import mil.nga.elevation_services.model.MinMaxElevationResponse;

import java.util.Optional;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-09-10T14:44:29.236Z[Etc/GMT-0]")
@Controller
@RequestMapping("${openapi.elevationServices.base-path:/elevation/v1}")
public class MinMaxElevationApiController implements MinMaxElevationApi {
	
	/**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(MinMaxElevationApiController.class);
    
    /**
     * Manually added auto-wired reference to the elevation data service bean.
     */
	@Autowired
	ElevationExtremesService minMaxService;
	
    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public MinMaxElevationApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * Method manually added to generated stub providing the response 
     * associated with calls to the <code>MinMaxElevation<code> end point when 
     * called with an HTTP POST.  
     * 
     * @param minMaxElevationQuery Deserialized query parameters.
     */
    public ResponseEntity<Object> getElevationAtPOST(
    		MinMaxElevationQuery minMaxElevationQuery) {
    	
    	String                  arguments = ConversionUtils.toString(minMaxElevationQuery);
    	long                    start     = System.currentTimeMillis();
    	MinMaxElevationResponse response  = null;
    	
		try {
			LOGGER.info("Processing MinMaxElevation POST endpoint request for "
					+ "input arguments: "
					+ arguments);
			response = minMaxService.getMinMaxElevation(minMaxElevationQuery);
			LOGGER.info("MinMaxElevation POST endpoint processed in [ "
    				+ (System.currentTimeMillis() - start)
    				+ " ] ms.");
		}
		catch (ApplicationException ae) {
			LOGGER.error("ApplicationException encountered while processing "
    				+ "MinMaxElevation POST endpoint.  Input arguments:  "
					+ arguments);
    		Error err = new Error();
    		err.setCode(ae.getErrorCode());
    		err.setMessage(ae.getErrorMessage());
    		return new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST);
		}
    	if (response == null) {
    		LOGGER.error("Unable to generate a valid response for "
    				+ "MinMaxElevation POST endpoint.  Input arguments:   "
    				+ arguments);
    		Error err = new Error();
    		err.setCode(ErrorCodes.INTERNAL_EXCEPTION.getErrorCode());
    		err.setMessage(ErrorCodes.INTERNAL_EXCEPTION.getErrorMessage());
    		return new ResponseEntity<Object>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<Object>(response, HttpStatus.OK);
    }


    /**
     * Method manually added to generated stub providing the response 
     * associated with calls to the MinMaxElevation end point when called with
     * an HTTP GET.  
     * 
     * @param lllon The lower-left longitude value.
	 * @param lllat The lower-left latitude value.
	 * @param urlon The upper-right longitude value.
	 * @param urlat The upper-right latitude value.
	 * @param heightType The output elevation units.
	 * @param source The source DEM type.
	 * @return A concatenated String of the input function arguments.
     */
    public ResponseEntity<Object> getMinMaxElevationAtGET(
    		String lllon,
    		String lllat,
    		String urlon,
    		String urlat,
    		String heightType,
    		String source,
    		String operation) {

    	String                  arguments = 
    			ConversionUtils.toString(lllon, lllat, urlon, urlat, heightType, source);
    	long                    start     = System.currentTimeMillis();
    	MinMaxElevationResponse response  = null;
    	
		try {
			LOGGER.info("Processing MinMaxElevation GET endpoint request for "
					+ "input arguments: "
					+ arguments);
			response = minMaxService.getMinMaxElevation(
					lllon, 
					lllat, 
					urlon, 
					urlat, 
					heightType, 
					source);
			LOGGER.info("MinMaxElevation GET endpoint processed in [ "
    				+ (System.currentTimeMillis() - start)
    				+ " ] ms.");
		}
		catch (ApplicationException ae) {
			LOGGER.error("ApplicationException encountered while processing "
    				+ "MinMaxElevation GET endpoint.  Input arguments:  "
					+ arguments);
    		Error err = new Error();
    		err.setCode(ae.getErrorCode());
    		err.setMessage(ae.getErrorMessage());
    		return new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST);
		}
    	if (response == null) {
    		LOGGER.error("Unable to generate a valid response for "
    				+ "MinMaxElevation GET endpoint.  Input arguments:   "
    				+ arguments);
    		Error err = new Error();
    		err.setCode(ErrorCodes.INTERNAL_EXCEPTION.getErrorCode());
    		err.setMessage(ErrorCodes.INTERNAL_EXCEPTION.getErrorMessage());
    		return new ResponseEntity<Object>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
}
