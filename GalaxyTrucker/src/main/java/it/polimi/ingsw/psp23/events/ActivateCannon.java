package it.polimi.ingsw.psp23.events;

/**
 * Event triggered when the user wants to activate a double cannon during the action phase.
 * The client specifies the position of the cannon to activate (cx, cy) and the battery hub (bx, by)
 * from which one battery will be consumed.
 * */
public record ActivateCannon(int cx, int cy, int bx, int by) implements Event { }
