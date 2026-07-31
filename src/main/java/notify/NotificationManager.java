package main.java.notify;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.custom.SpringContext;
import main.java.controllers.RecurringTaskController;
import main.java.controllers.ReminderController;
import main.java.controllers.TaskController;
import org.springframework.http.ResponseEntity;

import main.java.dto.ReminderDTO;
import main.java.dto.TaskDTO;

public class NotificationManager {

	private static final Logger logger = LoggerFactory.getLogger(NotificationManager.class.getName());
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final ConcurrentHashMap<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

	private final ReminderController reminderController = SpringContext.getBean(ReminderController.class);
	private final TaskController taskController = SpringContext.getBean(TaskController.class);
	private final RecurringTaskController recurringTaskController = SpringContext.getBean(RecurringTaskController.class);

	public void initialize() {
		logger.info("Initializing NotificationManager and scheduling existing reminders...");
		List<ReminderDTO> reminders = null;
		try {
			ResponseEntity<List<ReminderDTO>> response = reminderController.getReminders();
			reminders = response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (reminders != null) {
			reminders.forEach(this::scheduleReminder);
		}
        logger.info("Notification manager is initialized.");
	}

	public void scheduleReminder(ReminderDTO reminder) {
		logger.info("Executing {} for reminder: {}", this.getClass(), reminder);
		if (reminder == null) {
			logger.warn("Attempted to schedule a null reminder.");
			return;
		}
		
		// If already scheduled, cancel the existing one first
		cancelReminder(reminder.getTaskId());

		if (reminder.getRemindAt() == null) {
			logger.warn("Reminder time is null for task ID {}", reminder.getTaskId());
			return;
		}
		
		setNewDateTimeForReminder(reminder);
		
		long delay = Timestamp.valueOf(reminder.getRemindAt()).getTime() - System.currentTimeMillis();
		if (delay <= 0) {
			logger.info("Reminder for task ID {} is in the past, skipping scheduling.", reminder.getTaskId());
			return;
		}
		
		TaskDTO task = null;
		try {
			ResponseEntity<TaskDTO> response = taskController.getTaskById(reminder.getTaskId());
			task = response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (task == null) {
			logger.warn("Could not find task with ID {} for scheduling reminder.", reminder.getTaskId());
		}
		String description = task != null && task.getDescription() != null ? task.getDescription() : "";
		String title = task != null ? task.getTaskTitle() : "Reminder";
		String message = reminder.getMessage() != null ? reminder.getMessage() : description;
 
		logger.info("Scheduling reminder for task ID {} in {} ms.", reminder.getTaskId(), delay);
		ScheduledFuture<?> future = scheduler.schedule(
			new NotificationWorker(reminder.getTaskId(), title, message),
			delay,
			TimeUnit.MILLISECONDS
		);
		scheduledTasks.put(reminder.getTaskId(), future);
		logger.info("Reminder successfully scheduled and tracked for task ID {}", reminder.getTaskId());
	}
	
	public void setNewDateTimeForReminder(ReminderDTO reminder) {

		if (reminder.getRemindAt().isAfter(LocalDateTime.now())) {
//			Date is already in the future, exiting
			return;
		}

		List<DayOfWeek> recurringDays = recurringTaskController.getRecurringDaysOfTask(reminder.getTaskId()).getBody();

		if (recurringDays == null || recurringDays.isEmpty()) {
			return;
		}

		LocalDateTime now = LocalDateTime.now();
		LocalDate today = now.toLocalDate();
		DayOfWeek todayDow = now.getDayOfWeek();
		LocalTime reminderTime = reminder.getRemindAt().toLocalTime();

		LocalDateTime nextRemindAt;

		// If today is a recurring day and the time-of-day hasn't passed yet, keep today
		if (recurringDays.contains(todayDow) && today.atTime(reminderTime).isAfter(now)) {
			nextRemindAt = today.atTime(reminderTime);
		} else {
			// Find the closest recurring day strictly after today (never lands in the past)
			DayOfWeek nextDay = recurringDays.stream()
					.filter(d -> (d.getValue() - todayDow.getValue() + 7) % 7 != 0)
					.min(Comparator.comparingInt(d -> (d.getValue() - todayDow.getValue() + 7) % 7))
					.orElse(null);

			if (nextDay != null) {
				// next() always advances at least 1 day from today, guaranteeing a future date
				nextRemindAt = today.with(TemporalAdjusters.next(nextDay)).atTime(reminderTime);
			} else {
				// Only recurring day is today and its time has passed — schedule same day next week
				nextRemindAt = today.plusWeeks(1).atTime(reminderTime);
			}
		}

		logger.info("Recurring reminder for task ID {}: advancing remindAt from {} to {}",
				reminder.getTaskId(), reminder.getRemindAt(), nextRemindAt);
		reminder.setRemindAt(nextRemindAt);

		try {
			reminderController.updateReminder(reminder);
		} catch (Exception e) {
			logger.error("Failed to persist updated remindAt for task ID {}: {}", reminder.getTaskId(), e.getMessage());
		}
	}

	public void cancelReminder(long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);
		ScheduledFuture<?> future = scheduledTasks.remove(taskId);
		if (future != null) {
			logger.info("Cancelling scheduled reminder for task ID {}", taskId);
			future.cancel(true);
		} else {
			logger.debug("No scheduled reminder found to cancel for task ID {}", taskId);
		}
	}

	public void shutdown() {
		logger.info("Shutting down NotificationManager scheduler...");
		scheduler.shutdown();
		logger.info("Scheduler shutdown initiated.");
	}
}
