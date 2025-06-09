package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.TileResponse;

import java.util.ArrayList;
import java.util.List;

/** event triggered when the user wants to view the details of a component at ship[x][y] */
public record RequestTileInfo(int x, int y) implements Action {


    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Player p = game.getPlayerFromNickname(username);
        Component target = p.getTruck().getTile(x, y);
        DirectMessage dm = new DirectMessage(new TileResponse(target));
        Server.getInstance().sendMessage(username, dm);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRequestTileInfo(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
