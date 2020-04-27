package mil.nga.elevation_services.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;

import mil.nga.elevation.services.CellAvailabilityService;
import mil.nga.elevation.services.TerrainDataFileService;
import mil.nga.util.FileUtils;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-04-07T08:48:31.266-05:00[America/Chicago]")

@Controller
@RequestMapping("${openapi.elevationServices.base-path:/elevation/v1}")
public class IsAliveApiController implements IsAliveApi {

    private final NativeWebRequest request;

    @Autowired
    public IsAliveApiController(NativeWebRequest request) {
        this.request = request;
    }

    /**
     * AutoWired reference to the data repository.
     */
    @Autowired
    CellAvailabilityService cellsAvailable;
    
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
        
        cellsAvailable.CheckForMissingCells();
        
        return new ResponseEntity<String>(sb.toString(), HttpStatus.OK);
    }
}
