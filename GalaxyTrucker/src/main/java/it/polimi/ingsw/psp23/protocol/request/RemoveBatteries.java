package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;

import java.util.List;

/** event triggered when the user wants to remove the tile at ship[x][y] .
 * ServerActionHandler will call getPlayerFromNickname().getBoard().delete(x,y) */
public record RemoveBatteries(int x, int y, int num) implements Action {

    public void handle(String username) {
        Game game = Game.getInstance();
        if(game.getGameStatus() != GameStatus.SECOND_COMBATZONE && game.getGameStatus() != GameStatus.END_SMUGGLERS){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Player p = game.getPlayerFromNickname(username);
        Board board = p.getTruck();
        board.reduceBatteries(x, y, num);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRemoveBatteries(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

    public List<DirectMessage> getDm(){
        return null;
    }

    public List<BroadcastMessage> getBm(){
        return null;
    }

}