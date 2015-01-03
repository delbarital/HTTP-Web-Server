import java.util.HashMap;

import sun.security.ec.ECDSASignature.Raw;

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
	// specify if the requested content is an image
	private boolean isImage = false;
	// contentLength based on the content-length header
	private int contentLength = -1;
	// referer header
	private String referer;
	// content of message body
	private HashMap<String, String> bodyParam;

	// Constructor
	public HttpRequest(String[] rawRequest) throws HttpException {
		this.headers = new HashMap<String, String>();

		// Security check. If the request has above 100 lines, it's probably an
		// attack.
		if (rawRequest.length > 100) {
			// 431 Request Header Fields Too Large (RFC 6585)
			throw new HttpException(431);
		}

		// Ignoring empty lines at the beginning of the message that only
		// contains [CRLF] as the RFC advice
		int firstRealContentLine = 0;
		while (rawRequest[firstRealContentLine].equals("/r/n")) {
			firstRealContentLine++;
		}

		// Parse start line
		parseStartLine(rawRequest[firstRealContentLine]);

		// Parse headers
		int bodyLineNumber = -1;
		for (int i = firstRealContentLine + 1; i < rawRequest.length; i++) {
			// check if the message body started
			if (rawRequest[i] == "\r\n") {
				break;
				bodyLineNumber = i++;
			}
			parseHeaderLine(rawRequest[i]);
		}

		// Is the requested file an image?
		String[] knownImageExtensions = { "jpg", "jpeg", "bmp", "png", "gif" };
		String requestedFileExtension = url.substring(url.lastIndexOf("." + 1),
				url.length() - 1).toLowerCase();
		for (int i = 0; i < knownImageExtensions.length; i++) {
			if (requestedFileExtension.equals(knownImageExtensions[i])) {
				this.isImage = true;
				break;
			}
		}

		// parse the Content-Length value
		if (headers.containsKey("content-length")) {
			this.contentLength = Integer.getInteger(
					headers.get("content-length")).intValue();

		}

		// parse the referer value
		if (headers.containsKey("Referer")) {
			this.referer = headers.get("refere");
		}

		// parse body if there is one
		if (bodyLineNumber != -1) {
			for (int i = bodyLineNumber; i < rawRequest.length; i++) {
				parseBody(rawRequest[i]);
			}
		}
	}

	private void parseBody(String messageBodyLine) {
		String[] params = messageBodyLine.replaceAll("(\\r|\\n)", "")
				.split("&");
		String parameter[];
		for (int i = 0; i < params.length; i++) {
			parameter = params[i].split("=");
			if (parameter.length != 2) {
				throw new HttpException(404); // URL forbidden. Status 404 is
												// sent in order to mask the
												// reason.
			}
			bodyParam.put(parameter[0].toLowerCase(),
					parameter[1].toLowerCase());
		}

	}

	/**
	 * Parse the headers to header field name and values
	 * 
	 * @param string
	 * @throws HttpException
	 */
	private void parseHeaderLine(String header) throws HttpException {

		String[] parsedLine = header.replaceAll("(\\r|\\n)", "").split(":");
		if (parsedLine.length > 2) {
			throw new HttpException(404); // URL forbidden. Status 404 is sent
											// in order to mask the reason.
		}
		// Remove padding spaces around the field and the value
		parsedLine[0] = parsedLine[0].trim();
		parsedLine[1] = parsedLine[1].trim();
		if (!HttpRequestFields.isFieldLegal(parsedLine[0])) {
			throw new HttpException(400);
		}
		headers.put(parsedLine[0].toLowerCase(), parsedLine[1]);

	}

	/*
	 * Parse the first line of the HTTP request. The request structure is
	 * something like this: GET /somedir/page.html HTTP/1.1
	 */
	private void parseStartLine(String line) throws HttpException {
		// TODO: test security for the startline

		// split the first line (without the CRLF at the end) to three parts,
		// The method, the URL and the HTTP version
		String[] values = line.replaceAll("(\\r|\\n)", "").split(" ");

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
			throw new HttpException(405); // Method Not Allowed
			// TODO: we should pass the allowed methods
		}

		// Parse URL
		if (!Security.checkUrl(values[1])) {
			throw new HttpException(404); // URL forbidden. Status 404 is sent
											// in order to mask the reason.
		}
		this.url = values[1];

		// Parse HTTP Version
		if (values[2].toUpperCase().equals("HTTP/1.0")) {
			this.httpVersion = "HTTP/1.0";
		} else if (values[2].toUpperCase().equals("HTTP/1.1")) {
			this.httpVersion = "HTTP/1.1";
		} else {
			throw new HttpException(505); // HTTP Version Not Supported
		}

		// Required field validation
		if (httpMethod == null || url == null || httpVersion == null) {
			throw new HttpException(400); // Bad Request
		}
	}

	// getters
	public String getUrl() {
		return this.url;
	}

	/*
	 * Returns this http method as a string (in upper case)
	 */
	public String getHttpMethod() {
		return this.httpMethod.toUpperCase();
	}

	public String getHttpVersion() {
		return this.httpVersion;
	}

	/*
	 * Return the content length based on the request. The int value -1 will be
	 * returned if there was no Content-Length filed in the request.
	 */
	public int getContentLength() {
		return this.contentLength;
	}

	public String getReferer() {
		return this.referer;
	}
	
	public HashMap<String, String> getAllBodyParams() {
		return this.bodyParam;
	}

	
	public String getParameterValue(String param) {
		if (param == "" || param.isEmpty() || param == null) {
			return "";
		} else if (bodyParam.containsKey(param.toLowerCase())) {
			return bodyParam.get(param);
		} else {
			return null;
		}
		
	}

	/*
	 * Return true if the requested content is an image and false otherwise
	 */
	public boolean isImage() {
		return this.isImage;
	}

}
