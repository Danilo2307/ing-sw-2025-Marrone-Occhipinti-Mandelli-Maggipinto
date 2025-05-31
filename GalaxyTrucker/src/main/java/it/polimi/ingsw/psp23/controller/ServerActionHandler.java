package it.polimi.ingsw.psp23.controller;

import it.polimi.ingsw.psp23.exceptions.GameException;
import it.polimi.ingsw.psp23.exceptions.InvalidCoordinatesException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.request.*;
import it.polimi.ingsw.psp23.protocol.response.ErrorResponse;
import it.polimi.ingsw.psp23.protocol.response.IncorrectWelding;

import java.util.List;


public class ServerActionHandler {

    String username;

    public ServerActionHandler(String username) {
        this.username = username;
    }

    public void handleAction(Action a) {

        try {

            a.call(new HandleActionVisitor(), username);

            List<DirectMessage> dm = a.getDm();
            List<BroadcastMessage> bm = a.getBm();

            if(dm != null && !dm.isEmpty()){
                for(Message m : dm) {
                    Server.getInstance().sendMessage(username, m);
                }
                dm.clear();
            }
            if(bm != null && !bm.isEmpty()){
                Server server = Server.getInstance();
                for(Message m : bm) {
                    for(Player p : Game.getInstance().getPlayers()) {
                        if(server.getClients().keySet().stream().map(server::getUsernameForConnection).toList().contains(p.getNickname()))
                            Server.getInstance().sendMessage(p.getNickname(), m);
                    }
                }
                bm.clear();
            }
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
