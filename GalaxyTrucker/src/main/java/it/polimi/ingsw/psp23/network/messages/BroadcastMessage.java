// Tipi di messaggi usati nella comunicazione da server a client per notificare tutti gli observer

package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.protocol.response.Event;

/**
 * Represents a message intended for broadcast to multiple recipients.
 * This class extends the {@link Message} abstract class and encapsulates an {@link Event} object.
 *
 * The {@code BroadcastMessage} class includes functionality for retrieving the contained event and for accepting
 * a visitor via the Visitor pattern defined by {@link MessageVisitor}.
 *
 * The {@link Event} encapsulated in this message defines the specific action or data to be communicated.
 */
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
