package it.polimi.ingsw.psp23.network;

/**
 * Represents a critical server-side error that should terminate the application.
 * This type of error indicates that recovery is not feasible.
 */
public class ServerCriticalError extends Error {
    /**
     * Constructs a new ServerError with the specified error message.
     *
     * @param message a description of the critical failure
     */
    public ServerCriticalError(String message) {
        super(message);
    }
}


