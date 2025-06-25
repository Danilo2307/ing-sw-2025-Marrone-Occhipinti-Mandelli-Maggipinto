package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.exceptions.LevelException;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.exceptions.InvalidComponentActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.network.socket.Users;
import it.polimi.ingsw.psp23.protocol.response.TileResponse;

import java.util.ArrayList;
import java.util.List;


public record SetCrew(int x, int y, boolean alien, Color color) implements Action {


    /**
     * Handles the action of adding crew to a housing unit at the specified coordinates within a player's ship.
     * The crew can be either an alien or astronauts, determined by the `alien` flag. The method performs several
     * validation checks before applying the action. If the conditions are not met, an appropriate exception is thrown.
     *
     * @param username the username of the player performing the action
     */
    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.SECOND_COMBATZONE && game.getGameStatus() != GameStatus.END_SLAVERS && game.getGameStatus() != GameStatus.SetCrew){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Player p = game.getPlayerFromNickname(username);
        Board truck = p.getTruck();
        Component tile = truck.getTile(x, y);
        if (tile == null)
            throw new InvalidActionException("Hai selezionato una tile vuota! Riprova");

        int index = truck.getHousingUnits().indexOf(tile);
        if (index == -1)
            throw new InvalidActionException("La tile considerata non è una cabina! Riprova");

        HousingUnit housingUnit = truck.getHousingUnits().get(index);

        if (alien) {
            if (!truck.containsSameAlien(color)) {
                if (UsersConnected.getInstance().getGameFromUsername(username).getLevel() == 0) {
                    throw new LevelException("Non puoi aggiungere alieni nel volo di prova!");
                }
                else if (housingUnit.canContainAlien(color))
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
        Server.getInstance().sendMessage(username, new DirectMessage(new TileResponse(tile)));
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForSetCrew(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
