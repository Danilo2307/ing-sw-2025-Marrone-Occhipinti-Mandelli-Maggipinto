package it.polimi.ingsw.psp23.controller;

import it.polimi.ingsw.psp23.exceptions.GameException;
import it.polimi.ingsw.psp23.exceptions.InvalidCoordinatesException;
import it.polimi.ingsw.psp23.exceptions.LobbyUnavailableException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.request.*;
import it.polimi.ingsw.psp23.protocol.response.ErrorResponse;
import it.polimi.ingsw.psp23.protocol.response.IncorrectWelding;

import java.util.List;


/**
 * The ServerActionHandler class processes player actions received by the server.
 * It provides the functionality to handle, validate, and respond to game actions
 * initiated by a user represented by their unique username.
 *
 * Actions are executed using the designated ActionVisitor pattern to ensure proper handling
 * of various action types, while sending appropriate responses or error messages
 * back to the client in case of rule violations or exceptions.
 */
public class ServerActionHandler {

    String username;

    public ServerActionHandler(String username) {
        this.username = username;
    }

    /**
     * Handles a provided game action initiated by a player.
     * The method invokes the action using the provided visitor pattern,
     * and processes any exceptions occurring due to invalid actions.
     * Invalid actions (e.g., rule violations or incorrect input) will result
     * in error messages sent back to the client without disrupting game flow.
     *
     * @param a the action object representing the player's requested operation
     */
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
        } catch (InvalidCoordinatesException invalidCoordinatesException) {
            DirectMessage dm = new DirectMessage(new ErrorResponse(invalidCoordinatesException.getMessage()));
            Server.getInstance().sendMessage(username, dm);
            DirectMessage dm1 = new DirectMessage(new IncorrectWelding());
            Server.getInstance().sendMessage(username, dm1);
        }

    }
}
