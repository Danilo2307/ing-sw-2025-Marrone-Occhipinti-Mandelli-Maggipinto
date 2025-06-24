package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.visitor.GetCosmicCreditsVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;

public record EarnCredits() implements Action{

    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (game.getGameStatus() != GameStatus.END_SLAVERS) {
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }

        Card currentCard = game.getCurrentCard();
        GetCosmicCreditsVisitor visitor = new GetCosmicCreditsVisitor();
        currentCard.call(visitor);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForEarnCredits(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }
}
