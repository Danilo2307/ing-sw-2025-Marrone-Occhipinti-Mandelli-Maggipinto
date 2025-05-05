package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.ActiveCannonVisitor;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.RemovePreciousItemVisitor;

public record RemovePreciousItem(int cx, int cy, int num) implements Action {
    public void handle(String username){
        Game game = Game.getInstance();
        Card currentCard = game.getCurrentCard();
        RemovePreciousItemVisitor removeItem = new RemovePreciousItemVisitor();
        currentCard.call(removeItem, username, cx, cy, num);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRemovePreciousItem(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }
}
