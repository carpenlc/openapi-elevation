package mil.nga.elevation.exceptions;

/**
 * Exception used to capture known error conditions to propogate back to 
 * the REST controller in order to provide the caller with feedback on error
 * conditions.
 * 
 * @author L. Craig Carpenter
 */
public class ApplicationException extends Exception {

	/**
	 * Eclipse-generated serialVersionUID
	 */
	private static final long serialVersionUID = 2086720850694481770L;

	private final Integer errorCode;
	private final String  errorMessage;
	
	/**
	 * Default constructor enforcing the builder creation pattern.
	 * @param builder Object containing default values for the private final
	 * internal parameters.
	 */
	protected ApplicationException(ApplicationExceptionBuilder builder) {
		super(builder.errorMessage);
		errorCode    = builder.errorCode;
		errorMessage = builder.errorMessage;
	}
	
	/**
	 * Getter method for the code associated with the error condition.
	 * @return The error code.
	 */
	public Integer getErrorCode() {
		return errorCode;
	}
	
	/**
	 * Getter method for the message associated with the error condition.
	 * @return The error message.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	/**
	 * Convert to human-readable String.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ApplicationException: code [ ");
		sb.append(getErrorCode());
		sb.append(" ], message [ ");
		sb.append(getErrorMessage());
		sb.append(" ].");
		return sb.toString();
	}
	
    /**
     * Static inner class implementing the builder creation pattern for 
     * objects of type <code>ApplicationException</code>.
     * 
     * @author L. Craig Carpenter
     */
	public static class ApplicationExceptionBuilder {
		
		private Integer errorCode;
		private String  errorMessage;
		
		public ApplicationExceptionBuilder errorCode(Integer value) {
			errorCode = value;
			return this;
		}
		
		public ApplicationExceptionBuilder errorMessage(String value) {
			errorMessage = value;
			return this;
		}
		
		/**
		 * Method used to actually construct the exception object.
		 * @return A constructed, validated <code>ApplicationException</code>
		 * exception.
		 */
		public ApplicationException build() {
			ApplicationException object = new ApplicationException(this);
			validate(object);
			return object;
		}
		
		/**
		 * Validate the candidate exception object.
		 * 
		 * @param exc Candidate <code>ApplicationException</code> object.
		 * @throws IllegalStateException Thrown if there are issues during 
		 * the validation algorithm.
		 */
		public void validate(ApplicationException exc) {
			if (exc.getErrorCode() == null) {
				throw new IllegalStateException("errorCode field is null.");
			}
			if ((exc.getErrorMessage() == null) || (exc.getErrorMessage().isEmpty())) {
				throw new IllegalStateException("errorMessage field is null or empty.");
			}
		}
	}
	
}
