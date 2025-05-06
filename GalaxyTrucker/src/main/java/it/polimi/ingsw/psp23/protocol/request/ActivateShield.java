package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.ActiveShieldVisitor;
import it.polimi.ingsw.psp23.model.cards.Card;

public record ActivateShield(int sx, int sy, int bx, int by) implements Action{

    public void handle(String username){
        Game game = Game.getInstance();
        Card currentCard = game.getCurrentCard();
        ActiveShieldVisitor activeShield = new ActiveShieldVisitor();
        currentCard.call(activeShield, username, sx, sy);
        game.getPlayerFromNickname(username).getTruck().reduceBatteries(bx, by,1);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForActivateShield(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
