package main.java.notify;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.custom.SpringContext;
import main.java.controllers.ReminderController;
import main.java.controllers.TaskController;
import org.springframework.http.ResponseEntity;

import main.java.entities.Reminder;
import main.java.entities.Task;

public class NotificationManager {

	private static final Logger logger = LoggerFactory.getLogger(NotificationManager.class.getName());
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final ConcurrentHashMap<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

	private final ReminderController reminderController = SpringContext.getBean(ReminderController.class);
	private final TaskController taskController = SpringContext.getBean(TaskController.class);

	public void initialize() {
		logger.info("Initializing NotificationManager and scheduling existing reminders...");
		List<Reminder> reminders = null;
		try {
			ResponseEntity<List<Reminder>> response = reminderController.getReminders();
			reminders = response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (reminders != null) {
			reminders.forEach(this::scheduleReminder);
		}
        logger.info("Notification manager is initialized.");
	}

	public void scheduleReminder(Reminder reminder) {
		logger.info("Executing {} for reminder: {}", this.getClass(), reminder);
		if (reminder == null) {
			logger.warn("Attempted to schedule a null reminder.");
			return;
		}
		
		// If already scheduled, cancel the existing one first
		cancelReminder(reminder.taskId());

		long delay = reminder.remindAt().getTime() - System.currentTimeMillis();
		if (delay <= 0) {
			logger.info("Reminder for task ID {} is in the past, skipping scheduling.", reminder.taskId());
			return;
		}

		Task task = null;
		try {
			ResponseEntity<Task> response = taskController.getTaskById(reminder.taskId());
			task = response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (task == null) {
			logger.warn("Could not find task with ID {} for scheduling reminder.", reminder.taskId());
		}
		String description = task != null && task.description() != null ? task.description() : "";
		String title = task != null ? task.taskTitle() : "Reminder";
		String message = reminder.message() != null ? reminder.message() : description;
 
		logger.info("Scheduling reminder for task ID {} in {} ms.", reminder.taskId(), delay);
		ScheduledFuture<?> future = scheduler.schedule(
			new NotificationWorker(reminder.taskId(), title, message),
			delay,
			TimeUnit.MILLISECONDS
		);
		scheduledTasks.put(reminder.taskId(), future);
		logger.info("Reminder successfully scheduled and tracked for task ID {}", reminder.taskId());
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
