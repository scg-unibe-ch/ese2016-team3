package ch.unibe.ese.team3.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Reads values from a properties file and makes them
 * available in memory. This class is registered as a bean and
 * instantiated by spring automatically.
 *
 */
public class ConfigReader {

	private static Map<String, String> config;
	
	public String configFileName;

	/**
	 * Return the config value with a specific key
	 * @param key the key to retrieve the value for
	 * @return the value for the key or null if the key doesn't exist
	 */
	public String getConfigValue(String key) {
		if (config.containsKey(key)) {
			return config.get(key);
		}
		return null;
	}

	/**
	 * Create a config reader which reads from the specified config file.
	 * The config file has to be stored under src/main/resources under
	 * the name <<configFileName>>.properties.
	 * 
	 * @param configFileName
	 */
	public ConfigReader(String configFileName) {
		this.configFileName = configFileName;
		config = new HashMap<String, String>();
		ResourceBundle bundle = ResourceBundle.getBundle(this.configFileName);
		for (Object key : bundle.keySet()) {
			String keyString = (String) key;
			config.put(keyString, bundle.getString(keyString));
		}
	}

}
