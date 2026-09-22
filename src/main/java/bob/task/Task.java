package bob.task;

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
     * @param description The task description.
     */
    public Task(String description) {
        if (description == null || description.isBlank() || description.contains("|")
                || description.codePoints().anyMatch(value -> Character.isISOControl(value) && value != '\t')) {
            throw new InvalidTaskException(
                    "Descriptions must be non-empty and cannot contain | or control characters.");
        }
        this.description = description.strip();
        this.isDone = false;
    }

    /**
     * Marks this task as complete.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return True if the task is complete.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the description entered for this task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Compares task type, normalized description, and dates, ignoring completion status.
     * Description matching ignores letter case and repeated whitespace.
     */
    public boolean hasSameDetails(Task other) {
        if (getClass() != other.getClass()
                || !description.replaceAll("(?U)\\s+", " ")
                        .equalsIgnoreCase(other.description.replaceAll("(?U)\\s+", " "))) {
            return false;
        }
        if (this instanceof Deadline deadline && other instanceof Deadline otherDeadline) {
            return deadline.getBy().equals(otherDeadline.getBy());
        }
        if (this instanceof Event event && other instanceof Event otherEvent) {
            return event.getFrom().equals(otherEvent.getFrom()) && event.getTo().equals(otherEvent.getTo());
        }
        return true;
    }

    /**
     * Returns this task's status and description for display.
     *
     * @return The formatted task text.
     */
    @Override
    public String toString() {
        String status = isDone ? "[X]" : "[ ]";
        return status + " " + description;
    }
}
