package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.UsersConnected;


/** event triggered when the user wants to add the tile currently in hand at ship[x][y] */
public record AddTile(int x, int y) implements Action {

    /**
     * Handles the event of adding a tile to the ship. This method retrieves the game
     * and player by username, processes the tile addition at the corresponding coordinates,
     * and triggers an automatic update for the ship.
     *
     * @param username the username of the player initiating the action
     */
    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Player p = game.getPlayerFromNickname(username);
        p.addTile(x, y);
        // Gestisco l'aggiornamento automatico della ship nella view a seguito del fissaggio delle tile chiamando
        // l'handle dell'azione RequestShip in modo da simulare una richiesta dell'utente
        new RequestShip(username).handle(username);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForAddTile(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}