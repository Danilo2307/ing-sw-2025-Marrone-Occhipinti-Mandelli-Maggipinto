package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.exceptions.NoTileException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.ErrorResponse;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;
import it.polimi.ingsw.psp23.protocol.response.TileResponse;

/** Event triggered when the user wants to draw a face-up component at position x. */
public record DrawFromFaceUp (int x, int version) implements Action {

    public void handle(String username) {
        Game game = Game.getInstance();
        Player p = game.getPlayerFromNickname(username);
        try {
            Component drawn = p.chooseCardUncovered(x, version);
            DirectMessage dm = new DirectMessage(new TileResponse(drawn));
            Server.getInstance().sendMessage(username, dm);
        }
        catch (NoTileException | IndexOutOfBoundsException exception) {
            DirectMessage dm = new DirectMessage(new ErrorResponse(exception.getMessage()));
            Server.getInstance().sendMessage(username, dm);
        }
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForDrawFromFaceUp(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}