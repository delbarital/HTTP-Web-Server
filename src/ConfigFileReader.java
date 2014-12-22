import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Character.Subset;
import java.nio.file.AccessDeniedException;

/**
 * 
 * Reads and parses the config file.
 * 
 * @author Tal Delbari
 * 
 */
public class ConfigFileReader {

	private int port;
	private String root = null;
	private String defaultPage = null;
	private int maxThreads;

	private File configFile = null;
	private FileReader fileReader = null;
	private BufferedReader br = null;

	// Constructor
	public ConfigFileReader(String settingsFilePath)
			throws IllegalArgumentException, AccessDeniedException,
			FileNotFoundException {
		super();
		if (!Security.checkSettingsFilePath(settingsFilePath)) {
			throw new IllegalArgumentException("Error! Bad settings file path!");
		}
		if (!configFile.exists()) {
			throw new FileNotFoundException("Error! Config files not found.");
		}
		if (!configFile.canRead()) {
			throw new AccessDeniedException(
					"Error! Could not access config file.");
		}
		fileReader = new FileReader(configFile);
		if (!Security.scanConfigFile(br)) {
			throw new SecurityException(
					"Error! The config file is corrupted and not following the right config file structure!");
		}
		br = new BufferedReader(fileReader);

		parseFile();
	}

	private void parseFile() throws IOException {
		String line = "";
		String attribute = null;
		String attributeValue = null;
		int equalsSignIndex;
		for (int i = 0; i < 4; i++) {
			line = br.readLine();
			equalsSignIndex = line.indexOf("=");
			// Parses the config line to an attribute and a value. Turn the
			// attribute to lower cases and trim both of the strings to remove
			// white spaces.
			attribute = line.substring(0, equalsSignIndex - 1).toLowerCase()
					.trim();
			// Remember not to turn the value to lower cases. Filenames and
			// paths are case sensitive.
			attributeValue = line.substring(equalsSignIndex + 1).trim();
			
			switch (attribute) {
			case "port":
				this.port = Integer.parseInt(attributeValue);
				break;
			case "root":
				this.root = attributeValue;
				break;
			case "defaultpage":
				this.defaultPage = attributeValue;
				break;
			case "maxthreads":
				this.maxThreads = Integer.parseInt(attributeValue);

			default:
				break;
			}
		}

	}

}