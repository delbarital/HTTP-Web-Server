import java.util.Hashtable;

/**
 * Represent HTTP response according to RFC 2616 (HTTP/1.1) Chapter 10 (Status
 * Code Definitions).
 * 
 * @author Tal Delbari
 * 
 */
public class HttpResponse {
	// HTTP responses strings
	private final static String HTTP_RESPONSE_200 = "OK";
	private final static String HTTP_RESPONSE_400 = "Bad Request";
	private final static String HTTP_RESPONSE_404 = "Not Found";
	private final static String HTTP_RESPONSE_500 = "Internal Server Error";
	private final static String HTTP_RESPONSE_501 = "Not Implemented";

	// HTTP responses static Hash Table
	private static final Hashtable RESPONSES;
	static {
		RESPONSES = new Hashtable();
		RESPONSES.put(new Integer(200), HTTP_RESPONSE_200);
		RESPONSES.put(new Integer(400), HTTP_RESPONSE_400);
		RESPONSES.put(new Integer(404), HTTP_RESPONSE_404);
		RESPONSES.put(new Integer(500), HTTP_RESPONSE_500);
		RESPONSES.put(new Integer(501), HTTP_RESPONSE_501);
		// TODO: Add other HTTP responses
	}

	// The response code number and the response explanation string.
	private int responseCode;
	private String responseString = null;

	/**
	 * Check if the responseNum is a known HTTP response code. If it is not,
	 * throw an IllegalArgumentException.
	 * 
	 * @param responseCode
	 * @throws IllegalArgumentException
	 */
	public HttpResponse(int responseCode) throws IllegalArgumentException {
		// Check if the repsponseNumber is legit, if not, throws
		// IllegalArgumentException
		if (!Security.checkResponseCode(responseCode)) {
			throw new IllegalArgumentException("Bad HTTP response number.");
		}

		// If the exact code is known
		if (RESPONSES.containsKey(new Integer(responseCode))) {
			this.responseCode = responseCode;
			this.responseString = (String) RESPONSES.get(new Integer(
					responseCode));
		} else {
			// If the exact code is unknown, set the class code of this code.
			responseCode = getResponseCodeClass(responseCode);
			this.responseCode = responseCode;
			this.responseString = (String) RESPONSES.get(new Integer(responseCode));
		}
	}

	// Getters and Setters
	
	public int getResponseCode() {
		return this.responseCode;
	}
	
	public String getResponseString() {
		return this.responseString;
	}
	
	public String toString() {
		return this.responseCode + " " + this.responseString;
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
		responseCode /= 100;
		responseCode *= 100;
		
		return reponseCode;
	}
}
