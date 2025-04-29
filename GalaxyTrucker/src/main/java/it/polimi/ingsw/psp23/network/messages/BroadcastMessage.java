// Tipi di messaggi usati nella comunicazione da server a client per notificare tutti gli observer

package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.events.server.Event;

public final class BroadcastMessage extends Message{

    Event e;

    public BroadcastMessage(Event e) {
        this.e = e;
    }

    @Override
    public Event getEvent() {
        return e;
    }
}
