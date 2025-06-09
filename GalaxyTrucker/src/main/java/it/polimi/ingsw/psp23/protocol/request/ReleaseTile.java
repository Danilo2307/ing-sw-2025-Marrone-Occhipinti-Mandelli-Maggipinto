package it.polimi.ingsw.psp23.protocol.request;


import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Event triggered when the user wants to release the tile currently held in hand.
 * After drawing a tile (e.g., from the heap or from the uncovered components), the server stores it
 * in the player's internal field (currentTileInHand).
 * When the user types a command like "rilascia", the client sends this event to indicate
 * that they want to discard the tile in hand.
 * The server will verify that the player is actually holding a tile and, if so,
 * remove it from their hand and place it back into the uncovered area.
 */
public record ReleaseTile() implements Action {

    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Player p = game.getPlayerFromNickname(username);
        p.discardComponent();
        Server.getInstance().sendMessage(username, new DirectMessage(new StringResponse("Hai scartato la tile\n")));
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForReleaseTile(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }
}
