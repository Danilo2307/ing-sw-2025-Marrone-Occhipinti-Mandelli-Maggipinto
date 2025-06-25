package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.visitor.RemoveBatteriesVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;

/** event triggered when the user wants to remove the tile at ship[x][y] .
 * ServerActionHandler will call getPlayerFromNickname().getBoard().delete(x,y) */
public record RemoveBatteries(int x, int y, int num) implements Action {


    /**
     * Handles the removal of a tile from the player's board at the specified coordinates in the game.
     * The action is executed if the game status allows it. Otherwise, an exception is thrown.
     *
     * @param username the username of the player attempting to perform the action
     * @throws InvalidActionException if the current game status does not permit this action
     */
    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.SECOND_COMBATZONE && game.getGameStatus() != GameStatus.END_SMUGGLERS){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Player p = game.getPlayerFromNickname(username);
        Board board = p.getTruck();
        game.getCurrentCard().call(new RemoveBatteriesVisitor(), username, x, y, num);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRemoveBatteries(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}