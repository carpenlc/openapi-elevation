package mil.nga.elevation.openapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import mil.nga.elevation.openapi.model.ElevationHeightType;
import mil.nga.elevation.openapi.model.SourceDEMType;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ElevationQuery
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-08-16T14:03:58.428Z[Etc/GMT-0]")

public class ElevationQuery   {
  @JsonProperty("heightType")
  private ElevationHeightType heightType = ElevationHeightType.METERS;

  @JsonProperty("source")
  private SourceDEMType source = SourceDEMType.DTED0;

  public ElevationQuery heightType(ElevationHeightType heightType) {
    this.heightType = heightType;
    return this;
  }

  /**
   * Get heightType
   * @return heightType
  */
  @ApiModelProperty(value = "")

  @Valid

  public ElevationHeightType getHeightType() {
    return heightType;
  }

  public void setHeightType(ElevationHeightType heightType) {
    this.heightType = heightType;
  }

  public ElevationQuery source(SourceDEMType source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
  */
  @ApiModelProperty(value = "")

  @Valid

  public SourceDEMType getSource() {
    return source;
  }

  public void setSource(SourceDEMType source) {
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
    return Objects.equals(this.heightType, elevationQuery.heightType) &&
        Objects.equals(this.source, elevationQuery.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(heightType, source);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ElevationQuery {\n");
    
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

