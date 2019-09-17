package mil.nga.elevation_services.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import mil.nga.elevation_services.model.CoordinateTypeDouble;
import mil.nga.elevation_services.model.TerrainDataFileType;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * MinMaxElevationType
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-09-17T12:49:34.296Z[Etc/GMT-0]")

public class MinMaxElevationType   {
  @JsonProperty("elevation")
  private Integer elevation;

  @JsonProperty("absHorizontalAccuracy")
  private Integer absHorizontalAccuracy;

  @JsonProperty("absVerticalAccuracy")
  private Integer absVerticalAccuracy;

  @JsonProperty("relHorizontalAccuracy")
  private Integer relHorizontalAccuracy;

  @JsonProperty("relVerticalAccuracy")
  private Integer relVerticalAccuracy;

  @JsonProperty("source")
  private TerrainDataFileType source = TerrainDataFileType.DTED0;

  @JsonProperty("coordinate")
  private CoordinateTypeDouble coordinate;

  public MinMaxElevationType elevation(Integer elevation) {
    this.elevation = elevation;
    return this;
  }

  /**
   * Get elevation
   * @return elevation
  */
  @ApiModelProperty(value = "")


  public Integer getElevation() {
    return elevation;
  }

  public void setElevation(Integer elevation) {
    this.elevation = elevation;
  }

  public MinMaxElevationType absHorizontalAccuracy(Integer absHorizontalAccuracy) {
    this.absHorizontalAccuracy = absHorizontalAccuracy;
    return this;
  }

  /**
   * Get absHorizontalAccuracy
   * @return absHorizontalAccuracy
  */
  @ApiModelProperty(value = "")


  public Integer getAbsHorizontalAccuracy() {
    return absHorizontalAccuracy;
  }

  public void setAbsHorizontalAccuracy(Integer absHorizontalAccuracy) {
    this.absHorizontalAccuracy = absHorizontalAccuracy;
  }

  public MinMaxElevationType absVerticalAccuracy(Integer absVerticalAccuracy) {
    this.absVerticalAccuracy = absVerticalAccuracy;
    return this;
  }

  /**
   * Get absVerticalAccuracy
   * @return absVerticalAccuracy
  */
  @ApiModelProperty(value = "")


  public Integer getAbsVerticalAccuracy() {
    return absVerticalAccuracy;
  }

  public void setAbsVerticalAccuracy(Integer absVerticalAccuracy) {
    this.absVerticalAccuracy = absVerticalAccuracy;
  }

  public MinMaxElevationType relHorizontalAccuracy(Integer relHorizontalAccuracy) {
    this.relHorizontalAccuracy = relHorizontalAccuracy;
    return this;
  }

  /**
   * Get relHorizontalAccuracy
   * @return relHorizontalAccuracy
  */
  @ApiModelProperty(value = "")


  public Integer getRelHorizontalAccuracy() {
    return relHorizontalAccuracy;
  }

  public void setRelHorizontalAccuracy(Integer relHorizontalAccuracy) {
    this.relHorizontalAccuracy = relHorizontalAccuracy;
  }

  public MinMaxElevationType relVerticalAccuracy(Integer relVerticalAccuracy) {
    this.relVerticalAccuracy = relVerticalAccuracy;
    return this;
  }

  /**
   * Get relVerticalAccuracy
   * @return relVerticalAccuracy
  */
  @ApiModelProperty(value = "")


  public Integer getRelVerticalAccuracy() {
    return relVerticalAccuracy;
  }

  public void setRelVerticalAccuracy(Integer relVerticalAccuracy) {
    this.relVerticalAccuracy = relVerticalAccuracy;
  }

  public MinMaxElevationType source(TerrainDataFileType source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
  */
  @ApiModelProperty(value = "")

  @Valid

  public TerrainDataFileType getSource() {
    return source;
  }

  public void setSource(TerrainDataFileType source) {
    this.source = source;
  }

  public MinMaxElevationType coordinate(CoordinateTypeDouble coordinate) {
    this.coordinate = coordinate;
    return this;
  }

  /**
   * Get coordinate
   * @return coordinate
  */
  @ApiModelProperty(value = "")

  @Valid

  public CoordinateTypeDouble getCoordinate() {
    return coordinate;
  }

  public void setCoordinate(CoordinateTypeDouble coordinate) {
    this.coordinate = coordinate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MinMaxElevationType minMaxElevationType = (MinMaxElevationType) o;
    return Objects.equals(this.elevation, minMaxElevationType.elevation) &&
        Objects.equals(this.absHorizontalAccuracy, minMaxElevationType.absHorizontalAccuracy) &&
        Objects.equals(this.absVerticalAccuracy, minMaxElevationType.absVerticalAccuracy) &&
        Objects.equals(this.relHorizontalAccuracy, minMaxElevationType.relHorizontalAccuracy) &&
        Objects.equals(this.relVerticalAccuracy, minMaxElevationType.relVerticalAccuracy) &&
        Objects.equals(this.source, minMaxElevationType.source) &&
        Objects.equals(this.coordinate, minMaxElevationType.coordinate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elevation, absHorizontalAccuracy, absVerticalAccuracy, relHorizontalAccuracy, relVerticalAccuracy, source, coordinate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MinMaxElevationType {\n");
    
    sb.append("    elevation: ").append(toIndentedString(elevation)).append("\n");
    sb.append("    absHorizontalAccuracy: ").append(toIndentedString(absHorizontalAccuracy)).append("\n");
    sb.append("    absVerticalAccuracy: ").append(toIndentedString(absVerticalAccuracy)).append("\n");
    sb.append("    relHorizontalAccuracy: ").append(toIndentedString(relHorizontalAccuracy)).append("\n");
    sb.append("    relVerticalAccuracy: ").append(toIndentedString(relVerticalAccuracy)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    coordinate: ").append(toIndentedString(coordinate)).append("\n");
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

