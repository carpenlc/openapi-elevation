package mil.nga.elevation_services.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets TerrainDataFileType
 */
public enum TerrainDataFileType {
  
  DTED2("DTED2"),
  
  DTED1("DTED1"),
  
  DTED0("DTED0"),
  
  SRTM2("SRTM2"),
  
  SRTM1("SRTM1"),
  
  SRTM2F("SRTM2F"),
  
  SRTM1F("SRTM1F"),
  
  BEST("BEST");

  private String value;

  TerrainDataFileType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TerrainDataFileType fromValue(String value) {
    for (TerrainDataFileType b : TerrainDataFileType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

