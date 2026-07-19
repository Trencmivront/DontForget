package main.notify;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import main.entities.Reminder;
import main.entities.Task;
import main.services.reminder.GetRemindersService;
import main.services.task.GetTaskByIdService;

public class NotificationManager {

	private static final Logger logger = Logger.getLogger(NotificationManager.class.getName());
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final ConcurrentHashMap<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

	public void initialize() {
		logger.info("Initializing NotificationManager and scheduling existing reminders...");
		List<Reminder> reminders = new GetRemindersService().execute();
		if (reminders != null) {
			reminders.forEach(this::scheduleReminder);
		}
        logger.info("Notification manager is initialized.");
	}

	public void scheduleReminder(Reminder reminder) {
		if (reminder == null) {
			logger.warning("Attempted to schedule a null reminder.");
			return;
		}
		
		// If already scheduled, cancel the existing one first
		cancelReminder(reminder.task_id());

		long delay = reminder.remind_at().getTime() - System.currentTimeMillis();
		if (delay <= 0) {
			logger.info("Reminder for task ID " + reminder.task_id() + " is in the past, skipping scheduling.");
			return;
		}

		Task task = new GetTaskByIdService().execute(reminder.task_id());
		if (task == null) {
			logger.warning("Could not find task with ID " + reminder.task_id() + " for scheduling reminder.");
		}
		String description = task != null && task.description() != null ? task.description() : "";
		String title = task != null ? task.task_title() : "Reminder";
		String message = reminder.cstm_message() != null ? reminder.cstm_message() : description;
 
		logger.info("Scheduling reminder for task ID " + reminder.task_id() + " in " + delay + " ms.");
		ScheduledFuture<?> future = scheduler.schedule(
			new NotificationWorker(reminder.task_id(), title, message),
			delay,
			TimeUnit.MILLISECONDS
		);
		scheduledTasks.put(reminder.task_id(), future);
		logger.info("Reminder successfully scheduled and tracked for task ID " + reminder.task_id());
	}

	public void cancelReminder(long taskId) {
		ScheduledFuture<?> future = scheduledTasks.remove(taskId);
		if (future != null) {
			logger.info("Cancelling scheduled reminder for task ID " + taskId);
			future.cancel(true);
		} else {
			logger.fine("No scheduled reminder found to cancel for task ID " + taskId);
		}
	}

	public void shutdown() {
		logger.info("Shutting down NotificationManager scheduler...");
		scheduler.shutdown();
		logger.info("Scheduler shutdown initiated.");
	}
}
