package it.polimi.ingsw.psp23.protocol.request;


import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
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

    private static List<DirectMessage> dm = new ArrayList<>();
    private static List<BroadcastMessage> bm = new ArrayList<>();

    public void handle(String username) {
        dm.clear();
        bm.clear();
        Game game = Game.getInstance();
        Player p = game.getPlayerFromNickname(nickname);
        Component[][] ship = p.getTruck().getShip();
        int[][] validCoordinates = p.getTruck().getValidCoords();
        DirectMessage m = new DirectMessage(new ShipResponse(ship, validCoordinates));
        // Server.getInstance().sendMessage(username, dm);
        dm.add(m);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRequestShip(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

    public List<DirectMessage> getDm() {
        return dm;
    }

    public List<BroadcastMessage> getBm() {
        return bm;
    }
}
