import java.io.BufferedReader;

/**
 * 
 * This security class is used to check if some parameter values are aligned
 * with the HTTP.
 * 
 * @author Tal Delbari
 * 
 */
public class Security {

	/**
	 * Check if the response code number is according to the RFC, as described
	 * here http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html . The
	 * function receives a responseNumber and check if it is legal. If it is,
	 * return true, if not, return false.
	 * 
	 * @param reponseNumber
	 */
	public static boolean checkResponseCode(int reponseNumber) {
		// Note that HTTP/1.0 does not allow 1XX response codes, and as it is
		// not mandatory we not allow it.
		if (reponseNumber < 200) {
			return false;
		}
		// The highest response code number which is described at the RFC is
		// 599
		if (reponseNumber > 599) {
			return false;
		}
		return true;
	}

	/**
	 * Check the given port number
	 * 
	 * @param port
	 * @return true if the port is in the legal range and false otherwise.
	 */
	public static boolean checkPortNumer(int port) {
		if (port < 0 || port > 65536) {
			return false;
		}
		return true;
	}

	public static boolean checkRootDirectoryPath(String root) {
		// TODO implement a security check for the root path.
		return true;
	}

	public static boolean checkDefaultPage(String defaultPage) {
		// If the default page is void or null return false.
		if (defaultPage.equals("") || defaultPage == null) {
			return false;
		}

		// if the filename contains two dots (".") return false.
		if (defaultPage.indexOf(".") != defaultPage.lastIndexOf(".")) {
			return false;
		}

		// search for illegal chars in the filename.
		char currentChar;
		for (int i = 0, length = defaultPage.length(); i < length; i++) {
			currentChar = defaultPage.charAt(i);
			if (currentChar > 122) {
				return false;
			}
			if (currentChar < 32) {
				return false;
			}
			if (currentChar > 32 && currentChar < 46) {
				return false;
			}
			if (currentChar == 47) {
				return false;
			}
			if (currentChar > 57 && currentChar < 65) {
				return false;
			}

			// TODO: Change those checks to positive checks. It's more likely
			// that the chars will be at the legal range
		}

		// Check if the file extension is longer than it should be
		int fileExtensionIndex = defaultPage.indexOf(".");
		String fileExtension = defaultPage.substring(fileExtensionIndex);
		if (fileExtension.length() > 4) {
			return false;
		}
		// Check if the file extension length is zero, meaning the filename ends
		// with a dot.
		if (fileExtension.length() == 0) {
			return false;
		}

		return true;
	}

	public static boolean checkSettingsFilePath(String settingsFile) {
		// TODO: Implement this check
		return true;
	}

	public static boolean scanConfigFile(BufferedReader br) {
		// TODO: Implement this check
		return true;
	}

	public static boolean checkMaxThreads(int maxThreads, int threadsLimit) {
		if (maxThreads > threadsLimit || maxThreads < 1) {
			return false;
		}
		return true;
	}

	public static boolean checkUrl(String url) {

		// Block attempts to surf outside of the root directory
		/*
		 * TODO: This check can be done better. If we'll have time we should let
		 * the URL contain ".." and return false only if it's really out of the
		 * root directory. Also, it would be good to check for other encodes of
		 * the double dots.
		 */

		if (url.contains("..")) {
			return false;
		}

		// We add two spaces to the end of the URL because the
		// checkCRLFInjection ignore the last two characters. As URL should not
		// contain CRLF at all, we should add a padding of two spaces.
		if (!checkCRLFInjection(url + "  ")) {
			return false;
		}

		return true;
	}

	/*
	 * This function returns false if there is a risk for an CRLF injection
	 * attack in the given string. For more information visit the link
	 * http://www.acunetix.com/websitesecurity/crlf-injection/
	 */
	public static boolean checkCRLFInjection(String str) {
		String CRLF = "\r\n";
		// If the given string contains CRLF sequence, and not at its end,
		// return false.
		if (str.substring(0, str.length() - 3).equals(CRLF)) {
			return false;
		}
		return true;
	}
}
