/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (4.1.1-SNAPSHOT).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package mil.nga.elevation_services.api;

import mil.nga.elevation_services.model.ElevationQuery;
import mil.nga.elevation_services.model.ElevationResponse;
import mil.nga.elevation_services.model.Error;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-09-17T12:49:34.296Z[Etc/GMT-0]")

@Validated
@Api(value = "ElevationAt", description = "the ElevationAt API")
public interface ElevationAtApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    @ApiOperation(value = "Determine the elevation height value at a particular geographic location", nickname = "getElevationAtGET", notes = "", response = ElevationResponse.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Elevation data requested.", response = ElevationResponse.class),
        @ApiResponse(code = 200, message = "unexpected error", response = Error.class) })
    @RequestMapping(value = "/ElevationAt",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<Object> getElevationAtGET(@NotNull @ApiParam(value = "Comma seperated list of geographic points in lon, lat order.  The coordinates can be specified in either DMS format or decimal degrees.", required = true) @Valid @RequestParam(value = "pts", required = true) String pts,@ApiParam(value = "The output units for the elevation height data.", allowableValues = "FEET, METERS", defaultValue = "METERS") @Valid @RequestParam(value = "heightType", required = false, defaultValue="METERS") String heightType,@ApiParam(value = "The source DEM type to use for calculating the elevation height data.", allowableValues = "DTED2, DTED1, DTED0, SRTM2, SRTM1, SRTM2F, SRTM1F, BEST", defaultValue = "DTED0") @Valid @RequestParam(value = "source", required = false, defaultValue="DTED0") String source,@ApiParam(value = "This parameter is no longer used.") @Valid @RequestParam(value = "operation", required = false) String operation) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"security\" : {    \"ownerProducer\" : \"ownerProducer\",    \"classification\" : \"classification\"  },  \"elevations\" : [ {    \"elevation\" : 0,    \"coordinate\" : {      \"lon\" : \"lon\",      \"lat\" : \"lat\"    },    \"absHorizontalAccuracy\" : 6,    \"relHorizontalAccuracy\" : 5,    \"absVerticalAccuracy\" : 1,    \"relVerticalAccuracy\" : 5  }, {    \"elevation\" : 0,    \"coordinate\" : {      \"lon\" : \"lon\",      \"lat\" : \"lat\"    },    \"absHorizontalAccuracy\" : 6,    \"relHorizontalAccuracy\" : 5,    \"absVerticalAccuracy\" : 1,    \"relVerticalAccuracy\" : 5  } ]}");
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    @ApiOperation(value = "Determine the elevation height value at a particular geographic location", nickname = "getElevationAtPOST", notes = "", response = ElevationResponse.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Elevation data requested.", response = ElevationResponse.class),
        @ApiResponse(code = 200, message = "unexpected error", response = Error.class) })
    @RequestMapping(value = "/ElevationAt",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    default ResponseEntity<Object> getElevationAtPOST(@ApiParam(value = "Client specified geographic point.  The coordinates can be specified in either DMS format or decimal degrees." ,required=true )  @Valid @RequestBody ElevationQuery elevationQuery) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"security\" : {    \"ownerProducer\" : \"ownerProducer\",    \"classification\" : \"classification\"  },  \"elevations\" : [ {    \"elevation\" : 0,    \"coordinate\" : {      \"lon\" : \"lon\",      \"lat\" : \"lat\"    },    \"absHorizontalAccuracy\" : 6,    \"relHorizontalAccuracy\" : 5,    \"absVerticalAccuracy\" : 1,    \"relVerticalAccuracy\" : 5  }, {    \"elevation\" : 0,    \"coordinate\" : {      \"lon\" : \"lon\",      \"lat\" : \"lat\"    },    \"absHorizontalAccuracy\" : 6,    \"relHorizontalAccuracy\" : 5,    \"absVerticalAccuracy\" : 1,    \"relVerticalAccuracy\" : 5  } ]}");
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
