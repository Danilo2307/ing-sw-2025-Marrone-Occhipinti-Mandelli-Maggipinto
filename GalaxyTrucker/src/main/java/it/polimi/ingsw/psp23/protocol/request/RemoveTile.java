package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;

import java.util.ArrayList;
import java.util.List;

/** event triggered when the user wants to remove the tile at ship[x][y] .
 * ServerActionHandler will call getPlayerFromNickname().getBoard().delete(x,y) */
public record RemoveTile(int x, int y) implements Action {


    /**
     * Handles the removal of a specific tile from the player's truck and triggers
     * an automatic update for the ship view by simulating a user request.
     *
     * @param username the username of the player for whom the tile removal action is being performed
     */
    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Player p = game.getPlayerFromNickname(username);
        p.getTruck().delete(x,y);
        // Gestisco l'aggiornamento automatico della ship nella view a seguito del fissaggio delle tile chiamando
        // l'handle dell'azione RequestShip in modo da simulare una richiesta dell'utente
        new RequestShip(username).handle(username);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRemoveTile(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
