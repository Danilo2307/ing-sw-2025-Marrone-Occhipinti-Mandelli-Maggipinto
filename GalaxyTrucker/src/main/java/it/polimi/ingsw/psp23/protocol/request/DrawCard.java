package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.LandOnPlanetVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class DrawCard implements Action{

    public void handle(String username){
        Game game = Game.getInstance();
        if(game.getGameStatus() != GameStatus.WAITING_FOR_NEW_CARD){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        if(!username.equals(game.getPlayers().getFirst().getNickname())){
            throw new InvalidActionException("Solo il leader può eseguire questa azione!");
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
}
