/**
 * Represents a task without a deadline or scheduled time.
 */
public class Todo extends Task {
    /**
     * Creates a to-do task with the given description.
     *
     * @param description the task description
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
