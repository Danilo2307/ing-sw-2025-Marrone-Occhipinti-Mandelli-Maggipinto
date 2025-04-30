package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.model.Game.Game;

public record ActivateShield(int sx, int sy, int bx, int by) implements Action{

    public void handle(String username){
        Game game = Game.getInstance();
        Board truck = game.getPlayerFromNickname(username).getTruck();
        truck.activeCannon(sx, sy);
        truck.reduceBatteries(bx, by, 1);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForActivateShield(this, username);
    }
}
