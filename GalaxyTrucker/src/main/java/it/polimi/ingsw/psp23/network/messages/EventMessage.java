package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.events.Event;

public final class EventMessage extends Message{
    Event e;
    public EventMessage(Event e){
        this.e = e;
    }
}
