package mil.nga.elevation_services.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import mil.nga.elevation_services.model.EarthModelType;
import mil.nga.elevation_services.model.HeightUnitType;
import mil.nga.elevation_services.model.TerrainDataFileType;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * MinMaxElevationQueryWKT
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-04-07T08:48:31.266-05:00[America/Chicago]")

public class MinMaxElevationQueryWKT   {
  @JsonProperty("wkt")
  private String wkt;

  @JsonProperty("heightType")
  private HeightUnitType heightType = HeightUnitType.METERS;

  @JsonProperty("earthModelType")
  private EarthModelType earthModelType = EarthModelType.EGM96;

  @JsonProperty("source")
  private TerrainDataFileType source = TerrainDataFileType.DTED0;

  public MinMaxElevationQueryWKT wkt(String wkt) {
    this.wkt = wkt;
    return this;
  }

  /**
   * Get wkt
   * @return wkt
  */
  @ApiModelProperty(value = "")


  public String getWkt() {
    return wkt;
  }

  public void setWkt(String wkt) {
    this.wkt = wkt;
  }

  public MinMaxElevationQueryWKT heightType(HeightUnitType heightType) {
    this.heightType = heightType;
    return this;
  }

  /**
   * Get heightType
   * @return heightType
  */
  @ApiModelProperty(value = "")

  @Valid

  public HeightUnitType getHeightType() {
    return heightType;
  }

  public void setHeightType(HeightUnitType heightType) {
    this.heightType = heightType;
  }

  public MinMaxElevationQueryWKT earthModelType(EarthModelType earthModelType) {
    this.earthModelType = earthModelType;
    return this;
  }

  /**
   * Get earthModelType
   * @return earthModelType
  */
  @ApiModelProperty(value = "")

  @Valid

  public EarthModelType getEarthModelType() {
    return earthModelType;
  }

  public void setEarthModelType(EarthModelType earthModelType) {
    this.earthModelType = earthModelType;
  }

  public MinMaxElevationQueryWKT source(TerrainDataFileType source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
  */
  @ApiModelProperty(value = "")

  @Valid

  public TerrainDataFileType getSource() {
    return source;
  }

  public void setSource(TerrainDataFileType source) {
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
    MinMaxElevationQueryWKT minMaxElevationQueryWKT = (MinMaxElevationQueryWKT) o;
    return Objects.equals(this.wkt, minMaxElevationQueryWKT.wkt) &&
        Objects.equals(this.heightType, minMaxElevationQueryWKT.heightType) &&
        Objects.equals(this.earthModelType, minMaxElevationQueryWKT.earthModelType) &&
        Objects.equals(this.source, minMaxElevationQueryWKT.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wkt, heightType, earthModelType, source);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MinMaxElevationQueryWKT {\n");
    
    sb.append("    wkt: ").append(toIndentedString(wkt)).append("\n");
    sb.append("    heightType: ").append(toIndentedString(heightType)).append("\n");
    sb.append("    earthModelType: ").append(toIndentedString(earthModelType)).append("\n");
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

