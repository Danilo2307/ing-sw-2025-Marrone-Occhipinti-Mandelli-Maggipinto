package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.TileResponse;

import java.util.ArrayList;
import java.util.List;

/** event triggered when the user wants to draw a component from the heap*/
public record DrawFromHeap() implements Action {

    private static List<DirectMessage> dm = new ArrayList<>();
    private static List<BroadcastMessage> bm = new ArrayList<>();

    public void handle(String username) {
        dm.clear();
        bm.clear();
        Game game = Game.getInstance();
        Player p = game.getPlayerFromNickname(username);
        Component drawn = p.chooseTileFromHeap();
        DirectMessage m = new DirectMessage(new TileResponse(drawn));
        // Server.getInstance().sendMessage(username, dm);
        dm.add(m);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForDrawFromHeap(this, username);
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
