package ch.unibe.ese.team3.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ConfigReader {

	private static Map<String, String> config;
	
	public String configFileName;

	public String getConfigValue(String key) {
		if (config.containsKey(key)) {
			return config.get(key);
		}
		return null;
	}

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
