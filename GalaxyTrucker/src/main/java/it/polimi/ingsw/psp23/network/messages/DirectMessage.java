// Tipi di messaggi usati nella comunicazione da server a client per notificare i singoli client

package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.protocol.response.Event;

public final class DirectMessage extends Message{

    Event e;

    public DirectMessage(Event e) {
        this.e = e;
    }

    public Event getEvent() {
        return e;
    }

    @Override
    public <T> T call(MessageVisitor<T> messageVisitor){
        return messageVisitor.visitForDirectMessage(this);
    }

}
