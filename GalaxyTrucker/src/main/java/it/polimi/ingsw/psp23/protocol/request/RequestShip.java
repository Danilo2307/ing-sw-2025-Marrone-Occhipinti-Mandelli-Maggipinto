package it.polimi.ingsw.psp23.protocol.request;


import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.ShipResponse;



public record RequestShip(String nickname) implements Action {

    /**
     * Handles the "RequestShip" event triggered by a client.
     * This method retrieves the ship layout associated with a player and sends it back to the client.
     *
     * @param username The username of the client requesting their ship layout.
     */
    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Player p = game.getPlayerFromNickname(nickname);
        Component[][] ship = p.getTruck().getShip();
        DirectMessage dm = new DirectMessage(new ShipResponse(ship, nickname));
        Server.getInstance().sendMessage(username, dm);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRequestShip(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }
}
