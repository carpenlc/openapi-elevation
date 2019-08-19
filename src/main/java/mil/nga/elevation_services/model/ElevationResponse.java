package mil.nga.elevation_services.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import mil.nga.elevation_services.model.ElevationType;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.SecurityType;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ElevationResponse
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-08-19T14:21:12.307-05:00[America/Chicago]")

public class ElevationResponse   {
  @JsonProperty("security")
  private SecurityType security = null;

  @JsonProperty("heightType")
  private HeightUnitType heightType = HeightUnitType.METERS;

  @JsonProperty("elevations")
  @Valid
  private List<ElevationType> elevations = null;

  public ElevationResponse security(SecurityType security) {
    this.security = security;
    return this;
  }

  /**
   * Get security
   * @return security
  */
  @ApiModelProperty(value = "")

  @Valid

  public SecurityType getSecurity() {
    return security;
  }

  public void setSecurity(SecurityType security) {
    this.security = security;
  }

  public ElevationResponse heightType(HeightUnitType heightType) {
    this.heightType = heightType;
    return this;
  }

  /**
   * Get heightType
   * @return heightType
  */
  @ApiModelProperty(value = "")

  @Valid

  public HeightUnitType getHeightType() {
    return heightType;
  }

  public void setHeightType(HeightUnitType heightType) {
    this.heightType = heightType;
  }

  public ElevationResponse elevations(List<ElevationType> elevations) {
    this.elevations = elevations;
    return this;
  }

  public ElevationResponse addElevationsItem(ElevationType elevationsItem) {
    if (this.elevations == null) {
      this.elevations = new ArrayList<>();
    }
    this.elevations.add(elevationsItem);
    return this;
  }

  /**
   * Get elevations
   * @return elevations
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<ElevationType> getElevations() {
    return elevations;
  }

  public void setElevations(List<ElevationType> elevations) {
    this.elevations = elevations;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ElevationResponse elevationResponse = (ElevationResponse) o;
    return Objects.equals(this.security, elevationResponse.security) &&
        Objects.equals(this.heightType, elevationResponse.heightType) &&
        Objects.equals(this.elevations, elevationResponse.elevations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(security, heightType, elevations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ElevationResponse {\n");
    
    sb.append("    security: ").append(toIndentedString(security)).append("\n");
    sb.append("    heightType: ").append(toIndentedString(heightType)).append("\n");
    sb.append("    elevations: ").append(toIndentedString(elevations)).append("\n");
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

