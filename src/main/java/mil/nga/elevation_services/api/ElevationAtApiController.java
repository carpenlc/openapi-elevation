package mil.nga.elevation_services.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-08-19T14:21:12.307-05:00[America/Chicago]")

@Controller
@RequestMapping("${openapi.elevationServices.base-path:/v1}")
public class ElevationAtApiController implements ElevationAtApi {

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ElevationAtApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
