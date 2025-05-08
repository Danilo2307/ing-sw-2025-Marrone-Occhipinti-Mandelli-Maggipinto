package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;

/** event triggered when the user wants to add the tile currently in hand at ship[x][y] */
public record AddTile(int x, int y) implements Action {

    public void handle(String username) {
        Game game = Game.getInstance();
        Player p = game.getPlayerFromNickname(username);
        p.addTile(x, y);
        // Gestisco l'aggiornamento automatico della ship nella tui a seguito del fissaggio delle tile chiamando
        // l'handle dell'azione RequestShip in modo da simulare (QUASI) una richiesta dell'utente
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