package main.services.recurring;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.RecurringTask;
import main.repos.RecurringTaskRepository;

@Service
public class GetRecurringDaysOfTaskService {

	private static final Logger logger = Logger.getLogger(GetRecurringDaysOfTaskService.class.getName());

	@Autowired
	private RecurringTaskRepository recurringTaskRepository;

	public List<DayOfWeek> execute(Long taskId) {
		logger.info("Executing GetRecurringDaysOfTaskService for taskId: " + taskId);
		List<DayOfWeek> days = new ArrayList<>();
		try {
			List<RecurringTask> records = recurringTaskRepository.findByTask_id(taskId);
			for (RecurringTask rt : records) {
				days.add(DayOfWeek.of(rt.week_day_id().intValue()));
			}
		} catch (Exception e) {
			logger.severe("Error retrieving recurring days: " + e.getMessage());
			e.printStackTrace();
		}
		return days;
	}
}
