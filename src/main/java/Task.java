/**
 * Represents a task stored by Bob.
 */
public abstract class Task {
    /** The description entered for this task. */
    private final String description;

    /** Whether this task has been completed. */
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as complete. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns this task's status and description for display.
     *
     * @return the formatted task text
     */
    @Override
    public String toString() {
        String status = isDone ? "[X]" : "[ ]";
        return status + " " + description;
    }
}
