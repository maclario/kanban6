package service;

import model.EpicTask;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest {
    private static TaskManager taskManager;
    private static Task task1;
    private static EpicTask epic2;
    private static Subtask sub3;
    private static Task convertedEpic2;
    private static Task convertedSub3;
    private static int task1Id;
    private static int epic2Id;
    private static int sub3Id;

    @BeforeAll
    public static void historyShouldBeEmptyIfNoTasksAdded() {
        taskManager = Managers.getDefault();
        List<Task> history = taskManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой.");
    }

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        task1 = new Task("Title t1", "Description t1");
        taskManager.createTask(task1);
        task1Id = taskManager.getId();
        epic2 = new EpicTask("Title e2", "Description e2");
        taskManager.createEpicTask(epic2);
        epic2Id = taskManager.getId();
        sub3 = new Subtask("Title s3", "Description s3", epic2Id);
        taskManager.createSubtask(sub3);
        sub3Id = taskManager.getId();

        convertedEpic2 = new Task(epic2.getTitle(), epic2.getDescription());
        convertedEpic2.setId(epic2Id);
        convertedSub3 = new Task(sub3.getTitle(), sub3.getDescription());
        convertedSub3.setId(sub3Id);

        taskManager.getTask(task1Id);
        taskManager.getEpicTask(epic2Id);
        taskManager.getSubtask(sub3Id);
    }

    @Test
    public void shouldAddTasksToHistory() {
        List<Task> history = taskManager.getHistory();

        assertEquals(3, history.size(), "Размер истории не совпадает.");
        assertEquals(task1, history.get(0), "Задача не добавлена в историю.");
        assertEquals(convertedEpic2, history.get(1), "Эпик не добавлена в историю.");
        assertEquals(convertedSub3, history.get(2), "Подзадача не добавлена в историю.");
    }

    @Test
    public void historyShouldNotContainsDuplicates() {
        taskManager.getTask(task1Id);
        taskManager.getEpicTask(epic2Id);
        taskManager.getSubtask(sub3Id);

        List<Task> history = taskManager.getHistory();

        assertEquals(3, history.size(), "Размер истории увеличился.");
    }

    @Test
    public void historyShouldContainOriginalTasksAfterUpdate() {
        Task updatedTask1 = new Task("New Title task1", "New Description task1");
        updatedTask1.setId(task1Id);
        updatedTask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(updatedTask1);

        EpicTask updatedEpic2 = new EpicTask("New Title epic2", "New Description epic2");
        updatedEpic2.setId(epic2Id);
        updatedEpic2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateEpicTask(updatedEpic2);

        Subtask updatedSub3 = new Subtask("New Title sub3", "New Description sub3", epic2Id);
        updatedSub3.setId(sub3Id);
        updatedSub3.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(updatedSub3);

        List<Task> history = taskManager.getHistory();

        assertEquals("Title t1", history.get(0).getTitle(), "Title задачи изменился.");
        assertEquals("Description t1", history.get(0).getDescription(), "Description задачи изменился.");
        assertEquals(TaskStatus.NEW, history.get(0).getStatus(), "Status задачи изменился.");

        assertEquals("Title e2", history.get(1).getTitle(), "Title эпика изменился.");
        assertEquals("Description e2", history.get(1).getDescription(), "Description эпика изменился.");
        assertEquals(TaskStatus.NEW, history.get(1).getStatus(), "Status эпика изменился.");

        assertEquals("Title s3", history.get(2).getTitle(), "Title подзадачи изменился.");
        assertEquals("Description s3", history.get(2).getDescription(), "Description подзадачи изменился.");
        assertEquals(TaskStatus.NEW, history.get(2).getStatus(), "Status подзадачи изменился.");
    }

    @Test
    public void shouldRemoveTaskFromHistoryById() {
        taskManager.deleteTask(task1Id);
        List<Task> history = taskManager.getHistory();

        assertEquals(2, history.size(), "Размер истории не уменьшился на единицу.");
        assertEquals(convertedEpic2, history.get(0), "Эпик не сместился к голове списка.");
        assertEquals(convertedSub3, history.get(1), "Подзадача не сметислась к голове списка.");
    }

    @Test
    public void shouldRemoveEpicFromHistoryById() {
        taskManager.deleteEpicTask(epic2Id);
        List<Task> history = taskManager.getHistory();

        assertEquals(1, history.size(), "Из истории должны удалиться и эпик, и его подзадача.");
    }

    @Test
    public void shouldRemoveSubtaskFromHistoryById() {
        System.out.println(taskManager.getHistory().size());
        taskManager.deleteSubtask(sub3Id);
        System.out.println(taskManager.getHistory().size());
        List<Task> history = taskManager.getHistory();

        assertEquals(2, history.size(), "Размер истории не уменьшился на единицу.");
    }

    @Test
    public void historyShouldBeEmptyAfterRemoveAllTasks() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpicTasks();
        List<Task> history = taskManager.getHistory();

        assertTrue(history.isEmpty(), "История должна быть пустой.");
    }



}
