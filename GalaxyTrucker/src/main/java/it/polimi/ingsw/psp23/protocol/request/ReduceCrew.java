package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.ReduceCrewVisitorNum;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;

import java.util.List;

public record ReduceCrew(int hx, int hy, int num) implements Action {
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.END_ABANDONEDSHIP && game.getGameStatus() != GameStatus.SetCrew && game.getGameStatus() != GameStatus.SECOND_COMBATZONE && game.getGameStatus() != GameStatus.END_SLAVERS){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Card currentCard = game.getCurrentCard();
        ReduceCrewVisitorNum reduceCrew = new ReduceCrewVisitorNum();
        currentCard.call(reduceCrew, username, hx, hy, num);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForReduceCrew(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
