package mil.nga.elevation_services.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CoordinateType
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-08-19T14:21:12.307-05:00[America/Chicago]")

public class CoordinateType   {
  @JsonProperty("lat")
  private Double lat;

  @JsonProperty("lon")
  private Double lon;

  public CoordinateType lat(Double lat) {
    this.lat = lat;
    return this;
  }

  /**
   * Get lat
   * minimum: -90.0
   * maximum: 90.0
   * @return lat
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@DecimalMin("-90.0") @DecimalMax("90.0") 
  public Double getLat() {
    return lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public CoordinateType lon(Double lon) {
    this.lon = lon;
    return this;
  }

  /**
   * Get lon
   * minimum: -180.0
   * maximum: 180.0
   * @return lon
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@DecimalMin("-180.0") @DecimalMax("180.0") 
  public Double getLon() {
    return lon;
  }

  public void setLon(Double lon) {
    this.lon = lon;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CoordinateType coordinateType = (CoordinateType) o;
    return Objects.equals(this.lat, coordinateType.lat) &&
        Objects.equals(this.lon, coordinateType.lon);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lat, lon);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CoordinateType {\n");
    
    sb.append("    lat: ").append(toIndentedString(lat)).append("\n");
    sb.append("    lon: ").append(toIndentedString(lon)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

