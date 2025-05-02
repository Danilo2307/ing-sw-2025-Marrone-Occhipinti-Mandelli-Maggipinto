package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.controller.Controller;
import it.polimi.ingsw.psp23.exceptions.PlayerExistsException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

public record SetUsername(String username) implements Action {

    public void handle(String username){
        Controller.getInstance().addPlayerToGame(username);
        // TODO: DA LEVARE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Controller.getInstance().startBuildingPhase();
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForSetUsername(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return actionVisitorSinglePar.visitForSetUsername(this);
    }

    @Override
    public String toString() {
        return username;
    }

}
