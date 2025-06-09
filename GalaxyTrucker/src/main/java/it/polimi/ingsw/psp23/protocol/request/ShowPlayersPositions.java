package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

import java.util.ArrayList;
import java.util.List;

public record ShowPlayersPositions() implements Action{


    public void handle(String username) {
        StringBuilder sb = new StringBuilder();
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        for (Player player : game.getPlayers()) {
            sb.append(player.getNickname() + ": " + player.getPosition()).append("\n");
        }
        DirectMessage dm = new DirectMessage(new StringResponse(sb.toString()));
        Server.getInstance().sendMessage(username, dm);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForShowPlayersPositions(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar) {
        return null;
    }

}
