package it.polimi.ingsw.psp23.exceptions;

/**
 * Thrown when an error occurs while loading an item into a container.
 * Carries the original cause so it can be inspected or logged.
 */
public class LoadException extends GameException {
    /**
     * Constructs a LoadException with a custom message and underlying cause.
     *
     * @param message a brief description of the load failure
     * @param cause   the original exception that triggered this failure
     */
    public LoadException(String message, Throwable cause) {
        super(message);
        // Since GameException doesn’t expose a cause‐taking constructor,
        // we attach it manually.
        initCause(cause);
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        Throwable c = getCause();
        if (c != null) {
            msg += " (caused by "
                    + c.getClass().getSimpleName()
                    + ": "
                    + c.getMessage()
                    + ")";
        }
        return msg;
    }
}
