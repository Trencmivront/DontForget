package main.java.notify;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
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
		
		List<DayOfWeek> recurringDays = recurringTaskController.getRecurringDaysOfTask(reminder.getTaskId()).getBody();

		if (recurringDays != null && !recurringDays.isEmpty()) {
			// Find the next day from today that is in recurringDays
			LocalDateTime now = LocalDateTime.now();
			DayOfWeek today = now.getDayOfWeek();
			DayOfWeek nextDay = recurringDays.stream()
					.min(Comparator.comparingInt(d -> (d.getValue() - today.getValue() + 7) % 7))
					.orElse(today);
			int daysUntilNext = (nextDay.getValue() - today.getValue() + 7) % 7;
			// Keep same time-of-day, advance to the next matching weekday
			LocalDateTime nextRemindAt = reminder.getRemindAt().toLocalDate()
					.with(TemporalAdjusters.nextOrSame(nextDay))
					.atTime(reminder.getRemindAt().toLocalTime());
			// If the computed date is in the past (same day but past the time), advance by one full cycle
			if (!nextRemindAt.isAfter(now) && daysUntilNext == 0) {
				DayOfWeek fallback = recurringDays.stream()
						.min(Comparator.comparingInt(d -> (d.getValue() - today.getValue() + 7) % 7 == 0
								? 7
								: (d.getValue() - today.getValue() + 7) % 7))
						.orElse(today);
				nextRemindAt = reminder.getRemindAt().toLocalDate()
						.with(TemporalAdjusters.next(fallback))
						.atTime(reminder.getRemindAt().toLocalTime());
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
