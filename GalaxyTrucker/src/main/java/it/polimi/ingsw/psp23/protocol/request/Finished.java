package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.controller.Controller;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;

import java.util.List;

public record Finished() implements Action {

    public void handle(String username){
        UsersConnected.getInstance().getGameFromUsername(username).getController().crewPositioned(username);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForFinished(this, username);
    }

    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
