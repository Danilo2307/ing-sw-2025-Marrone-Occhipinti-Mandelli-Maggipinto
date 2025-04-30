package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.protocol.response.EventVisitor;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

/**
 * Event triggered when the user wants to activate a double cannon during the action phase.
 * The client specifies the position of the cannon to activate (cx, cy) and the battery hub (bx, by)
 * from which one battery will be consumed.
 * */
public record ActivateCannon(int cx, int cy, int bx, int by) implements Action {

    public void handle(String username){
        Game game = Game.getInstance();
        Board truck = game.getPlayerFromNickname(username).getTruck();
        truck.activeCannon(cx, cy);
        truck.reduceBatteries(bx, by, 1);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForActivateCannon(this, username);
    }

}
