package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.ActiveCannonVisitor;
import it.polimi.ingsw.psp23.model.cards.Card;

/**
 * Event triggered when the user wants to activate a double cannon during the action phase.
 * The client specifies the position of the cannon to activate (cx, cy) and the battery hub (bx, by)
 * from which one battery will be consumed.
 * */
public record ActivateCannon(int cx, int cy, int bx, int by) implements Action {

    public void handle(String username){
        Game game = Game.getInstance();
        Card currentCard = game.getCurrentCard();
        ActiveCannonVisitor activeCannon = new ActiveCannonVisitor();
        currentCard.call(activeCannon, username, cx, cy);
        game.getPlayerFromNickname(username).getTruck().reduceBatteries(bx, by,1);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForActivateCannon(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
