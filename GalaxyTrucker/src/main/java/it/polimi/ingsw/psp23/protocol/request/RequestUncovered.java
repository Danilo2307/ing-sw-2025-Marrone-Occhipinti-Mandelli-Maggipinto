package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.UncoveredListResponse;

public record RequestUncovered() implements Action {

    /**
     * Handles the processing of a request to view the uncovered list of tiles.
     * It retrieves the game associated with the specified username, constructs a response
     * with the uncovered items and their version, and sends the response as a direct message.
     *
     * @param username the username of the user requesting the uncovered list
     */
    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        DirectMessage dm = new DirectMessage(new UncoveredListResponse(game.getUncovered(), game.getLastUncoveredVersion()));
        Server.getInstance().sendMessage(username, dm);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRequestUncovered(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
