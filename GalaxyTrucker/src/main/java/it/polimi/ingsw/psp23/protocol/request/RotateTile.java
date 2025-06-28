package it.polimi.ingsw.psp23.protocol.request;


import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.Server;
import it.polimi.ingsw.psp23.protocol.response.TileResponse;

/** the user wants to rotate (senso orario di 90 gradi) the tile currently in hand*/
public record RotateTile() implements Action {


    /**
     * Handles the action of rotating the current tile in the player's hand by 90 degrees clockwise.
     *
     * @param username The username of the player whose tile in hand will be rotated.
     */
    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Player p = game.getPlayerFromNickname(username);
        p.rotateTileInHand();
        Server.getInstance().sendMessage(username, new DirectMessage(new TileResponse(p.getCurrentTileInHand())));
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username) {
        return actionVisitor.visitForRotateTile(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar) {
        return null;
    }
}

