package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import app.config.GlobalConfig;
import app.gui.Main;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;

@ComponentScan
public class App {
    // Initialize the Spring context using App as the configuration entry point
    private final static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    static {
        context.register(GlobalConfig.class);
        context.refresh();
    }
    private final static Logger logger = context.getBean(Logger.class);
    public static void main(String[] args) {
        try {

            // Initialize the look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Retrieve the Logger bean configured in GlobalConfig
            Logger logger = context.getBean(Logger.class);
            logger.info("Starting DontForget application...");
            
            try {
                // Load H2 JDBC driver
                Class.forName("org.h2.Driver");
                
                // Connect to H2 database (persistent file-based database stored in src/data)
                try (Connection conn = DriverManager.getConnection("jdbc:h2:./src/data/dontforget", "sa", "")) {
                    logger.info("Database connection established successfully.");

                    // Initialize database tables using crTables.sql
                    Path crTablesPath = Path.of("src/app/db/tables/crTables.sql");
                    if (Files.exists(crTablesPath)) {
                        try (Statement stmt = conn.createStatement()) {
                            stmt.execute("RUNSCRIPT FROM 'src/app/db/tables/crTables.sql'");
                            logger.info("Database tables initialized successfully using crTables.sql.");
                        }
                    } else {
                        logger.severe("crTables.sql file not found at " + crTablesPath.toAbsolutePath());
                    }
                    new Main();
                }
            } catch (ClassNotFoundException e) {
                logger.severe("H2 Driver not found.");
            } catch (SQLException e) {
                logger.severe("Database initialization failed with SQL error.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
