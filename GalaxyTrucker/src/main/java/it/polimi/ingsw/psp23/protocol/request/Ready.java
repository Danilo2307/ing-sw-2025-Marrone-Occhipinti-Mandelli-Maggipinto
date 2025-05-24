package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.PassVisitor;
import it.polimi.ingsw.psp23.model.cards.ReadyVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;

import java.util.List;

public record Ready() implements Action {
    public void handle(String username){
        Game game = Game.getInstance();
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

    public List<DirectMessage> getDm(){
        return null;
    }

    public List<BroadcastMessage> getBm(){
        return null;
    }

}
