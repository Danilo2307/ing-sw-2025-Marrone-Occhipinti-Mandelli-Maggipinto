package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.Server;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.protocol.response.TileResponse;

import java.util.List;

public record TakeReservedTile(int index) implements Action{

    /**
     * Handles the action of a player taking a reserved tile based on the provided index.
     *
     * @param username the username of the player performing the action
     */
    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Player p = game.getPlayerFromNickname(username);
        int trueIndex = index;

        if (p.getTruck().getReservedTiles().size() == 1 && index == 1) {
            trueIndex = 0;
        }
        p.takeReservedTile(trueIndex);
        Server.getInstance().sendMessage(username,new DirectMessage(new TileResponse(p.getCurrentTileInHand())));
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForTakeReservedTile(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
