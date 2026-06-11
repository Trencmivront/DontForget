package app.excp;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler{
	
    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		
		logger.warning(LocalDateTime.now() + " An exception : " + e.getClass().getName() + " was thrown");
		e.printStackTrace();
	}

}
