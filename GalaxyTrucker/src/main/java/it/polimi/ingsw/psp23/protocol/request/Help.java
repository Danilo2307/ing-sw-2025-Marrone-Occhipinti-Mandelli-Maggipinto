package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.visitor.HelpVisitor;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

public record Help() implements Action {


    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Card currentCard = game.getCurrentCard();
        HelpVisitor help = new HelpVisitor();
        Server.getInstance().sendMessage(username, new DirectMessage(new StringResponse(currentCard.call(help, username))));
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForHelp(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
