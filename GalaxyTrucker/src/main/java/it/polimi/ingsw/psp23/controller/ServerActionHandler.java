package it.polimi.ingsw.psp23.controller;

import it.polimi.ingsw.psp23.exceptions.GameException;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.request.*;
import it.polimi.ingsw.psp23.protocol.response.ErrorResponse;


public class ServerActionHandler {

    String username;

    public ServerActionHandler(String username) {
        this.username = username;
    }

    public void handleAction(Action a) {

        try {
            a.call(new HandleActionVisitor(), username);
        }
        /// TODO: raccolgo eccezioni lanciate dalla call
        // Catch all game-related exceptions triggered by invalid player actions.
        // These are not recoverable errors but rule violations (e.g., wrong component state or illegal move).
        // The server sends an error message back to the client to notify them, without stopping the game flow.
        catch(GameException e) {
            DirectMessage dm = new DirectMessage(new ErrorResponse(e.getMessage()));
            Server.getInstance().sendMessage(username, dm);
        }

    }
}
