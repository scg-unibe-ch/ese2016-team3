package ch.unibe.ese.team3.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ConfigReader {

	private static Map<String, String> config;

	private static ConfigReader instance;

	public static ConfigReader getInstance() {
		if (instance == null) {
			instance = new ConfigReader();
		}
		return instance;
	}

	public String getConfigValue(String key) {
		if (config.containsKey(key)) {
			return config.get(key);
		}
		return null;
	}

	private ConfigReader() {
		config = new HashMap<String, String>();
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		for (Object key : bundle.keySet()) {
			String keyString = (String) key;
			config.put(keyString, bundle.getString(keyString));
		}
	}

}
