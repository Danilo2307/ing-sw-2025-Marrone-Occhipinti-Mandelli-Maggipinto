package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.PassVisitor;
import it.polimi.ingsw.psp23.model.cards.ReadyVisitor;

public record Ready() implements Action {
    public void handle(String username){
        Game game = Game.getInstance();
        Card currentCard = game.getCurrentCard();
        ReadyVisitor ready = new ReadyVisitor();
        currentCard.call(ready, username);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForReady(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
