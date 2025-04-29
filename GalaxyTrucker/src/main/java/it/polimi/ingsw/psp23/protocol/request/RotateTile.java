package it.polimi.ingsw.psp23.protocol.request;


import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.Game.Game;

/** the user wants to rotate (senso orario di 90 gradi) the tile currently in hand*/
public record RotateTile() implements Action {

    public void handle(String username) {
        Game game = Game.getInstance();
        Player p = game.getPlayerFromNickname(username);
        p.rotateTileInHand();
    }
}
