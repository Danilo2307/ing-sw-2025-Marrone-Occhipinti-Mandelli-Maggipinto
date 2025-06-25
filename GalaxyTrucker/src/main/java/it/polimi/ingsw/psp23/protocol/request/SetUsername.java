package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.network.UsersConnected;


public record SetUsername(String username) implements Action {

    /**
     * Handles the action of adding a player to the game associated with the specified username.
     * This method retrieves the game using the given username and invokes the game controller
     * to add the player to the game.
     *
     * @param username the username of the player to be added to the game
     */
    public void handle(String username){
        UsersConnected.getInstance().getGameFromUsername(username).getController().addPlayerToGame(username);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForSetUsername(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return actionVisitorSinglePar.visitForSetUsername(this);
    }

    @Override
    public String toString() {
        return username;
    }

}
