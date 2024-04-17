package service;

import model.EpicTask;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {
    private static InMemoryHistoryManager storyManager;

    @BeforeAll
    public static void beforeAll() {
        storyManager = new InMemoryHistoryManager();

        storyManager.add(new Task("Название 1", "Описание 1"));
        storyManager.add(new Task("Название 2", "Описание 2"));
        storyManager.add(new Task("Название 3", "Описание 3"));
        storyManager.add(new Task("Название 4", "Описание 4"));
        storyManager.add(new Task("Название 5", "Описание 5"));
        storyManager.add(new Task("Название 6", "Описание 6"));
        storyManager.add(new Task("Название 7", "Описание 7"));
        storyManager.add(new Task("Название 8", "Описание 8"));
        storyManager.add(new Task("Название 9", "Описание 9"));
        storyManager.add(new Task("Название 10", "Описание 10"));
    }

    @Test
    public void shouldAddTaskToHistory() {
        Task task11 = new Task("Название t11", "Описание t11");
        task11.setId(11);
        storyManager.add(task11);
        List<Task> result = storyManager.getHistory();
        assertEquals(11, result.get(9).getId());
        assertEquals("Название t11", result.get(9).getTitle());
        assertEquals("Описание t11", result.get(9).getDescription());
    }

    @Test
    public void shouldAddSubtaskToHistory() {
        Subtask sub = new Subtask("Название s11", "Описание s11", 1);
        sub.setId(11);
        storyManager.add(sub);
        List<Task> result = storyManager.getHistory();
        assertEquals(11, result.get(9).getId());
        assertEquals("Название s11", result.get(9).getTitle());
        assertEquals("Описание s11", result.get(9).getDescription());
    }

    @Test
    public void shouldAddEpicTaskToHistory() {
        EpicTask epic = new EpicTask("Название 12", "Описание 12");
        epic.setId(12);
        storyManager.add(epic);
        List<Task> result = storyManager.getHistory();
        assertEquals(12, result.get(9).getId());
        assertEquals("Название 12", result.get(9).getTitle());
        assertEquals("Описание 12", result.get(9).getDescription());
    }

    @Test
    public void shouldSaveDataOfTheTaskAtTheTimeOfTheViewing() {
        EpicTask epic = new EpicTask("Название 17", "Описание 17");
        epic.setId(17);
        storyManager.add(epic);
        epic.setId(23);
        epic.setTitle("Новое название");
        epic.setDescription("Новое описание");
        List<Task> result = storyManager.getHistory();
        assertEquals(17, result.get(9).getId());
        assertEquals("Название 17", result.get(9).getTitle());
        assertEquals("Описание 17", result.get(9).getDescription());
    }

    @Test
    public void historyHasSize10WhenViewing11task() {
        Task task11 = new Task("Название 11", "Описание 11");
        storyManager.add(task11);
        List<Task> result = storyManager.getHistory();
        assertEquals(10, result.size());
    }

}
