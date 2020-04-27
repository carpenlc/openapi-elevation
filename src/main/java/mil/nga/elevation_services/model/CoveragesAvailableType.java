package mil.nga.elevation_services.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import mil.nga.elevation_services.model.CoverageAvailableType;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CoveragesAvailableType
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-04-07T08:48:31.266-05:00[America/Chicago]")

public class CoveragesAvailableType   {
  @JsonProperty("coveragesAvailable")
  @Valid
  private List<CoverageAvailableType> coveragesAvailable = null;

  public CoveragesAvailableType coveragesAvailable(List<CoverageAvailableType> coveragesAvailable) {
    this.coveragesAvailable = coveragesAvailable;
    return this;
  }

  public CoveragesAvailableType addCoveragesAvailableItem(CoverageAvailableType coveragesAvailableItem) {
    if (this.coveragesAvailable == null) {
      this.coveragesAvailable = new ArrayList<>();
    }
    this.coveragesAvailable.add(coveragesAvailableItem);
    return this;
  }

  /**
   * Get coveragesAvailable
   * @return coveragesAvailable
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<CoverageAvailableType> getCoveragesAvailable() {
    return coveragesAvailable;
  }

  public void setCoveragesAvailable(List<CoverageAvailableType> coveragesAvailable) {
    this.coveragesAvailable = coveragesAvailable;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CoveragesAvailableType coveragesAvailableType = (CoveragesAvailableType) o;
    return Objects.equals(this.coveragesAvailable, coveragesAvailableType.coveragesAvailable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coveragesAvailable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CoveragesAvailableType {\n");
    
    sb.append("    coveragesAvailable: ").append(toIndentedString(coveragesAvailable)).append("\n");
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

