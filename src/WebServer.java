import java.io.*;
import java.net.*;
import java.util.*;

public final class WebServer {
	private static int numberOfThreads = 0;

	public WebServer(int port, int maxThreads) { // Add threads counter
		// TEMPORARY
		this.numberOfThreads = 0;

		// Establish the listen socket.
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(port);
		} catch (IOException e2) {
			System.out.println("Could not create server socket on port: "
					+ port);
		}
		System.out.println("listening to: " + port);

		// Process HTTP service requests in an infinite loop.
		while (true) {

			// Listen for a TCP connection request.
			Socket connection = null;
			try {
				connection = socket.accept();
			} catch (Exception ex) {
				System.out.println("Encoutered error on accapting connection");
				System.exit(1);
			}

			// Construct an object to process the HTTP request message.
			HttpRequest request = null;
			try {
				request = new HttpRequest(connection);
			} catch (Exception e) {
				System.out
						.println("Encoutered error on creating new Http request");
			}

			if (numberOfThreads == maxThreads) {
				System.out.println("Server is Busy");
			} else {
				// Create a new thread to process the request.
				Thread thread = new Thread(request);

				// Start the thread.
				thread.start();
				this.numberOfThreads++;
				System.out.println(this.numberOfThreads);
			}

		}
	}

	// TEMPORARY, to be used by HTTPRESPONSE
	public static void closedThread() {
		numberOfThreads--;
	}

	public static void main(String argv[]) throws Exception {
		// read config file
		ConfigFileReader currentConfigs = new ConfigFileReader("C:/config.ini");
		// get server settings
		HttpServerSettings currentSetting = currentConfigs
				.getHttpServerSettings();
		// creating web server
		new WebServer(currentSetting.getPort(), currentSetting.getMaxThreads());
	}
}
