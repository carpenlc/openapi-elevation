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
import mil.nga.elevation.services.ElevationDataService;
import mil.nga.elevation.utils.ConversionUtils;
import mil.nga.elevation_services.model.ElevationQuery;
import mil.nga.elevation_services.model.ElevationResponse;
import mil.nga.elevation_services.model.Error;

import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-09-17T12:49:34.296Z[Etc/GMT-0]")

@Controller
@RequestMapping("${openapi.elevationServices.base-path:/elevation/v1}")
public class ElevationAtApiController implements ElevationAtApi {

    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(ElevationAtApiController.class);
    
    /**
     * Manually added auto-wired reference to the elevation data service bean.
     */
    @Autowired
    ElevationDataService elevDataService;
    
    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ElevationAtApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * Method manually added to generated stub providing the response 
     * associated with calls to the <code>ElevationAt<code> end point when 
     * called with an HTTP GET.  
     * 
     * @param pts List of coordinates in Lon, Lat order.
     * @param units The output units for the elevation information.
     * @param source The terrain data file source information to use for the
     * backing elevation data.
     * @param operation Not used.
     */
    @Override
    public ResponseEntity<Object> getElevationAtGET(
            String pts,  
            String heightType,
            String source,
            String operation) {

        String            arguments = ConversionUtils.toString(pts, heightType, source);
        long              start     = System.currentTimeMillis();
        ElevationResponse response  = null;
        
        try {
            LOGGER.info("Processing ElevationAt GET endpoint request for "
                    + "input arguments [ "
                    + arguments
                    + " ].");
            response = elevDataService.getElevationAt(pts, heightType, source);
            LOGGER.info("ElevationAt GET endpoint processed in [ "
                    + (System.currentTimeMillis() - start)
                    + " ] ms.");
        }
        catch (ApplicationException ae) {
            LOGGER.error("ApplicationException encountered while processing "
                    + "ElevationAt GET endpoint.  Input arguments [ "
                    + arguments
                    + " ].  Error message => [ "
                    + ae.toString()
                    + " ].");
            Error err = new Error();
            err.setCode(ae.getErrorCode());
            err.setMessage(ae.getErrorMessage());
            return new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST);
        }
        if (response == null) {
            LOGGER.error("Unable to generate a valid response for "
                    + "ElevationAt GET endpoint.  Input arguments [ "
                    + arguments
                    + " ].  See previous errors.");
            Error err = new Error();
            err.setCode(ErrorCodes.INTERNAL_EXCEPTION.getErrorCode());
            err.setMessage(ErrorCodes.INTERNAL_EXCEPTION.getErrorMessage());
            return new ResponseEntity<Object>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
    
    /**
     * Method manually added to generated stub providing the response 
     * associated with calls to the <code>ElevationAt<code> end point when 
     * called with an HTTP POST.  
     * 
     * @param elevationQuery Deserialized query parameters.
     */
    @Override
    public ResponseEntity<Object> getElevationAtPOST(ElevationQuery elevationQuery) {
        
        String            arguments = ConversionUtils.toString(elevationQuery);
        long              start     = System.currentTimeMillis();
        ElevationResponse response  = null;
        
        try {    
            LOGGER.info("Processing ElevationAt POST endpoint request for "
                    + "input arguments [ "
                    + arguments
                    + " ].");
            response = elevDataService.getElevationAt(elevationQuery);
            LOGGER.info("ElevationAt GET endpoint processed in [ "
                    + (System.currentTimeMillis() - start)
                    + " ] ms.");
        }
        catch (ApplicationException ae) {
            LOGGER.error("ApplicationException encountered while processing "
                    + "ElevationAt POST endpoint.  Input arguments [ "
                    + arguments
                    + " ].  Error message => [ "
                    + ae.toString()
                    + " ].");
            Error err = new Error();
            err.setCode(ae.getErrorCode());
            err.setMessage(ae.getErrorMessage());
            return new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST);
        }
        if (response == null) {
            LOGGER.error("Unable to generate a valid response for "
                    + "ElevationAt POST endpoint.  Input arguments [ "
                    + arguments
                    + " ].  See previous errors.");
            Error err = new Error();
            err.setCode(ErrorCodes.INTERNAL_EXCEPTION.getErrorCode());
            err.setMessage(ErrorCodes.INTERNAL_EXCEPTION.getErrorMessage());
            return new ResponseEntity<Object>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
}
