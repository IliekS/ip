/**
 * Represents a task scheduled between a start and end time.
 */
public class Event extends Task {
    /** The event start time entered by the user. */
    private final String from;

    /** The event end time entered by the user. */
    private final String to;

    /**
     * Creates an event whose schedule is stored as one text value.
     *
     * @param description the event description
     * @param when the event schedule
     */
    public Event(String description, String when) {
        super(description);
        this.from = when;
        this.to = null;
    }

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

    /**
     * Returns the event start time entered for this task.
     *
     * @return the event start time
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event end time entered for this task.
     *
     * @return the event end time
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the complete event schedule for storage.
     *
     * @return the event schedule
     */
    public String getWhen() {
        return to == null ? from : from + " to: " + to;
    }

    @Override
    public String toString() {
        if (to == null) {
            return "[E]" + super.toString() + " (at: " + from + ")";
        }
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
