package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.exceptions.NoTileException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.ErrorResponse;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;
import it.polimi.ingsw.psp23.protocol.response.TileResponse;
import it.polimi.ingsw.psp23.protocol.response.UncoveredListResponse;

import java.util.ArrayList;
import java.util.List;

/** Event triggered when the user wants to draw a face-up component at position x. */
public record DrawFromFaceUp (int x, int version) implements Action {

    private static List<DirectMessage> dm = new ArrayList<>();
    private static List<BroadcastMessage> bm = new ArrayList<>();

    public void handle(String username) {
        dm.clear();
        bm.clear();
        Game game = Game.getInstance();
        Player p = game.getPlayerFromNickname(username);
        try {
            Component drawn = p.chooseCardUncovered(x, version);
            DirectMessage m = new DirectMessage(new TileResponse(drawn));
            // Server.getInstance().sendMessage(username, dm);
            dm.add(m);
        }
        catch (NoTileException | IndexOutOfBoundsException exception) {
            DirectMessage m = new DirectMessage(new ErrorResponse(exception.getMessage()));
            // Server.getInstance().sendMessage(username, dm);
            dm.add(m);
        }
        finally {
            ArrayList<Component> uncovered = game.getUncovered();
            int updatedVersion = game.getLastUncoveredVersion();
            DirectMessage m1 = new DirectMessage(new UncoveredListResponse(uncovered, updatedVersion));
            // Server.getInstance().sendMessage(username, dm1);
            dm.add(m1);
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

    public List<DirectMessage> getDm() {
        return dm;
    }

    public List<BroadcastMessage> getBm() {
        return bm;
    }
}