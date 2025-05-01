// Tipi di messaggi usati nella comunicazione da client a server

package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.request.SetUsernameActionVisitor;

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
