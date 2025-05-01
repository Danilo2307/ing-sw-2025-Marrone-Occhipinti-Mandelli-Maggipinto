package it.polimi.ingsw.psp23.exceptions;

/**
 * Exception thrown when the actual type of a tile at coordinates [i][j]
 * does not match the expected type required by a specific operation.
 * <p>
 * This exception is typically used in conjunction with sealed classes
 * and pattern matching, where a method performs a specialized action
 * (e.g., loading goods, discharging batteries, activating cannons)
 * that requires the tile to be of a specific subtype of {@code Component}.
 * </p>
 * It is usually thrown in the {@code default} branch of a {@code switch} expression
 * when the component type does not match any of the expected cases.
 * */
public class TypeMismatchException extends GameException {
    public TypeMismatchException(String message) {
        super(message);
    }
}
