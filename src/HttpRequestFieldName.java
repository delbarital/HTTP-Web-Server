import java.util.HashSet;

public class HttpRequestFields {

	// The following HashSet contains all the allowed request fields acording
	// to the HTTP/1.1 RFC
	private static final HashSet<String> requestFields = new HashSet<String>();
	static {
		requestFields.add("Accept".toLowerCase());
		requestFields.add("Accept-Charset".toLowerCase());
		requestFields.add("Accept-Encoding".toLowerCase());
		requestFields.add("Accept-Language".toLowerCase());
		requestFields.add("Accept-Datetime".toLowerCase());
		requestFields.add("Authorization".toLowerCase());
		requestFields.add("Cache-Control".toLowerCase());
		requestFields.add("Connection".toLowerCase());
		requestFields.add("Cookie".toLowerCase());
		requestFields.add("Content-Length".toLowerCase());
		requestFields.add("Content-MD5".toLowerCase());
		requestFields.add("Content-Type".toLowerCase());
		requestFields.add("Date".toLowerCase());
		requestFields.add("Expect".toLowerCase());
		requestFields.add("From".toLowerCase());
		requestFields.add("Host".toLowerCase());
		requestFields.add("If-Match".toLowerCase());
		requestFields.add("If-Modified-Since".toLowerCase());
		requestFields.add("If-None-Match".toLowerCase());
		requestFields.add("If-Range".toLowerCase());
		requestFields.add("If-Unmodified-Since".toLowerCase());
		requestFields.add("Max-Forwards".toLowerCase());
		requestFields.add("Origin".toLowerCase());
		requestFields.add("Pragma".toLowerCase());
		requestFields.add("Proxy-Authorization".toLowerCase());
		requestFields.add("Range".toLowerCase());
		requestFields.add("Referer [sic]".toLowerCase());
		requestFields.add("TE".toLowerCase());
		requestFields.add("User-Agent".toLowerCase());
		requestFields.add("Upgrade".toLowerCase());
		requestFields.add("Via".toLowerCase());
		requestFields.add("Warning".toLowerCase());
	}
	
	// A basic lookup at the 
	public static boolean isFieldLegal(String field) {
		if (requestFields.contains(field.toLowerCase())) {
			return true;
		}
		return false; 
	}

}
