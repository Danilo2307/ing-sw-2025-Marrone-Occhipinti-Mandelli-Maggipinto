package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.controller.Controller;

public record Fixed() implements Action {

    public void handle(String username){
        Controller.getInstance().startCheckBoard();
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForFixed(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }
}
