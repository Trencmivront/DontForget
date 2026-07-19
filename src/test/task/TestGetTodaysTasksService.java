package test.task;

import java.util.List;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import main.dco.TaskDCO;
import main.entities.Task;
import main.services.task.CreateTaskService;
import main.services.task.GetTodaysTasksService;

@RunWith(MockitoJUnitRunner.class)
public class TestGetTodaysTasksService {
	
	@InjectMocks
	GetTodaysTasksService getTodaysTasksService;
	
	CreateTaskService createTaskService;
	
	@Before
	public void begin() {
		createTaskService = new CreateTaskService();
	}
	
	@Test
	public void testServiceReturnsTrueValue(){
		TaskDCO taskDCO = new TaskDCO("", "", 1l, 1,LocalDate.now(), 1l);
		
		createTaskService.execute(taskDCO);
		
		List<Task> tasks = getTodaysTasksService.execute();
		
		assertNotNull(tasks);
		
	}
}

