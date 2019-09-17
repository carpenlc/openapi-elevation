package mil.nga.elevation_services.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import mil.nga.elevation_services.model.CoordinateType;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CoordinateTypeArray
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-09-17T12:49:34.296Z[Etc/GMT-0]")

public class CoordinateTypeArray   {
  @JsonProperty("coordinates")
  @Valid
  private List<CoordinateType> coordinates = null;

  public CoordinateTypeArray coordinates(List<CoordinateType> coordinates) {
    this.coordinates = coordinates;
    return this;
  }

  public CoordinateTypeArray addCoordinatesItem(CoordinateType coordinatesItem) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CoordinateTypeArray coordinateTypeArray = (CoordinateTypeArray) o;
    return Objects.equals(this.coordinates, coordinateTypeArray.coordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CoordinateTypeArray {\n");
    
    sb.append("    coordinates: ").append(toIndentedString(coordinates)).append("\n");
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

