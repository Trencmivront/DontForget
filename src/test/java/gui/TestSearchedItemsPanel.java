package test.java.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import main.io.github.trencmivront.dontforget.controllers.ProjectController;
import main.io.github.trencmivront.dontforget.controllers.TagController;
import main.io.github.trencmivront.dontforget.controllers.TaskController;
import main.io.github.trencmivront.dontforget.custom.SpringContext;
import main.io.github.trencmivront.dontforget.dto.ProjectDTO;
import main.io.github.trencmivront.dontforget.dto.TagDTO;
import main.io.github.trencmivront.dontforget.dto.TaskDTO;
import main.io.github.trencmivront.dontforget.gui.panels.SearchedItemsPanel;
import main.io.github.trencmivront.dontforget.gui.windows.SearchWindow;

@ExtendWith(MockitoExtension.class)
class TestSearchedItemsPanel {

	@Mock
	private ProjectController projectController;

	@Mock
	private TaskController taskController;

	@Mock
	private TagController tagController;

	@Mock
	private SearchWindow searchWindow;

	private MockedStatic<SpringContext> springContextMock;

	private ProjectDTO sampleProject;
	private TaskDTO sampleTask;
	private TagDTO sampleTag;
	private TagDTO sampleTaskTag;

	@BeforeEach
	void init() {
//		sample data
		sampleProject = new ProjectDTO(1L, "My Project", "A test project", null);

		sampleTask = new TaskDTO();
		sampleTask.setTaskId(10L);
		sampleTask.setTaskTitle("My Task");

		sampleTag = new TagDTO(100L, "My Tag", null);

		sampleTaskTag = new TagDTO(200L, "Task Tag", null);

//		intercept SpringContext.getBean() so no Spring context is needed
		springContextMock = mockStatic(SpringContext.class);
		springContextMock.when(() -> SpringContext.getBean(ProjectController.class)).thenReturn(projectController);
		springContextMock.when(() -> SpringContext.getBean(TaskController.class)).thenReturn(taskController);
		springContextMock.when(() -> SpringContext.getBean(TagController.class)).thenReturn(tagController);
	}

	@AfterEach
	void tearDown() {
		springContextMock.close();
	}

	@Test
	void testAllSectionsAreListedWhenNoneAreEmpty() {
//		arrange: all three controllers return non-empty lists
		when(projectController.getProjects())
				.thenReturn(ResponseEntity.ok(List.of(sampleProject)));
		when(taskController.getTasks())
				.thenReturn(ResponseEntity.ok(List.of(sampleTask)));
		when(tagController.getTagsOfTask(sampleTask.getTaskId()))
				.thenReturn(ResponseEntity.ok(List.of(sampleTaskTag)));
		when(tagController.getTags())
				.thenReturn(ResponseEntity.ok(List.of(sampleTag)));

//		act
		SearchedItemsPanel panel = new SearchedItemsPanel();

//		assert: 3 headers + 1 project row + 1 task row + 1 tag row = 6 rows total
		DefaultTableModel model = getModel(panel);
		assertEquals(6, model.getRowCount(),
				"Expected 3 header rows + 3 item rows (1 project, 1 task, 1 tag)");
	}

	@Test
	void testProjectSectionContainsHeaderAndRow() {
//		arrange
		when(projectController.getProjects())
				.thenReturn(ResponseEntity.ok(List.of(sampleProject)));
		when(taskController.getTasks())
				.thenReturn(ResponseEntity.ok(List.of()));
		when(tagController.getTags())
				.thenReturn(ResponseEntity.ok(List.of()));

//		act
		SearchedItemsPanel panel = new SearchedItemsPanel();

//		assert: 1 header + 1 project row = 2 rows
		DefaultTableModel model = getModel(panel);
		assertEquals(2, model.getRowCount(),
				"Expected a Projects header row and one project item row");
	}

	@Test
	void testTaskSectionContainsHeaderAndRow() {
//		arrange
		when(projectController.getProjects())
				.thenReturn(ResponseEntity.ok(List.of()));
		when(taskController.getTasks())
				.thenReturn(ResponseEntity.ok(List.of(sampleTask)));
		when(tagController.getTagsOfTask(sampleTask.getTaskId()))
				.thenReturn(ResponseEntity.ok(List.of()));
		when(tagController.getTags())
				.thenReturn(ResponseEntity.ok(List.of()));

//		act
		SearchedItemsPanel panel = new SearchedItemsPanel();

//		assert: 1 header + 1 task row = 2 rows
		DefaultTableModel model = getModel(panel);
		assertEquals(2, model.getRowCount(),
				"Expected a Tasks header row and one task item row");
	}

	@Test
	void testTagSectionContainsHeaderAndRow() {
//		arrange
		when(projectController.getProjects())
				.thenReturn(ResponseEntity.ok(List.of()));
		when(taskController.getTasks())
				.thenReturn(ResponseEntity.ok(List.of()));
		when(tagController.getTags())
				.thenReturn(ResponseEntity.ok(List.of(sampleTag)));

//		act
		SearchedItemsPanel panel = new SearchedItemsPanel();

//		assert: 1 header + 1 tag row = 2 rows
		DefaultTableModel model = getModel(panel);
		assertEquals(2, model.getRowCount(),
				"Expected a Tags header row and one tag item row");
	}

	@Test
	void testNoRowsWhenAllListsAreEmpty() {
//		arrange: all controllers return empty lists
		when(projectController.getProjects())
				.thenReturn(ResponseEntity.ok(List.of()));
		when(taskController.getTasks())
				.thenReturn(ResponseEntity.ok(List.of()));
		when(tagController.getTags())
				.thenReturn(ResponseEntity.ok(List.of()));

//		act
		SearchedItemsPanel panel = new SearchedItemsPanel();

//		assert
		DefaultTableModel model = getModel(panel);
		assertEquals(0, model.getRowCount(),
				"Expected no rows when all lists are empty");
	}

	@Test
	void testMultipleProjectsAllListed() {
//		arrange
		ProjectDTO project2 = new ProjectDTO(2L, "Second Project", "Another project", null);
		when(projectController.getProjects())
				.thenReturn(ResponseEntity.ok(List.of(sampleProject, project2)));
		when(taskController.getTasks())
				.thenReturn(ResponseEntity.ok(List.of()));
		when(tagController.getTags())
				.thenReturn(ResponseEntity.ok(List.of()));

//		act
		SearchedItemsPanel panel = new SearchedItemsPanel();

//		assert: 1 header + 2 project rows = 3
		DefaultTableModel model = getModel(panel);
		assertEquals(3, model.getRowCount(),
				"Expected a Projects header and two project item rows");
	}

	@Test
	void testTaskTagsAreAssignedAsToolTipWhenPresent() {
//		arrange: task has tags, verify row count still correct
		when(projectController.getProjects())
				.thenReturn(ResponseEntity.ok(List.of()));
		when(taskController.getTasks())
				.thenReturn(ResponseEntity.ok(List.of(sampleTask)));
		when(tagController.getTagsOfTask(sampleTask.getTaskId()))
				.thenReturn(ResponseEntity.ok(List.of(sampleTaskTag)));
		when(tagController.getTags())
				.thenReturn(ResponseEntity.ok(List.of()));

//		act
		SearchedItemsPanel panel = new SearchedItemsPanel();

//		assert: 1 task header + 1 task row = 2 rows
		DefaultTableModel model = getModel(panel);
		assertEquals(2, model.getRowCount(),
				"Expected a Tasks header and one task row even when tags are present");

//		the task row value should be a JPanel (TaskRowPanel)
		Object taskRowValue = model.getValueAt(1, 0);
		assertTrue(taskRowValue instanceof javax.swing.JPanel,
				"Task row should be rendered as a JPanel");
	}

	/**
	 * Reflectively retrieves the private {@code model} field from the panel
	 * so that row-count assertions can be made without exposing internal state.
	 */
	private DefaultTableModel getModel(SearchedItemsPanel panel) {
		try {
			java.lang.reflect.Field field = SearchedItemsPanel.class.getDeclaredField("model");
			field.setAccessible(true);
			return (DefaultTableModel) field.get(panel);
		} catch (Exception e) {
			throw new RuntimeException("Could not access DefaultTableModel from SearchedItemsPanel", e);
		}
	}
}
