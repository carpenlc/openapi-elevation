package mil.nga.elevation.openapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets SourceDEMType
 */
public enum SourceDEMType {
  
  DTED2("DTED2"),
  
  DTED1("DTED1"),
  
  DTED0("DTED0"),
  
  SRTM2("SRTM2"),
  
  SRTM1("SRTM1"),
  
  SRTM2F("SRTM2F"),
  
  SRTM1F("SRTM1F"),
  
  BEST("BEST");

  private String value;

  SourceDEMType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static SourceDEMType fromValue(String value) {
    for (SourceDEMType b : SourceDEMType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

