package main.services.recurring;

import java.time.DayOfWeek;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.RecurringTask;
import main.repos.RecurringTaskRepository;

@Service
public class CreateRecurringTaskService {

	private static final Logger logger = Logger.getLogger(CreateRecurringTaskService.class.getName());

	@Autowired
	private RecurringTaskRepository recurringTaskRepository;

	public boolean execute(Long taskId, List<DayOfWeek> days) {
		logger.info("Executing CreateRecurringTaskService for taskId: " + taskId);
		try {
			for (DayOfWeek day : days) {
				RecurringTask rt = new RecurringTask(taskId, (long) day.getValue());
				recurringTaskRepository.save(rt);
			}
			logger.info("Recurring task created successfully for task: " + taskId);
			return true;
		} catch (Exception e) {
			logger.severe("Error creating recurring task: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
