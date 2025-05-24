package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.LandOnPlanetVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;

import java.util.List;

public class DrawCard implements Action{

    public void handle(String username){
        Game game = Game.getInstance();
        if(game.getGameStatus() != GameStatus.WAITING_FOR_NEW_CARD){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        if(!game.getPlayers().isEmpty() && !username.equals(game.getPlayers().getFirst().getNickname())){
            throw new InvalidActionException("Solo il leader può eseguire questa azione!(se non ci sono giocatori in volo dovrà essere il vecchio leader a pescare)");
        }
        else {
            game.nextCard();
        }
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForDrawCard(this, username);
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
