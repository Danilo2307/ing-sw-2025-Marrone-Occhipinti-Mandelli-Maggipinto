package it.polimi.ingsw.psp23.events;

/** Event triggered when the user wants to draw a face-up component at position x. */
public record DrawFromFaceUp (String username, int x) implements Event {
}
