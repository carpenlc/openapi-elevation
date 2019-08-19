package mil.nga.elevation_services.model;

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
public enum HeightUnitType {
  
  FEET("FEET"),
  
  METERS("METERS");

  private String value;

  HeightUnitType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static HeightUnitType fromValue(String value) {
    for (HeightUnitType b : HeightUnitType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

