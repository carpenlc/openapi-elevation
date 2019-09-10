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
 * CoverageAvailableType
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-09-10T14:44:29.236Z[Etc/GMT-0]")

public class CoverageAvailableType   {
  @JsonProperty("coordinate")
  private CoordinateType coordinate;

  @JsonProperty("coverages")
  @Valid
  private List<String> coverages = null;

  public CoverageAvailableType coordinate(CoordinateType coordinate) {
    this.coordinate = coordinate;
    return this;
  }

  /**
   * Get coordinate
   * @return coordinate
  */
  @ApiModelProperty(value = "")

  @Valid

  public CoordinateType getCoordinate() {
    return coordinate;
  }

  public void setCoordinate(CoordinateType coordinate) {
    this.coordinate = coordinate;
  }

  public CoverageAvailableType coverages(List<String> coverages) {
    this.coverages = coverages;
    return this;
  }

  public CoverageAvailableType addCoveragesItem(String coveragesItem) {
    if (this.coverages == null) {
      this.coverages = new ArrayList<>();
    }
    this.coverages.add(coveragesItem);
    return this;
  }

  /**
   * Get coverages
   * @return coverages
  */
  @ApiModelProperty(value = "")


  public List<String> getCoverages() {
    return coverages;
  }

  public void setCoverages(List<String> coverages) {
    this.coverages = coverages;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CoverageAvailableType coverageAvailableType = (CoverageAvailableType) o;
    return Objects.equals(this.coordinate, coverageAvailableType.coordinate) &&
        Objects.equals(this.coverages, coverageAvailableType.coverages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinate, coverages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CoverageAvailableType {\n");
    
    sb.append("    coordinate: ").append(toIndentedString(coordinate)).append("\n");
    sb.append("    coverages: ").append(toIndentedString(coverages)).append("\n");
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

