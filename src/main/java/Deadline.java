/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    /** The deadline text entered by the user. */
    private final String by;

    /**
     * Creates a deadline task with its description and deadline text.
     *
     * @param description the task description
     * @param by the deadline text
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline text entered for this task.
     *
     * @return the deadline text
     */
    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
