import java.util.Hashtable;

/**
 * Represent HTTP response code according to RFC 2616 (HTTP/1.1) Chapter 10 (Status
 * Code Definitions).
 * 
 */
public class HttpStatusCode {
	// HTTP responses strings
	private final static String HTTP_RESPONSE_200 = "OK";
	private final static String HTTP_RESPONSE_400 = "Bad Request";
	private final static String HTTP_RESPONSE_404 = "Not Found";
	private final static String HTTP_RESPONSE_500 = "Internal Server Error";
	private final static String HTTP_RESPONSE_501 = "Not Implemented";

	// HTTP responses static Hash Table
	private static final Hashtable<Integer, String> RESPONSES;
	static {
		RESPONSES = new Hashtable<Integer, String>();
		RESPONSES.put(new Integer(200), HTTP_RESPONSE_200);
		RESPONSES.put(new Integer(400), HTTP_RESPONSE_400);
		RESPONSES.put(new Integer(404), HTTP_RESPONSE_404);
		RESPONSES.put(new Integer(500), HTTP_RESPONSE_500);
		RESPONSES.put(new Integer(501), HTTP_RESPONSE_501);
		// TODO: Add other HTTP responses
	}

	// The response code number and the response explanation string.
	private int statusCode;
	private String responseMessageString = null;

	/**
	 * Check if the responseNum is a known HTTP response code. If it is not,
	 * throw an IllegalArgumentException.
	 * 
	 * @param statusCode
	 * @throws IllegalArgumentException
	 */
	public HttpStatusCode(int statusCode) throws IllegalArgumentException {
		// Check if the repsponseNumber is legal, if not, throws
		// IllegalArgumentException
		if (!Security.checkResponseCode(statusCode)) {
			throw new IllegalArgumentException("Bad HTTP response number.");
		}

		// If the exact code is known
		if (RESPONSES.containsKey(new Integer(statusCode))) {
			this.statusCode = statusCode;
			this.responseMessageString = (String) RESPONSES.get(new Integer(
					statusCode));
		} else {
			// If the exact code is unknown, set the class code of this code.
			statusCode = getResponseCodeClass(statusCode);
			this.statusCode = statusCode;
			this.responseMessageString = (String) RESPONSES.get(new Integer(statusCode));
		}
	}

	// Getters and Setters
	
	public int getStatusCode() {
		return this.statusCode;
	}
	
	public String getResponseMessageString() {
		return this.responseMessageString;
	}
	
	public String toString() {
		return this.statusCode + " " + this.responseMessageString;
	}
	
	// Private functions
	
	/*
	 * Returns the response code class of the response. If the response number
	 * is XYZ the function returns X00.
	 */
	private int getResponseCodeClass(int reponseCode)
			throws IllegalArgumentException {
		
		// Use a rounding trick. But first checks if it is OK to use.
		if (Security.checkResponseCode(reponseCode)) {
			throw new IllegalArgumentException();
		}
		statusCode /= 100;
		statusCode *= 100;
		
		return reponseCode;
	}
}
