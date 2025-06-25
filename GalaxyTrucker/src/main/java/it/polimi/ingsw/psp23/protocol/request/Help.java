package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.visitor.HelpVisitor;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

public record Help() implements Action {


    /**
     * Handles the help action for the specified user. This involves retrieving the game associated
     * with the user, obtaining the current card, and processing the card with a visitor to generate
     * a response containing the which is then sent back to the user.
     *
     * @param username the username of the user for whom the help action is being handled
     */
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
