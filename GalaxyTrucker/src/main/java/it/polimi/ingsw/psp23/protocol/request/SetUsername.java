package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.PlayerExistsException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

public record SetUsername(String username) implements Action {

    public void handle(String username){
        Game game = Game.getInstance();
        try {
            game.addPlayer(username);
        }
        catch(PlayerExistsException e){
            /// TODO: manda indietro al client...però va gestito...come???
            DirectMessage dm = new DirectMessage(new StringResponse(e.getMessage()));
            Server.getInstance().sendMessage(username, dm);
        }
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForSetUsername(this, username);
    }

}
