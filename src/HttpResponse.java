/**
 * This class represent a HTTP Response as described in the RFC
 * https://www.ietf.org/rfc/rfc2616.txt
 * 
 */
public class HttpResponse {

	String httpVersion;
	HttpStatusCode httpStatusCode;	

	public HttpResponse(String httpVersion, HttpStatusCode statusCode)
			throws HttpException {
		// set version
		if (httpVersion.toUpperCase() == "HTTP/1.0"
				|| httpVersion.toLowerCase() == "HTTP/1.1") {
			this.httpVersion = httpVersion.toUpperCase();
		} else {
			// unknown or unsupported HTTP version
			throw new HttpException(501);
		}
		
		// set status code
		if (statusCode == null) {
			// No status code was received.
			throw new HttpException(501);
		}
		this.httpStatusCode = statusCode;
	}

	
	// Getters
	
	/**
	 * returns the fist line of the HTTP response, the status line, as described
	 * in the RFC: 
	 * HTTP-Version SP Status-Code SP Reason-Phrase CRLF
	 */
	public String getStatusLine() {
		String crlf = "\r\n";
		return this.httpVersion + " " + this.httpStatusCode.getStatusCode()
				+ " " + this.httpStatusCode.getResponseMessageString() + crlf;
	}
	
	public HttpStatusCode getStatusCode() {
		return this.httpStatusCode;
	}	

}
