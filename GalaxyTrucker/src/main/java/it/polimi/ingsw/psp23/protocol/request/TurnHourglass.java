package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.controller.Controller;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;

import java.util.List;

/** event triggered when a user wants to turn the hourglass during the building phase */
public record TurnHourglass() implements Action {

    /**
     * Handles the action of starting the hoursglass countdown timer for the specified user's game.
     * This method retrieves the game associated with the provided username and interacts
     * with its controller to initiate the timer.
     *
     * @param username The username of the player whose game timer should be started.
     */
    public void handle(String username){
        UsersConnected.getInstance().getGameFromUsername(username).getController().startTimer();
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForTurnHourGlass(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}