package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

import java.util.ArrayList;
import java.util.List;

public record TakeVisibleDeck(int index) implements Action{

    private static List<DirectMessage> dm = new ArrayList<>();
    private static List<BroadcastMessage> bm = new ArrayList<>();

    public void handle(String username) {
        dm.clear();
        bm.clear();
        Game game = Game.getInstance();
        Player player = game.getPlayerFromNickname(username);
        ArrayList<Card> deckChosen = switch(index) {
            case 1 -> game.getVisibleDeck1(player);
            case 2 -> game.getVisibleDeck2(player);
            case 3 -> game.getVisibleDeck3(player);
            default -> throw new InvalidActionException("Valore inaspettato: " + index);
        };

        if (deckChosen == null) {
            DirectMessage m = new DirectMessage(new StringResponse("Non puoi guardare il mazzetto "+index+" in questo momento\n"));
            // Server.getInstance().sendMessage(username, dm);
            dm.add(m);
        }
        else {
            StringBuilder descriptions = new StringBuilder();
            for (Card card : deckChosen) {
                descriptions.append(card.toString()).append("\n");
            }
            DirectMessage m = new DirectMessage(new StringResponse(descriptions.toString()));
            // Server.getInstance().sendMessage(username, dm);
            dm.add(m);
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

    public List<DirectMessage> getDm() {
        return dm;
    }

    public List<BroadcastMessage> getBm() {
        return bm;
    }

}
