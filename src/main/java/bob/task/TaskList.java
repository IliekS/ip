package bob.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Owns and manages Bob's ordered collection of tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks Initial tasks in display order.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task identified by a one-based task number.
     *
     * @param taskNumber One-based task number.
     * @return Selected task.
     */
    public Task get(int taskNumber) {
        return tasks.get(taskNumber - 1);
    }

    /**
     * Removes and returns the task identified by a one-based task number.
     *
     * @param taskNumber One-based task number.
     * @return Removed task.
     */
    public Task remove(int taskNumber) {
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Returns whether the supplied one-based task number exists.
     *
     * @param taskNumber One-based task number to check.
     * @return True if the task number exists.
     */
    public boolean hasTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    /**
     * Returns whether this task list contains no tasks.
     *
     * @return True if the task list is empty.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an unmodifiable view of the tasks in display order.
     *
     * @return Unmodifiable task list.
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
