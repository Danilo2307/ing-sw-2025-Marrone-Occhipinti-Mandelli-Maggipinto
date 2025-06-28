package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.exceptions.NoTileException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.Server;
import it.polimi.ingsw.psp23.protocol.response.ErrorResponse;
import it.polimi.ingsw.psp23.protocol.response.TileResponse;
import it.polimi.ingsw.psp23.protocol.response.UncoveredListResponse;

import java.util.ArrayList;

/** Event triggered when the user wants to draw a face-up component at position x. */
public record DrawFromFaceUp (int x, int version) implements Action {

    /**
     * Handles the action when a user attempts to draw a face-up component from the game.
     * This method retrieves the game and player information based on the username,
     * processes the drawing of a component, and sends appropriate responses back to the user.
     *
     * @param username the username of the player performing the action
     */
    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Player p = game.getPlayerFromNickname(username);
        try {
            Component drawn = p.chooseCardUncovered(x, version);
            DirectMessage dm = new DirectMessage(new TileResponse(drawn));
            Server.getInstance().sendMessage(username, dm);
        }
        catch (NoTileException | IndexOutOfBoundsException exception) {
            DirectMessage dm = new DirectMessage(new ErrorResponse(exception.getMessage()));
            Server.getInstance().sendMessage(username, dm);
        }
        finally {
            ArrayList<Component> uncovered = game.getUncovered();
            int updatedVersion = game.getLastUncoveredVersion();
            DirectMessage dm1 = new DirectMessage(new UncoveredListResponse(uncovered, updatedVersion));
            Server.getInstance().sendMessage(username, dm1);
        }
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForDrawFromFaceUp(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}