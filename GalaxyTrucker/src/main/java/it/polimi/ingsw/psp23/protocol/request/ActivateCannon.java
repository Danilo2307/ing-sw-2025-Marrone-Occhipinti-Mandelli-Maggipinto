package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.visitor.ActiveCannonVisitor;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

/**
 * Event triggered when the user wants to activate a double cannon during the action phase.
 * The client specifies the position of the cannon to activate (cx, cy) and the battery hub (bx, by)
 * from which one battery will be consumed.
 * */
public record ActivateCannon(int cx, int cy, int bx, int by) implements Action {

    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Board truck = game.getCurrentPlayer().getTruck();
        Component[][] nave = truck.getShip();
        int cannonIndex = truck.getCannons().indexOf(nave[cx][cy]);
        if(game.getGameStatus() != GameStatus.FIRST_COMBATZONE && game.getGameStatus() != GameStatus.THIRD_COMBATZONE && game.getGameStatus() != GameStatus.INIT_METEORSWARM && game.getGameStatus() != GameStatus.INIT_PIRATES && game.getGameStatus() != GameStatus.INIT_SLAVERS && game.getGameStatus() != GameStatus.INIT_SMUGGLERS){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Card currentCard = game.getCurrentCard();
        if(cannonIndex == -1){
            Server.getInstance().sendMessage(username, new DirectMessage(new StringResponse("Non hai selezionato un cannone\n")));
        }
        else {
            if (truck.getCannons().get(cannonIndex).isActive()) {
                Server.getInstance().sendMessage(username, new DirectMessage(new StringResponse("Il cannone è già stato attivato!\n")));
            } else {
                ActiveCannonVisitor activeCannon = new ActiveCannonVisitor();
                currentCard.call(activeCannon, username, cx, cy);
                game.getPlayerFromNickname(username).getTruck().reduceBatteries(bx, by, 1);
            }
        }
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForActivateCannon(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
