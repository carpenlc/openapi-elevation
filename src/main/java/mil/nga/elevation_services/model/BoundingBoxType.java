package mil.nga.elevation_services.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BoundingBoxType
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-04-07T08:48:31.266-05:00[America/Chicago]")

public class BoundingBoxType   {
  @JsonProperty("lllat")
  private String lllat;

  @JsonProperty("lllon")
  private String lllon;

  @JsonProperty("urlat")
  private String urlat;

  @JsonProperty("urlon")
  private String urlon;

  public BoundingBoxType lllat(String lllat) {
    this.lllat = lllat;
    return this;
  }

  /**
   * Get lllat
   * @return lllat
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getLllat() {
    return lllat;
  }

  public void setLllat(String lllat) {
    this.lllat = lllat;
  }

  public BoundingBoxType lllon(String lllon) {
    this.lllon = lllon;
    return this;
  }

  /**
   * Get lllon
   * @return lllon
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getLllon() {
    return lllon;
  }

  public void setLllon(String lllon) {
    this.lllon = lllon;
  }

  public BoundingBoxType urlat(String urlat) {
    this.urlat = urlat;
    return this;
  }

  /**
   * Get urlat
   * @return urlat
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getUrlat() {
    return urlat;
  }

  public void setUrlat(String urlat) {
    this.urlat = urlat;
  }

  public BoundingBoxType urlon(String urlon) {
    this.urlon = urlon;
    return this;
  }

  /**
   * Get urlon
   * @return urlon
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getUrlon() {
    return urlon;
  }

  public void setUrlon(String urlon) {
    this.urlon = urlon;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoundingBoxType boundingBoxType = (BoundingBoxType) o;
    return Objects.equals(this.lllat, boundingBoxType.lllat) &&
        Objects.equals(this.lllon, boundingBoxType.lllon) &&
        Objects.equals(this.urlat, boundingBoxType.urlat) &&
        Objects.equals(this.urlon, boundingBoxType.urlon);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lllat, lllon, urlat, urlon);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoundingBoxType {\n");
    
    sb.append("    lllat: ").append(toIndentedString(lllat)).append("\n");
    sb.append("    lllon: ").append(toIndentedString(lllon)).append("\n");
    sb.append("    urlat: ").append(toIndentedString(urlat)).append("\n");
    sb.append("    urlon: ").append(toIndentedString(urlon)).append("\n");
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

