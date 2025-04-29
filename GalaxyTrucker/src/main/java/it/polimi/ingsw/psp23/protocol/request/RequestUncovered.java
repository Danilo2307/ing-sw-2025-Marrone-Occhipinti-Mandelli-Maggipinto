package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.UncoveredListResponse;

public record RequestUncovered() implements Action {

    public void handle(String username) {
        Game game = Game.getInstance();
        DirectMessage dm = new DirectMessage(new UncoveredListResponse(game.getUncovered(), game.getLastUncoveredVersion()));
        Server.getInstance().sendMessage(username, dm);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRequestUncovered(this, username);
    }

}
