// Tipi di messaggi usati nella comunicazione da server a client per notificare tutti gli observer

package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.protocol.response.Event;

public final class BroadcastMessage extends Message{

    Event e;

    public BroadcastMessage(Event e) {
        this.e = e;
    }

    public Event getEvent() {
        return e;
    }

    @Override
    public <T> T call(MessageVisitor<T> messageVisitor){
        return messageVisitor.visitForBroadcastMessage(this);
    }

    @Override
    public String toString() {
        return e.toString();
    }

}
