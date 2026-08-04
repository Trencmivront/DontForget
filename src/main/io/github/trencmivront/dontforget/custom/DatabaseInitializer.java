package main.io.github.trencmivront.dontforget.custom;



import java.io.File;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.App;

// This file runs right when SpringBootApplication runs

@Component
public class DatabaseInitializer implements CommandLineRunner{

	@Autowired
	private DataSource dataSource;
	private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class.getName());
	private final static File settingsFile = App.getSettingsfile();
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public void run(String... args) throws Exception {
		
		if(!settingsFile.exists()) {
			settingsFile.getParentFile().mkdirs();
			mapper.writerWithDefaultPrettyPrinter().writeValue(settingsFile, mapper.createObjectNode());
		}
		
		if(isDatabaseInitialized(App.getMapper().readTree(App.getSettingsfile()))) {
			return;
		}
		Resource script = new ClassPathResource("db/data.sql");
		ScriptUtils.executeSqlScript(dataSource.getConnection(), script);
	}
	
    private static boolean isDatabaseInitialized(JsonNode node) {
    	if (!node.path("databaseInitialized").asBoolean(false)) {
    	    initializeDatabase();
    	    return false;
    	}
		logger.info("Database is already initialized. Skipping...");
		return true;
    }
    
    private static void initializeDatabase(){
    	try {
			App.setSettingValue("databaseInitialized", true);
			
			logger.info("Database initialized.");
				return;
		} catch(Exception e) {
			if(e instanceof SQLException) {
				logger.warn("Database initialization fail. Check if database is already initialized or connection is made.");
			}
			else {
				e.printStackTrace();
				logger.warn("Unexpected error while initializing the database: {}", e.getClass());
			}
		}
    }

}
