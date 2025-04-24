package it.polimi.ingsw.psp23.events;

import it.polimi.ingsw.psp23.model.components.Component;

/** event triggered when the user wants to draw a component from the heap*/
public record DrawFromHeap(String username) implements Event{
}
