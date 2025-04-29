package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.Game.Game;

/** event triggered when the user wants to remove the tile at ship[x][y] .
 * ServerActionHandler will call getPlayerFromNickname().getBoard().delete(x,y) */
public record RemoveTile(int x, int y) implements Action {

    public void handle(String username) {
        Game game = Game.getInstance();
        Player p = game.getPlayerFromNickname(username);
        p.getTruck().delete(x,y);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRemoveTile(this, username);
    }

}
