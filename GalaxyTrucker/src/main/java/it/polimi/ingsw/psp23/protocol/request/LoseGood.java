package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;

import java.util.List;

public record LoseGood(int i, int j, int index) implements Action {


    /**
     * Handles the action to remove a specific good from a player's truck.
     * This method ensures that the game is in an appropriate terminal state
     * before performing the action. Loose the index-th good at ship[i][j]
     *
     * @param username the username of the player performing the action
     * @throws InvalidActionException if the action is attempted when the game
     *                                is not in a valid end state
     */
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.END_ABANDONEDSTATION && game.getGameStatus() != GameStatus.END_PLANETS && game.getGameStatus() != GameStatus.END_SMUGGLERS){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        UsersConnected.getInstance().getGameFromUsername(username).getPlayerFromNickname(username).getTruck().removeGood(i, j , index);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForLoseGood(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
