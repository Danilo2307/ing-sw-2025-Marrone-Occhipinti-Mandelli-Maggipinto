package it.polimi.ingsw.psp23.protocol.request;


import it.polimi.ingsw.psp23.network.UsersConnected;


public record Put() implements Action {

    /**
     * Handles the action when the user has finished building their ship and places their piece on the board.
     * This method interacts with the game controller to finalize the building phase for the specific user
     * and determine the player's placement.
     *
     * @param username The username of the player who has completed the building phase and is placing their piece.
     */
    public void handle(String username){
        UsersConnected.getInstance().getGameFromUsername(username).getController().playerFinishedBuilding(username);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForPut(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
