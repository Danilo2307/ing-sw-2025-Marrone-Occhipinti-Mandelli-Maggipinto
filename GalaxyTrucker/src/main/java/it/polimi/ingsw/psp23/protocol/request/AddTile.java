package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.protocol.response.Event;

import java.util.ArrayList;
import java.util.List;

/** event triggered when the user wants to add the tile currently in hand at ship[x][y] */
public record AddTile(int x, int y) implements Action {

    private static List<DirectMessage> dm = new ArrayList<>();
    private static List<BroadcastMessage> bm = new ArrayList<>();

    public void handle(String username) {
        dm.clear();
        bm.clear();
        Game game = Game.getInstance();
        Player p = game.getPlayerFromNickname(username);
        p.addTile(x, y);
        // Gestisco l'aggiornamento automatico della ship nella tui a seguito del fissaggio delle tile chiamando
        // l'handle dell'azione RequestShip in modo da simulare (QUASI) una richiesta dell'utente
        RequestShip e = new RequestShip(username);
        e.handle(username);
        dm.addAll(e.getDm());
        bm.addAll(e.getBm());
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForAddTile(this, username);
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