package it.polimi.ingsw.psp23.model.enumeration;

/**
 * The ComponentLocation enum defines the possible states for a component in the game.
 *
 * Enum Constants:
 * - PILE: Represents a component that is stored in the pile.
 * - IN_HAND: Represents a component currently held by a player.
 * - FACE_UP: Represents a component that is discarded from a player and is visibly face up.
 * - ON_TRUCK: Represents a component placed on a truck.
 * - RESERVED: Represents a reserved component that is not currently in play.
 */
public enum ComponentLocation {
    PILE,
    IN_HAND,
    FACE_UP,    // discarded from player
    ON_TRUCK,    // x,y take positive values
    RESERVED;
}
