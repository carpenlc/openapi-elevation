package mil.nga.elevation_services.model;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Output Earth Model Reference:   * EGM96 - Elevation height relative to EGM96 geoid.   * WGS84 - Elevation height relative to WGS84 ellipsoid. 
 */
public enum EarthModelType {
  
  EGM96("EGM96"),
  
  WGS84("WGS84");

  private String value;

  EarthModelType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static EarthModelType fromValue(String value) {
    for (EarthModelType b : EarthModelType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

