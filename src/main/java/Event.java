/**
 * Represents a task scheduled between a start and end time.
 */
public class Event extends Task {
    /** The event start time entered by the user. */
    private final String from;

    /** The event end time entered by the user. */
    private final String to;

    /**
     * Creates an event task with its description and schedule text.
     *
     * @param description the event description
     * @param from the event start time
     * @param to the event end time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
