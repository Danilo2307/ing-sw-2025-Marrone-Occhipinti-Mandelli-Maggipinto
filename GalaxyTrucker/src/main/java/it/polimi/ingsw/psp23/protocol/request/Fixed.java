package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.controller.Controller;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;

import java.util.List;

public record Fixed() implements Action {

    /**
     * Handles the action triggered by the user when correcting their ship.
     * It initiates a check process on the user's game board.
     *
     * @param username the username of the player performing the action. This is used to retrieve the associated game
     *                 and trigger the board check process via the game controller.
     */
    public void handle(String username){
        UsersConnected.getInstance().getGameFromUsername(username).getController().startCheckBoard();
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForFixed(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
