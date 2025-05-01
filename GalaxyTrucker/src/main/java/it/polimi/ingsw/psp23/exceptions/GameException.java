package it.polimi.ingsw.psp23.exceptions;

/**
 * Abstract superclass for all expected game-related exceptions.
 *
 * Used to group exceptions that arise from predictable or rule-based game errors,
 * such as illegal actions, invalid coordinates, or type mismatches.
 * This design allows the controller to catch all domain-specific exceptions in a single
 * block (e.g., catch (GameException e)), avoiding bulky multi-catch statements and
 * improving code readability and maintainability.
 * Any exception representing a user-recoverable game error should extend this class.
 */

public abstract class GameException extends RuntimeException {
    public GameException(String message) {
        super(message);
    }
}
