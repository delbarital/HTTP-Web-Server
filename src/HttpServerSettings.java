/**
 * 
 * This class contains the settings for the HTTP server: Port number to listen
 * too. Root directory path for the files. Default page to load when only a
 * directory path is sent to the server.
 * 
 * @author Tal Delbari
 * 
 */
public class HttpServerSettings {
	private int port = -1;
	private String root = null;
	// TODO: add the option for more default pages
	private String defaultPage = null;
	private int maxThreads = -1;

	public HttpServerSettings(int port, String root, String defaultPage,
			int maxThreads) {
		super();

		if (!Security.checkPortNumer(port)) {
			throw new IllegalArgumentException("Error! Bad port number");
		}
		this.port = port;

		if (!Security.checkRootDirectoryPath(root)) {
			throw new IllegalArgumentException("Error! Bad root path");
		}
		this.root = root;

		if (!Security.checkDefaultPage(defaultPage)) {
			throw new IllegalArgumentException("Error! Bad default page");
		}
		this.defaultPage = defaultPage;

		// 0 means it has no limit
		if (maxThreads < 0) {
			throw new IllegalArgumentException(
					"Error! Bad max threads value. It must be a none negative value.");
		}
		this.maxThreads = maxThreads;
	}

	// getters
	public int getPort() {
		return this.port;
	}

	public String getRoot() {
		return this.root;
	}

	public String getDefaultPage() {
		return this.defaultPage;
	}

	public int getMaxThreads() {
		return this.maxThreads;
	}

	/*
	 * Returns a string with the server's settings.
	 */
	public String toString() {
		String sPort = "Port: " + port + "\n";
		String sRoot = "Root: " + root + "\n";
		String sDefaultPage = "Default Page: " + defaultPage + "\n";
		String sMaxThreads = "Max Threads: " + maxThreads + "\n";

		return sPort + sRoot + sDefaultPage + sMaxThreads;

	}

}
