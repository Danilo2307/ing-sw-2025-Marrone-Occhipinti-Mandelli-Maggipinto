package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.protocol.request.Action;

public class GetActionVisitor implements MessageVisitor<Action> {

    public Action visitForDirectMessage(DirectMessage directMessage){
        return null;
    }

    public Action visitForBroadcastMessage(BroadcastMessage broadcastMessage){
        return null;
    }

    public Action visitForActionMessage(ActionMessage actionMessage){
        return actionMessage.getAction();
    }

}
