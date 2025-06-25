package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.visitor.ReadyVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;

public record Ready() implements Action {

    /**
     * Handles the "ready" action, indicating the user has completed activating effects or actions
     * related to a specific game card. This action is only allowed during specific game statuses.
     *
     * @param username the username of the player performing the action
     * @throws InvalidActionException if the action is performed during an invalid game status
     */
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.FIRST_COMBATZONE && game.getGameStatus() != GameStatus.SECOND_COMBATZONE && game.getGameStatus() != GameStatus.THIRD_COMBATZONE && game.getGameStatus() != GameStatus.ENDTHIRD_COMBATZONE && game.getGameStatus() != GameStatus.INIT_METEORSWARM && game.getGameStatus() != GameStatus.INIT_OPENSPACE && game.getGameStatus() != GameStatus.INIT_PIRATES && game.getGameStatus() != GameStatus.END_PIRATES && game.getGameStatus() != GameStatus.INIT_SLAVERS && game.getGameStatus() != GameStatus.INIT_SMUGGLERS){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
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
