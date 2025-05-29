package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.exceptions.InvalidComponentActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.TileResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Event triggered when the user wants to add crew to a housing unit located at ship[x][y].
 * The crew can be either an alien or a number of astronauts, depending on the `alien` flag.
 * If `alien` is true, the specified alien color is placed at that location.
 * Otherwise, 2 astronauts are added (as defined in the rules).
 * The server will process this by accessing the housing unit at the specified coordinates
 * and modifying its internal state accordingly.
 * Note: the Color enum used here must be serializable (which is true by default in Java).
 */
public record SetCrew(int x, int y, boolean alien, Color color) implements Action {

    private static List<DirectMessage> dm = new ArrayList<>();
    private static List<BroadcastMessage> bm = new ArrayList<>();

    public void handle(String username) {
        dm.clear();
        bm.clear();
        Game game = Game.getInstance();
        if(game.getGameStatus() != GameStatus.SECOND_COMBATZONE && game.getGameStatus() != GameStatus.END_SLAVERS && game.getGameStatus() != GameStatus.SetCrew){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Player p = game.getPlayerFromNickname(username);
        Board truck = p.getTruck();
        Component tile = truck.getTile(x, y);
        int index = truck.getHousingUnits().indexOf(tile);
        HousingUnit housingUnit = truck.getHousingUnits().get(index);

        if (alien) {
            if (!truck.containsSameAlien(color)) {
                if (housingUnit.canContainAlien(color))
                    housingUnit.setAlien(color);
                else
                    throw new InvalidComponentActionException("Error: non puoi inserire un alieno di colore "+color+ " nella cabina "+x+" "+y);
            }
            else {
                throw new InvalidComponentActionException("Error: esiste già un alieno di quel colore nella tua nave!");
            }
        }
        else {
            housingUnit.setAstronaut();
        }
        // Server.getInstance().sendMessage(username, new DirectMessage(new TileResponse(tile)));
        dm.add(new DirectMessage(new TileResponse(tile)));
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForSetCrew(this, username);
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
