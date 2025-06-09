package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.DockStationVisitor;
import it.polimi.ingsw.psp23.model.cards.LoadGoodsVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;

import java.util.List;

public record LoadGood(int lx, int ly) implements Action {
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.END_ABANDONEDSTATION && game.getGameStatus() != GameStatus.END_PLANETS && game.getGameStatus() != GameStatus.END_SMUGGLERS){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Card currentCard = game.getCurrentCard();
        LoadGoodsVisitor loadGood = new LoadGoodsVisitor();
        currentCard.call(loadGood, username, lx, ly);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForLoadGoods(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
