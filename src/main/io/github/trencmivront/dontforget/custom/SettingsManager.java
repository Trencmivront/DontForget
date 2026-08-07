package main.io.github.trencmivront.dontforget.custom;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SettingsManager {
	
	private static final Logger logger = LoggerFactory.getLogger(SettingsManager.class.getName());
	
	private final ObjectMapper mapper = new ObjectMapper();
	private File settingsFile;
	private JsonNode node;
	private static SettingsManager settingsManager;
    
	public ObjectMapper getMapper() {
		return mapper;
	}

	public File getSettingsfile() {
		return settingsFile;
	}
	
	public static SettingsManager getSettingsManager() {
		return settingsManager;
	}
	
	public SettingsManager() {
		settingsManager = this;
		try {
//			initialize the settings file before starting
			createSettingsFile();
			settingsFile = Path.of(
					System.getenv().getOrDefault("XDG_CONFIG_HOME", System.getProperty("user.home") + "/.config"),
					"DontForget", "settings.json").toFile();
			node = mapper.readTree(settingsFile);
		} catch (Exception e) {
			settingsFile = null;
			node = null;
			logger.warn("Exception: {} thrown from {} at constructor.", e.getClass(), this.getClass());
		}
	}
	
    public void set(String key, Object value) {
        try {
            ObjectNode rootNode = settingsFile.exists() ?
            		(ObjectNode) mapper.readTree(settingsFile) : 
            			mapper.createObjectNode();
            // put object and let json cast it
            rootNode.putPOJO(key, value);
            mapper.writerWithDefaultPrettyPrinter().writeValue(settingsFile, rootNode);
            logger.info("Saved {} => {}", key, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Object get(String field) {
    	return node.get(field);
    }
    
    public void createSettingsFile() {
		if(!settingsFile.exists()) {
			settingsFile.getParentFile().mkdirs();
			try {
				mapper.writerWithDefaultPrettyPrinter().writeValue(settingsFile, mapper.createObjectNode());
			} catch (IOException e) {
				logger.warn("Exception: {} thrown at {} class {} ", e.getClass(), "createSettingsFile", this.getClass());
			}
		}
    }
	
}