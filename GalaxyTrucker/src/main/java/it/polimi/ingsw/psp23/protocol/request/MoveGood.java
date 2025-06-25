package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;

import java.util.List;

public record MoveGood(int fromX, int fromY, int index, int toX, int toY) implements Action {


    /**
     * Handles the action of moving a good from one container on the ship to another, performing necessary
     * validations to ensure the action adheres to the game's current state and rules.
     *
     * @param username the username of the player attempting to perform the action
     * @throws InvalidActionException if the game's current status does not permit this action
     */
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.END_ABANDONEDSTATION && game.getGameStatus() != GameStatus.END_PLANETS && game.getGameStatus() != GameStatus.END_SMUGGLERS){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Board board = UsersConnected.getInstance().getGameFromUsername(username).getPlayerFromNickname(username).getTruck();
        Component[][] ship = board.getShip();
        int containerIndex = board.getContainers().indexOf(ship[fromX][fromY]);
        Item item = board.getContainers().get(containerIndex).getItems().get(index);
        board.removeGood(fromX,fromY , index);
        board.loadGoods(item, toX, toY);

    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForMoveGood(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}