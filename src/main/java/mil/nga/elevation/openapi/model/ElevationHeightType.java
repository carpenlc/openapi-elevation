package mil.nga.elevation.openapi.model;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Output Elevation Units:   * FEET - Output elevation in feet.   * METERS - Output elevation in meters (default) 
 */
public enum ElevationHeightType {
  
  FEET("FEET"),
  
  METERS("METERS");

  private String value;

  ElevationHeightType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ElevationHeightType fromValue(String value) {
    for (ElevationHeightType b : ElevationHeightType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

