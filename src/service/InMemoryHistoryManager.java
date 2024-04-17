package service;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int HISTORY_CAPACITY = 10;
    private List<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            Task savedTask = new Task(task.getTitle(), task.getDescription());
            savedTask.setId(task.getId());
            savedTask.setStatus(task.getStatus());
            if (history.size() == HISTORY_CAPACITY) {
                history.remove(0);
            }
            history.add(savedTask);
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }

}
