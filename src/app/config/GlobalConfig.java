package app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.BeanProperty;
import java.beans.JavaBean;
import java.util.logging.Logger;

@Configuration
public class GlobalConfig{
	
    @Bean
    Logger logger() {
    	return Logger.getGlobal();
    }
}
