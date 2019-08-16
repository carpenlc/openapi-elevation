package mil.nga.elevation.openapi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import mil.nga.elevation.openapi.model.ElevationQuery;
import mil.nga.elevation.openapi.model.ElevationResponse;

import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-08-16T14:03:58.428Z[Etc/GMT-0]")

@Controller
@RequestMapping("${openapi.elevationServices.base-path:/v1}")
public class ElevationAtApiController implements ElevationAtApi {

    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(ElevationAtApiController.class);
    
    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ElevationAtApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
	public ResponseEntity<ElevationResponse> getElevationAt(ElevationQuery elevationQuery) {
		LOGGER.info("Method called!");
		LOGGER.info("Received => " + elevationQuery.toString());
		return ResponseEntity.ok(new ElevationResponse());
    	
	}
}
