import java.util.HashMap;

/**
 * Represent a HTTP request from the client to this web server as described in
 * the RFC https://www.ietf.org/rfc/rfc2616.txt
 * 
 */
public class HttpRequest {

	// Request fields
	private HashMap<String, String> headers;
	// HTTP Method: (GET\POST\OPTIONS\HEAD\TRACE)
	private String httpMethod;
	// The URL string
	private String url;
	// HTTP version: (HTTP/1.0, HTTP/1.1)
	private String httpVersion;

	// Constructor
	public HttpRequest(String[] rawRequest) throws Exception {
		this.headers = new HashMap<String, String>();

		// Ignoring empty lines at the beginning of the message that only
		// contains [CRLF] as the RFC advice
		int firstRealContentLine = 0;
		while (rawRequest[firstRealContentLine].equals("/r/n")) {
			firstRealContentLine++;
		}

		// Parse start line
		parseStartLine(rawRequest[firstRealContentLine]);

		// Parse headers
		for (int i = firstRealContentLine + 1; i < rawRequest.length; i++) {
			parseLine(rawRequest[i]);
		}
	}

	/*
	 * Parse the first line of the HTTP request. The request structure is
	 * something like this: GET /somedir/page.html HTTP/1.1
	 */
	private void parseStartLine(String line) throws Exception {
		// TODO: test security for the startline

		// split the first line (without the CRLF at the end) to three parts,
		// The method, the URL and the HTTP version
		String[] values = line.substring(0, line.length() - 2).split(" ");

		// Parse method part (GET\POST\OPTIONS\HEAD\TRACE)
		switch (values[0].toUpperCase()) {
		case "GET":
			this.httpMethod = "GET";
			break;
		case "POST":
			this.httpMethod = "POST";
			break;
		case "OPTIONS":
			this.httpMethod = "OPTIONS";
			break;
		case "HEAD":
			this.httpMethod = "HEAD";
			break;
		case "TRACE":
			this.httpMethod = "TRACE";
		default:
			throw new IllegalArgumentException(
					"Error! Bad HTTP method! The server doesn't support the "
							+ values[0] + " method.");
		}

		// Parse URL
		if (!Security.checkUrl(values[1])) {
			throw new SecurityException(
					"The URL contains a security threat to the server");
		}
		this.url = values[1];

		// Parse HTTP Version
		if (values[2].toUpperCase().equals("HTTP/1.0")) {
			this.httpVersion = "HTTP/1.0";
		} else if (values[2].toUpperCase().equals("HTTP/1.1")) {
			this.httpVersion = "HTTP/1.1";
		} else {
			throw new IllegalArgumentException(
					"Unsupported HTTP version. The server knows how to deal only with HTTP/1.0 and HTTP/1.1");
		}
		
		// Required field validation 
		if (httpMethod == null || url == null || httpVersion == null) {
			throw new SecurityException("The start line of the HTTP request is not in a legal format!");
		}
	}
}
