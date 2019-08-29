package mil.nga.elevation_services.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import mil.nga.elevation_services.model.HeightUnitType;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * MinMaxElevationQueryWKT
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-08-29T11:50:00.502Z[Etc/GMT-0]")

public class MinMaxElevationQueryWKT   {
  @JsonProperty("wkt")
  private String wkt;

  @JsonProperty("heightType")
  private HeightUnitType heightType = HeightUnitType.METERS;

  public MinMaxElevationQueryWKT wkt(String wkt) {
    this.wkt = wkt;
    return this;
  }

  /**
   * Get wkt
   * @return wkt
  */
  @ApiModelProperty(value = "")


  public String getWkt() {
    return wkt;
  }

  public void setWkt(String wkt) {
    this.wkt = wkt;
  }

  public MinMaxElevationQueryWKT heightType(HeightUnitType heightType) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MinMaxElevationQueryWKT minMaxElevationQueryWKT = (MinMaxElevationQueryWKT) o;
    return Objects.equals(this.wkt, minMaxElevationQueryWKT.wkt) &&
        Objects.equals(this.heightType, minMaxElevationQueryWKT.heightType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wkt, heightType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MinMaxElevationQueryWKT {\n");
    
    sb.append("    wkt: ").append(toIndentedString(wkt)).append("\n");
    sb.append("    heightType: ").append(toIndentedString(heightType)).append("\n");
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

