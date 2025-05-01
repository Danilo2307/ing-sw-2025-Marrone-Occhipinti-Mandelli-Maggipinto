package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.Game.Game;

public record TakeReservedTile(int index) implements Action{

    public void handle(String username) {
        Game game = Game.getInstance();
        Player p = game.getPlayerFromNickname(username);
        // suppongo utente inserisca 1-based
        p.takeReservedTile(index);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForTakeReservedTile(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
