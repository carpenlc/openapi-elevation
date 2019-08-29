package mil.nga.elevation_services.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-08-29T11:50:00.502Z[Etc/GMT-0]")

@Controller
@RequestMapping("${openapi.elevationServices.base-path:/elevation/v1}")
public class CoverageAvailableApiController implements CoverageAvailableApi {

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public CoverageAvailableApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
