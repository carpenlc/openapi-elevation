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
 * SecurityType
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-08-19T14:21:12.307-05:00[America/Chicago]")

public class SecurityType   {
  @JsonProperty("classification")
  private String classification;

  @JsonProperty("ownerProducer")
  private String ownerProducer;

  public SecurityType classification(String classification) {
    this.classification = classification;
    return this;
  }

  /**
   * Get classification
   * @return classification
  */
  @ApiModelProperty(value = "")


  public String getClassification() {
    return classification;
  }

  public void setClassification(String classification) {
    this.classification = classification;
  }

  public SecurityType ownerProducer(String ownerProducer) {
    this.ownerProducer = ownerProducer;
    return this;
  }

  /**
   * Get ownerProducer
   * @return ownerProducer
  */
  @ApiModelProperty(value = "")


  public String getOwnerProducer() {
    return ownerProducer;
  }

  public void setOwnerProducer(String ownerProducer) {
    this.ownerProducer = ownerProducer;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SecurityType securityType = (SecurityType) o;
    return Objects.equals(this.classification, securityType.classification) &&
        Objects.equals(this.ownerProducer, securityType.ownerProducer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classification, ownerProducer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SecurityType {\n");
    
    sb.append("    classification: ").append(toIndentedString(classification)).append("\n");
    sb.append("    ownerProducer: ").append(toIndentedString(ownerProducer)).append("\n");
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

