package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.network.UsersConnected;


public record Finished() implements Action {

    /**
     * Handles the action sent by the user to notify the server that the crew positioning is complete.
     *
     * @param username the username of the player who has completed positioning their crew
     */
    public void handle(String username){
        UsersConnected.getInstance().getGameFromUsername(username).getController().crewPositioned(username);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForFinished(this, username);
    }

    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
