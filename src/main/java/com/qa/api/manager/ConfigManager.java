package com.qa.api.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
	
	//read properties from config.properties file
	private static Properties prop = new Properties();
	
	static {
		
		//mvn clean install -Denv=qa/dev/stage/uat/prod
		
		String envName = System.getProperty("env", "prod");
		System.out.println("ENVIRONMENT CHOSEN ==>"+envName);
		String fileName = "config-"+envName+".properties";
		
		InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(fileName);
		
		if(input != null) {
			try {
				prop.load(input);
				System.out.println("Config Properties ==> "+prop);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String get(String key) {
		return prop.getProperty(key).trim();
	}
	
	public static void set(String key, String value) {
		prop.setProperty(key, value);
	}
}
