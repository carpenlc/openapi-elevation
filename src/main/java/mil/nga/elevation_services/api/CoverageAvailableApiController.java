package mil.nga.elevation_services.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mil.nga.elevation.ErrorCodes;
import mil.nga.elevation.exceptions.ApplicationException;
import mil.nga.elevation.services.CoverageService;
import mil.nga.elevation.utils.ConversionUtils;
import mil.nga.elevation_services.model.CoordinateTypeArray;
import mil.nga.elevation_services.model.CoverageAvailableType;
import mil.nga.elevation_services.model.CoveragesAvailableType;
import mil.nga.elevation_services.model.Error;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-04-07T08:48:31.266-05:00[America/Chicago]")

@Controller
@RequestMapping("${openapi.elevationServices.base-path:/elevation/v1}")
public class CoverageAvailableApiController implements CoverageAvailableApi {

    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(CoverageAvailableApiController.class);
    
    /**
     * Manually added auto-wired reference to the elevation data service bean.
     */
    @Autowired
    CoverageService coverageService;
    
    private final NativeWebRequest request;

    @Autowired
    public CoverageAvailableApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * Implementation of the <code>CoveragesAvailable</code> endpoint when 
     * called with a GET request.  This method will log the request and 
     * leverage the <code>CoverageService</code> to look up the available 
     * coverages. 
     * 
     * @param lat User-requested latitude value.
     * @param lon User-requested longitude value.
     * @return A ResponseEntity object to be serialized and sent to the 
     * caller.
     */
    public ResponseEntity<Object> getCoverageAvailableGET( 
            String lat,
            String lon) {
        long                  start    = System.currentTimeMillis();
        CoverageAvailableType response = null;

        try {
            LOGGER.info("Processing CoverageAvailable GET endpoint request for "
                    + "input arguments:  latitude [ "
                    + lat
                    + " ], longitude [ "
                    + lon
                    + " ].");
            response = coverageService.getCoveragesAvailable(lat, lon);
            LOGGER.info("CoverageAvailable GET endpoint processed in [ "
                    + (System.currentTimeMillis() - start)
                    + " ] ms.");
        }
        catch (ApplicationException ae) {
            LOGGER.error("ApplicationException encountered while processing "
                    + "CoverageAvailable GET endpoint.  Input arguments:  "
                    + "latitude [ "
                    + lat
                    + " ], longitude [ "
                    + lon
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
                    + "CoverageAvailable GET endpoint.  Input arguments:   "
                    + "latitude [ "
                    + lat
                    + " ], longitude [ "
                    + lon 
                    + " ].");
            Error err = new Error();
            err.setCode(ErrorCodes.INTERNAL_EXCEPTION.getErrorCode());
            err.setMessage(ErrorCodes.INTERNAL_EXCEPTION.getErrorMessage());
            return new ResponseEntity<Object>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
    
    /**
     * Implementation of the <code>CoveragesAvailable</code> endpoint when 
     * called with a POST request.  This method will log the request and 
     * leverage the <code>CoverageService</code> to look up the available 
     * coverages. 
     * 
     * @param coordinateTypeArray An array of coordinates.
     * @return A ResponseEntity object to be serialized and sent to the 
     * caller.
     */
    public ResponseEntity<Object> getCoverageAvailablePOST(
            CoordinateTypeArray coordinateTypeArray) {
        
        long                   start     = System.currentTimeMillis();
        String                 arguments = ConversionUtils.toString(coordinateTypeArray);
        CoveragesAvailableType response  = null;
        
        try {
            LOGGER.info("Processing CoverageAvailable POST endpoint request for "
                    + "input arguments: "
                    + arguments);
            response = coverageService.getCoveragesAvailable(coordinateTypeArray);
            LOGGER.info("CoverageAvailable POST endpoint processed in [ "
                    + (System.currentTimeMillis() - start)
                    + " ] ms.");
        }
        catch (ApplicationException ae) {
            LOGGER.error("ApplicationException encountered while processing "
                    + "CoverageAvailable POST endpoint.  Input arguments:  "
                    + arguments);
            Error err = new Error();
            err.setCode(ae.getErrorCode());
            err.setMessage(ae.getErrorMessage());
            return new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST);
        }
        if (response == null) {
            LOGGER.error("Unable to generate a valid response for "
                    + "CoverageAvailable POST endpoint.  Input arguments:   "
                    + arguments);
            Error err = new Error();
            err.setCode(ErrorCodes.INTERNAL_EXCEPTION.getErrorCode());
            err.setMessage(ErrorCodes.INTERNAL_EXCEPTION.getErrorMessage());
            return new ResponseEntity<Object>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
}
