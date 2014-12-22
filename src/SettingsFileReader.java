import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.AccessDeniedException;
/**
 * 
 * Reads the config file 
 * 
 * @author Tal Delbari
 *
 */
public class SettingsFileReader {

	private int port;
	private String root = null;
	private String defaultPage = null;
	private int maxThreads;

	private File configFile = null;
	private FileReader fileReader = null;

	public SettingsFileReader(String settingsFilePath)
			throws IllegalArgumentException, AccessDeniedException, FileNotFoundException {
		if (!Security.checkSettingsFilePath(settingsFilePath)) {
			throw new IllegalArgumentException("Error! Bad settings file path!");
		}
		if (!configFile.exists()) {
			throw new FileNotFoundException("Error! Config files not found.");
		}
		if (!configFile.canRead()) {
			throw new AccessDeniedException("Error! Could not access config file.");
		}
		fileReader = new FileReader(configFile);
		if (!Security.scanConfigFile(fileReader)) {
			throw new SecurityException("Error! The config file is corrupted and not following the right config file structure!");
		}
		
		
	}

}