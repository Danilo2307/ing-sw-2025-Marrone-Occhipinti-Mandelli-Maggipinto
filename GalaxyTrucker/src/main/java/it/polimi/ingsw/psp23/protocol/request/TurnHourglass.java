package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.controller.Controller;

/** event triggered when a user wants to turn the hourglass during the building phase */
public record TurnHourglass() implements Action {

    public void handle(String username){
        Controller.getInstance().startTimer();
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForTurnHourGlass(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}