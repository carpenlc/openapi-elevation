package mil.nga.elevation_services.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import io.swagger.annotations.ApiParam;
import mil.nga.elevation_services.model.MinMaxElevationQueryWKT;
import mil.nga.elevation_services.model.MinMaxElevationResponse;

import java.util.Optional;

import javax.validation.Valid;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-09-10T14:44:29.236Z[Etc/GMT-0]")

@Controller
@RequestMapping("${openapi.elevationServices.base-path:/elevation/v1}")
public class MinMaxElevationWKTApiController implements MinMaxElevationWKTApi {

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public MinMaxElevationWKTApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    public ResponseEntity<MinMaxElevationResponse> getElevationAtWKTPOST(
    		MinMaxElevationQueryWKT minMaxElevationQueryWKT) {
    	
    	return null;
    }

}
