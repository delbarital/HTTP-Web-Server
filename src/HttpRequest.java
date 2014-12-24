import java.util.HashMap;

import sun.security.util.Length;

import com.sun.org.apache.regexp.internal.recompile;
import com.sun.swing.internal.plaf.metal.resources.metal;

/**
 * Represent a HTTP request from the client to this web server as described in
 * the RFC https://www.ietf.org/rfc/rfc2616.txt
 * 
 */
public class HttpRequest {

	// Request fields
	private HashMap<String, String> headers;
	// HTTP Method: HTTP/1.1 or HTTP/1.0
	private String httpMethod;

	// Constructor
	public HttpRequest(String[] rawRequest) {
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
	private void parseStartLine(String line) {
		// TODO: test security for the startline

		// split the first line (without the CRLF at the end) to three parts,
		// The method, the URL and the HTTP version
		String[] values = line.substring(0, line.length() - 2).split(" ");
		
		// Parse method part (GET\POST\OPTIONS\HEAD\TRACE)
		switch (values[0].toUpperCase()) {
		case "GET":
			httpMethod = "GET";
			break;
		case "POST":
			httpMethod = "POST";
			break;
		case "OPTIONS":
			httpMethod = "OPTIONS";
			break;
		case "HEAD":
			httpMethod = "HEAD";
			break;
		case "TRACE":
			httpMethod = "TRACE";
		default:
			throw new IllegalArgumentException(
					"Error! Bad HTTP method! The server doesn't support the "
							+ values[0] + " method.");
		}
		
		//Parse URL
		if (!Security.checkUrl(values[1])) {
			throw new SecurityException("The URL contains a security threat to the server"); 
		}

	}
}
