package mil.nga.elevation_services.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import mil.nga.elevation_services.model.EarthModelType;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.MinMaxElevationType;
import mil.nga.elevation_services.model.SecurityType;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * MinMaxElevationResponse
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-04-07T08:48:31.266-05:00[America/Chicago]")

public class MinMaxElevationResponse   {
  @JsonProperty("security")
  private SecurityType security;

  @JsonProperty("heightType")
  private HeightUnitType heightType = HeightUnitType.METERS;

  @JsonProperty("earthModelType")
  private EarthModelType earthModelType = EarthModelType.EGM96;

  @JsonProperty("minElevation")
  private MinMaxElevationType minElevation;

  @JsonProperty("maxElevation")
  private MinMaxElevationType maxElevation;

  public MinMaxElevationResponse security(SecurityType security) {
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

  public MinMaxElevationResponse heightType(HeightUnitType heightType) {
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

  public MinMaxElevationResponse earthModelType(EarthModelType earthModelType) {
    this.earthModelType = earthModelType;
    return this;
  }

  /**
   * Get earthModelType
   * @return earthModelType
  */
  @ApiModelProperty(value = "")

  @Valid

  public EarthModelType getEarthModelType() {
    return earthModelType;
  }

  public void setEarthModelType(EarthModelType earthModelType) {
    this.earthModelType = earthModelType;
  }

  public MinMaxElevationResponse minElevation(MinMaxElevationType minElevation) {
    this.minElevation = minElevation;
    return this;
  }

  /**
   * Get minElevation
   * @return minElevation
  */
  @ApiModelProperty(value = "")

  @Valid

  public MinMaxElevationType getMinElevation() {
    return minElevation;
  }

  public void setMinElevation(MinMaxElevationType minElevation) {
    this.minElevation = minElevation;
  }

  public MinMaxElevationResponse maxElevation(MinMaxElevationType maxElevation) {
    this.maxElevation = maxElevation;
    return this;
  }

  /**
   * Get maxElevation
   * @return maxElevation
  */
  @ApiModelProperty(value = "")

  @Valid

  public MinMaxElevationType getMaxElevation() {
    return maxElevation;
  }

  public void setMaxElevation(MinMaxElevationType maxElevation) {
    this.maxElevation = maxElevation;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MinMaxElevationResponse minMaxElevationResponse = (MinMaxElevationResponse) o;
    return Objects.equals(this.security, minMaxElevationResponse.security) &&
        Objects.equals(this.heightType, minMaxElevationResponse.heightType) &&
        Objects.equals(this.earthModelType, minMaxElevationResponse.earthModelType) &&
        Objects.equals(this.minElevation, minMaxElevationResponse.minElevation) &&
        Objects.equals(this.maxElevation, minMaxElevationResponse.maxElevation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(security, heightType, earthModelType, minElevation, maxElevation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MinMaxElevationResponse {\n");
    
    sb.append("    security: ").append(toIndentedString(security)).append("\n");
    sb.append("    heightType: ").append(toIndentedString(heightType)).append("\n");
    sb.append("    earthModelType: ").append(toIndentedString(earthModelType)).append("\n");
    sb.append("    minElevation: ").append(toIndentedString(minElevation)).append("\n");
    sb.append("    maxElevation: ").append(toIndentedString(maxElevation)).append("\n");
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

