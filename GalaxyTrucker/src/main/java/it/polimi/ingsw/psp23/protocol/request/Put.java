package it.polimi.ingsw.psp23.protocol.request;

// Azione relativa al comando della TUI "posziona"

import it.polimi.ingsw.psp23.controller.Controller;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

public record Put() implements Action {

    public void handle(String username){
        Controller.getInstance().playerFinishedBuilding(username);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForPut(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
