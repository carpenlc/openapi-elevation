package mil.nga.elevation_services.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import mil.nga.elevation_services.model.CoordinateType;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.TerrainDataFileType;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ElevationQuery
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-09-17T12:49:34.296Z[Etc/GMT-0]")

public class ElevationQuery   {
  @JsonProperty("coordinates")
  @Valid
  private List<CoordinateType> coordinates = null;

  @JsonProperty("heightType")
  private HeightUnitType heightType = HeightUnitType.METERS;

  @JsonProperty("source")
  private TerrainDataFileType source = TerrainDataFileType.DTED0;

  public ElevationQuery coordinates(List<CoordinateType> coordinates) {
    this.coordinates = coordinates;
    return this;
  }

  public ElevationQuery addCoordinatesItem(CoordinateType coordinatesItem) {
    if (this.coordinates == null) {
      this.coordinates = new ArrayList<>();
    }
    this.coordinates.add(coordinatesItem);
    return this;
  }

  /**
   * Get coordinates
   * @return coordinates
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<CoordinateType> getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(List<CoordinateType> coordinates) {
    this.coordinates = coordinates;
  }

  public ElevationQuery heightType(HeightUnitType heightType) {
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

  public ElevationQuery source(TerrainDataFileType source) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ElevationQuery elevationQuery = (ElevationQuery) o;
    return Objects.equals(this.coordinates, elevationQuery.coordinates) &&
        Objects.equals(this.heightType, elevationQuery.heightType) &&
        Objects.equals(this.source, elevationQuery.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinates, heightType, source);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ElevationQuery {\n");
    
    sb.append("    coordinates: ").append(toIndentedString(coordinates)).append("\n");
    sb.append("    heightType: ").append(toIndentedString(heightType)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
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

