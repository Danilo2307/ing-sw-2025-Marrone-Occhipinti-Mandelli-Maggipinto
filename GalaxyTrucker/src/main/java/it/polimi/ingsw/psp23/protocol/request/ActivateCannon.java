package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.protocol.response.EventVisitor;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

/**
 * Event triggered when the user wants to activate a double cannon during the action phase.
 * The client specifies the position of the cannon to activate (cx, cy) and the battery hub (bx, by)
 * from which one battery will be consumed.
 * */
public record ActivateCannon(int cx, int cy, int bx, int by) implements Action {

    public void handle(String username){

    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForActivateCannon(this, username);
    }

}
