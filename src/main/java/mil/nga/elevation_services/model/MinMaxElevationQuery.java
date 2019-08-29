package mil.nga.elevation_services.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import mil.nga.elevation_services.model.BoundingBoxType;
import mil.nga.elevation_services.model.HeightUnitType;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * MinMaxElevationQuery
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-08-29T11:50:00.502Z[Etc/GMT-0]")

public class MinMaxElevationQuery   {
  @JsonProperty("bbox")
  private BoundingBoxType bbox;

  @JsonProperty("heightType")
  private HeightUnitType heightType = HeightUnitType.METERS;

  public MinMaxElevationQuery bbox(BoundingBoxType bbox) {
    this.bbox = bbox;
    return this;
  }

  /**
   * Get bbox
   * @return bbox
  */
  @ApiModelProperty(value = "")

  @Valid

  public BoundingBoxType getBbox() {
    return bbox;
  }

  public void setBbox(BoundingBoxType bbox) {
    this.bbox = bbox;
  }

  public MinMaxElevationQuery heightType(HeightUnitType heightType) {
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
    MinMaxElevationQuery minMaxElevationQuery = (MinMaxElevationQuery) o;
    return Objects.equals(this.bbox, minMaxElevationQuery.bbox) &&
        Objects.equals(this.heightType, minMaxElevationQuery.heightType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bbox, heightType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MinMaxElevationQuery {\n");
    
    sb.append("    bbox: ").append(toIndentedString(bbox)).append("\n");
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

