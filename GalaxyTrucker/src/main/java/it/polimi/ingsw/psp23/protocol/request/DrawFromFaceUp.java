package it.polimi.ingsw.psp23.protocol.request;

/** Event triggered when the user wants to draw a face-up component at position x. */
public record DrawFromFaceUp (int x, int version) implements Action {
}
