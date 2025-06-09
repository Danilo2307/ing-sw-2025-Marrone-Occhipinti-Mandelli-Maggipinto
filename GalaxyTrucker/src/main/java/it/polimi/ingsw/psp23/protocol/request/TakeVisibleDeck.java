package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;
import it.polimi.ingsw.psp23.protocol.response.VisibleDeckResponse;

import java.util.ArrayList;
import java.util.List;

public record TakeVisibleDeck(int index) implements Action{


    public void handle(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Player player = game.getPlayerFromNickname(username);
        ArrayList<Card> deckChosen = switch(index) {
            case 1 -> game.getVisibleDeck1(player);
            case 2 -> game.getVisibleDeck2(player);
            case 3 -> game.getVisibleDeck3(player);
            default -> throw new InvalidActionException("Valore inaspettato: " + index);
        };

        if (deckChosen == null) {
            DirectMessage dm = new DirectMessage(new StringResponse("Non puoi guardare il mazzetto "+index+" in questo momento\n"));
            Server.getInstance().sendMessage(username, dm);
        }
        else {
            StringBuilder descriptions = new StringBuilder();
            ArrayList<Integer> ids = new ArrayList<>();
            for (Card card : deckChosen) {
                descriptions.append(card.toString()).append("\n\n");
                ids.add(card.getId());
            }
            DirectMessage m = new DirectMessage(new VisibleDeckResponse(ids, descriptions.toString()));
            Server.getInstance().sendMessage(username, m);
        }
    }


    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForTakeVisibleDeck(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
