// Tipi di messaggi usati nella comunicazione da server a client per notificare i singoli client

package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.protocol.response.Event;

/**
 * Represents a direct message encapsulating an {@link Event}. This class is intended to be used for
 * communication purposes, where a specific event is targeted for an individual recipient.
 *
 * The {@code DirectMessage} extends the {@link Message} abstract class, adhering to the Visitor design
 * pattern, which allows external processing of the message through a {@link MessageVisitor}.
 *
 * The event encapsulated within a DirectMessage defines the specific action or data to be handled
 * by the recipient of the message. The {@code toString} method provides a string representation
 * of the contained event.
 */
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

    @Override
    public String toString() {
        return e.toString();
    }

}
