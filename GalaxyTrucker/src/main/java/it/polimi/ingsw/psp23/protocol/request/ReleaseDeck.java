package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.Card;

import java.util.ArrayList;

public record ReleaseDeck(int x) implements Action {

    public void handle(String username) {
        Game game = Game.getInstance();
        Player player = game.getPlayerFromNickname(username);
        switch(x) {
            case 1 -> game.releaseVisibleDeck1(player);
            case 2 -> game.releaseVisibleDeck2(player);
            case 3 -> game.releaseVisibleDeck3(player);
            default -> throw new InvalidActionException("Valore inaspettato: " + x);
        }
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForReleaseDeck(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }
}
