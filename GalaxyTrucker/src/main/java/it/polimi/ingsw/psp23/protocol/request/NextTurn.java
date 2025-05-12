package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.PassVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public record NextTurn() implements Action {
    public void handle(String username){
        Game game = Game.getInstance();
        if(game.getGameStatus() != GameStatus.INIT_ABANDONEDSHIP && game.getGameStatus() != GameStatus.INIT_ABANDONEDSTATION && game.getGameStatus()!= GameStatus.END_PIRATES && game.getGameStatus() != GameStatus.INIT_PLANETS && game.getGameStatus()!= GameStatus.END_SLAVERS && game.getGameStatus() != GameStatus.END_SMUGGLERS && game.getGameStatus() != GameStatus.END_ABANDONEDSTATION){
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
