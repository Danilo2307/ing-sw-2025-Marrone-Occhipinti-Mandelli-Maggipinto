package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.socket.ConnectionThread;


public record RegisterNumPlayers(int number) implements Action{

    /**
     * Handles the action of setting the number of players requested for a game associated with the given username.
     * This method updates the game configuration and initiates the server's connection thread if it is not already active.
     *
     * @param username The username of the player whose associated game is being updated with the requested number of players.
     */
    public void handle(String username) {
        UsersConnected.getInstance().getGameFromUsername(username).setNumRequestedPlayers(number);
        if(!ConnectionThread.getInstance().isListening()) {
            ConnectionThread.getInstance().start();
        }
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRegisterNumPlayers(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
