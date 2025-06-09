package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.DockStationVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;

import java.util.List;

public record DockStation() implements Action {
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.INIT_ABANDONEDSTATION){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Card currentCard = game.getCurrentCard();
        DockStationVisitor dockStation = new DockStationVisitor();
        currentCard.call(dockStation, username);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForDockStation(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
