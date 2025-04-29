package it.polimi.ingsw.psp23.protocol.request;


import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.Game.Game;

/**
 * Event triggered when the user wants to release the tile currently held in hand.
 * After drawing a tile (e.g., from the heap or from the uncovered components), the server stores it
 * in the player's internal field (currentTileInHand).
 * When the user types a command like "rilascia", the client sends this event to indicate
 * that they want to discard the tile in hand.
 * The server will verify that the player is actually holding a tile and, if so,
 * remove it from their hand and place it back into the uncovered area.
 */
public record ReleaseTile() implements Action {

    public void handle(String username) {
        Game game = Game.getInstance();
        Player p = game.getPlayerFromNickname(username);
        p.discardComponent();
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForReleaseTile(this, username);
    }

}
