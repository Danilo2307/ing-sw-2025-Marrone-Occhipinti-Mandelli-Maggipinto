package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.socket.ConnectionThread;

public record RegisterNumPlayers(int number) implements Action{

    public void handle(String username) {
        Game.getInstance().setNumRequestedPlayers(number);
        ConnectionThread.getInstance().start();
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRegisterNumPlayers(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
