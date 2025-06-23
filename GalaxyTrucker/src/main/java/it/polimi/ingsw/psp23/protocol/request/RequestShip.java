package it.polimi.ingsw.psp23.protocol.request;


import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.ShipResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Event triggered when a client wants to view their current ship layout.
 * This is a request from the client to the server asking to see its ship.
 * The server will handle this by retrieving the ship data from the model
 * and sending back a response event (e.g. ShipStateResponse) containing the full ship matrix.
 * The client will then use this data to display the ship in the TUI.
 */
public record RequestShip(String nickname) implements Action {

    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Player p = game.getPlayerFromNickname(nickname);
        Component[][] ship = p.getTruck().getShip();
        DirectMessage dm = new DirectMessage(new ShipResponse(ship, nickname));
        Server.getInstance().sendMessage(username, dm);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRequestShip(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }
}
