package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.visitor.GetCosmicCreditsVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;

public record EarnCredits() implements Action{

    /**
     * Handles the logic for earning cosmic credits based on the current game state and card.
     * Validates the game status before proceeding with the credit earning process.
     *
     * @param username the username of the player performing the action
     * @throws InvalidActionException if the action is performed when the game status is not END_SLAVERS or END_PIRATES
     */
    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (game.getGameStatus() != GameStatus.END_SLAVERS && game.getGameStatus() != GameStatus.END_PIRATES) {
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Card currentCard = game.getCurrentCard();
        GetCosmicCreditsVisitor visitor = new GetCosmicCreditsVisitor();
        currentCard.call(visitor, username);
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
