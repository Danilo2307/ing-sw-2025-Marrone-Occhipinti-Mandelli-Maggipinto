package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.visitor.PassVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;

public record NextTurn() implements Action {


    /**
     * Handles the process of transitioning to the next turn in the game for the specified user.
     * Validates the current game status before proceeding and throws an exception if the action
     * is not allowed at this stage. It uses a visitor pattern to initiate the next turn actions on the current card.
     *
     * @param username the username of the player initiating the next turn.
     *                 The player's associated game is determined based on this identifier.
     * @throws InvalidActionException if the game is not in a state that allows transitioning to the next turn.
     */
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.INIT_ABANDONEDSHIP && game.getGameStatus() != GameStatus.INIT_ABANDONEDSTATION && game.getGameStatus()!= GameStatus.END_PIRATES && game.getGameStatus() != GameStatus.INIT_PLANETS && game.getGameStatus()!= GameStatus.END_SLAVERS && game.getGameStatus() != GameStatus.END_SMUGGLERS && game.getGameStatus() != GameStatus.END_ABANDONEDSTATION && game.getGameStatus() != GameStatus.END_PLANETS){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Card currentCard = game.getCurrentCard();
        PassVisitor nextTurn = new PassVisitor();
        currentCard.call(nextTurn, username);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForNextTurn(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
