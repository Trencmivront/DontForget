package main.io.github.trencmivront.dontforget.custom;

import java.sql.SQLException;
import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

/// Annotated with @Component . So when @SpringBootApplication runs, this file will be initialized and <b>run()</b> function will be executed.
/// It executes a <i>data.sql</i> file located in resources/db/data.sql. <br>
/// Ensuring the database will be ready before every other action. 
@Component
public class DatabaseInitializer implements CommandLineRunner{

	@Autowired
	private DataSource dataSource;
	private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class.getName());
	private SettingsManager settingsManager = SettingsManager.getSettingsManager();
	
	@Override
	public void run(String... args) throws Exception {
		final boolean defaultValue = false;
		Object isInit = Objects.requireNonNullElse(settingsManager.get("databaseInitialized"), defaultValue);
//		
		if(isInit instanceof Boolean isInitBoolean && isInitBoolean) {
			logger.info("Database is already initialized, skipping...");
				return;
		}
		
		settingsManager.set("databaseInitialized", defaultValue);

		Resource script = new ClassPathResource("db/data.sql");
		try {
			ScriptUtils.executeSqlScript(dataSource.getConnection(), script);
		}catch (SQLException _) {
			logger.error("{} :Could not get connection.", this.getClass());
		}catch(ScriptException e) {
			logger.error("{} :Could not run the sql script. Consider data conflicts or table not exists. message: {}", this.getClass(), e.getMessage());
		}
	}
}
