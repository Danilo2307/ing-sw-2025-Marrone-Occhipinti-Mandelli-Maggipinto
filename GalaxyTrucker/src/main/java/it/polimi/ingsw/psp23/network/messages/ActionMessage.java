// Tipi di messaggi usati nella comunicazione da client a server

package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.events.Action;

public final class ActionMessage extends Message{

    Action a;

    public ActionMessage(Action a) {
        this.a = a;
    }

    @Override
    public Action getAction() {
        return a;
    }

}
