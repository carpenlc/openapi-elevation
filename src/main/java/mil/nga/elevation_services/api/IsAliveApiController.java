package mil.nga.elevation_services.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import mil.nga.elevation.services.CellAvailabilityService;
import mil.nga.util.FileUtils;

import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-09-17T12:49:34.296Z[Etc/GMT-0]")

@Controller
@RequestMapping("${openapi.elevationServices.base-path:/elevation/v1}")
public class IsAliveApiController implements IsAliveApi {

	/**
	 * Temporary reference to the CellAvailabilityService
	 */
	@Autowired
	CellAvailabilityService cellAvailability;
	
    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public IsAliveApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * Method manually added to generated stub providing the response 
     * associated with calls to the isAlive() end point.
     */
    public ResponseEntity<String> isAlive() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Server [ ");
    	sb.append(FileUtils.getHostName().trim());
    	sb.append(" ] is alive!");
    	
    	// Temporary call to output a list of missing DEM cells.
    	// cellAvailability.CheckForMissingCells();
    	
    	return new ResponseEntity<String>(sb.toString(), HttpStatus.OK);
    }
}
