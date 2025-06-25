package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.FlightBoardResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record RequestFlightBoard() implements Action{


    /**
     * Handles the action of requesting the flight board for a specific user.
     * This method retrieves the game instance associated with the given username,
     * extracts the flight board data, and sends it back to the user.
     *
     * @param username the username of the player requesting the flight board
     */
    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Map<Color, Integer> flightMap = new HashMap();
        for (Player player : game.getPlayers()) {
            flightMap.put(player.getColor(), player.getPosition());
        }
        DirectMessage directMessage = new DirectMessage(new FlightBoardResponse(flightMap));
        Server.getInstance().sendMessage(username, directMessage);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRequestFlightBoard(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
