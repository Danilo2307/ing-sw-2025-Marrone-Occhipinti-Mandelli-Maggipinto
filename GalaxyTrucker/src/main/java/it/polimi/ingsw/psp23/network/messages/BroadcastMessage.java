package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.events.Event;

public final class BroadcastMessage extends Message{

    public BroadcastMessage(Event e) {
        super(e);
    }
}
