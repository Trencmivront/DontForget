package main.io.github.trencmivront.dontforget.custom;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.assertj.core.error.ActualIsNotEmpty;
import org.hamcrest.collection.IsEmptyCollection;
import org.mockito.internal.util.Primitives;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SettingsManager {
	
	private static final Logger logger = LoggerFactory.getLogger(SettingsManager.class.getName());
	
	private final ObjectMapper mapper = new ObjectMapper();
	private File settingsFile;
	private JsonNode node;
    
	public ObjectMapper getMapper() {
		return mapper;
	}

	public File getSettingsfile() {
		return settingsFile;
	}
	
	public SettingsManager() {
		try {
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
    
    public <T> T get(String field, T type) {
    	Object object = node.get(field);

    	if(type instanceof Primitives) {
    		
    	}
    	
    	return (T)object;
    }
	
}