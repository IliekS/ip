package bob.task;

/**
 * Reports invalid task details using a message suitable for the user.
 */
public class InvalidTaskException extends IllegalArgumentException {
    /**
     * Creates a validation failure with actionable feedback.
     */
    public InvalidTaskException(String message) {
        super(message);
    }
}
