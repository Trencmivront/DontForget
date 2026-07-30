package main.java.services.recurring;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.RecurringTask;
import main.java.inter.Query;
import main.java.repos.RecurringTaskRepository;

@Service
public class GetRecurringDaysOfTaskService implements Query<Long, List<DayOfWeek>>{

	private static final Logger logger = LoggerFactory.getLogger(GetRecurringDaysOfTaskService.class.getName());

	@Autowired
	private RecurringTaskRepository recurringTaskRepository;

	public GetRecurringDaysOfTaskService(RecurringTaskRepository recurringTaskRepository) {
		this.recurringTaskRepository = recurringTaskRepository;
	}

	public ResponseEntity<List<DayOfWeek>> execute(Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);
		List<DayOfWeek> days = new ArrayList<>();
		try {
			List<RecurringTask> records = recurringTaskRepository.findBytaskId(taskId);
			records.forEach(rt -> days.add(DayOfWeek.of(rt.getWeekDayId().intValue())));
			return ResponseEntity.ok(days);
		} catch (Exception e) {
			logger.error("Error retrieving recurring days: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(days);
		}
	}
}
