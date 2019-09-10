package mil.nga.elevation;

public enum ErrorCodes {
	DB_CONNECTION_ERROR(-1001, "Unable to connect to the target data source."),
	INVALID_QUERY(-1005, "Invalid input query.  Unable to process."),
	INVALID_QUERY_NO_LAT(-1006, "Invalid input query.  Latitude not defined."),
	INVALID_QUERY_NO_LON(-1007, "Invalid input query.  Longitude not defined."),
	INVALID_QUERY_NO_COORDINATES(-1008, "Invalid input query.  Array of coordinates not supplied."),
	INVALID_UNITS(-1010, "Invalid height units.  Application supports FEET, METERS."),
	INVALID_SOURCE_TYPE(-1010, "Invalid source DEM specified"),
	INVALID_INPUT_COORDINATES(-1015, "Invalid input coordinates."),
	INVALID_NUMBER_OF_INPUT_COORDINATES(-1020, "Input coordinates must be coordinate pairs (odd number of points)"),
	NO_SOURCE_AVAILABLE(-1030, "Unable to find any DEM sources for the requested coordinate."),
	INTERNAL_EXCEPTION(-1100, "Unexpected internal exception.  See logs for more information.");
	
	// Internal member objects holding the error code and message
	private final int    code;
	private final String message;
	
	/**
	 * Default constructor.
	 * @param code The error code.
	 * @param message The error message.
	 */
	private ErrorCodes(int code, String message) {
		this.code    = code;
		this.message = message;
	}
	
	/**
	 * Getter method for the error code.
	 * @return The error code.
	 */
	public Integer getErrorCode() {
		return code;
	}
	
	/**
	 * Getter method for the error message.
	 * @return The error message.
	 */
	public String getErrorMessage() {
		return message;
	}
	
	/**
	 * Convert to a human-readable String.
	 */
	public String toString() {
		return String.valueOf(code) + ":  " + message;
	}
}
