// Tipi di messaggi usati nella comunicazione da client a server

package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.protocol.request.Action;


/**
 * Represents a type of message used for communication between client and server, encapsulating an {@link Action}.
 * This class extends the {@link Message} abstract class.
 *
 * The {@code ActionMessage} class includes functionality for retrieving its encapsulated {@link Action}
 * and for accepting a visitor via the Visitor Pattern defined by {@link MessageVisitor}.
 */
public final class ActionMessage extends Message{

    Action a;

    public ActionMessage(Action a) {
        this.a = a;
    }

    public Action getAction() {
        return a;
    }

    @Override
    public <T> T call(MessageVisitor<T> messageVisitor){
        return messageVisitor.visitForActionMessage(this);
    }

    @Override
    public String toString() {
        //return a.call(new SetUsernameActionVisitor());
        return a.toString();
    }

}
