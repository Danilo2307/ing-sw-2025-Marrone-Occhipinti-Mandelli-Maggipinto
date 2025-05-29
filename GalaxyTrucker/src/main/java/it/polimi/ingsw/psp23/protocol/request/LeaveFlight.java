package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Events.TurnOf;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.PassVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.MatchFinished;
import it.polimi.ingsw.psp23.protocol.response.StartTurn;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

import java.util.ArrayList;
import java.util.List;

public record LeaveFlight() implements Action {

    private static List<DirectMessage> dm = new ArrayList<>();
    private static List<BroadcastMessage> bm = new ArrayList<>();

    public void handle(String username){
        dm.clear();
        bm.clear();
        Game game = Game.getInstance();
        if(game.getGameStatus() != GameStatus.WAITING_FOR_NEW_CARD && game.getGameStatus() != GameStatus.SetCrew && game.getGameStatus() != GameStatus.CheckBoards && game.getGameStatus() != GameStatus.Building) {
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        try {
            game.getCurrentPlayer().setInGame(false);
        }
        // se entra nel catch vuol dire che non c'è un current player e quindi siamo all'inizio della partita prima di giocare la prima carta
        catch (NullPointerException e) {
            game.getPlayers().getFirst().setInGame(false);
        }
        // Server.getInstance().sendMessage(username, new DirectMessage(new MatchFinished("Hai abbandonato il volo, attendi che finiscano anche gli altri giocatori\n")));
        dm.add(new DirectMessage(new MatchFinished("Hai abbandonato il volo, attendi che finiscano anche gli altri giocatori\n")));
        //for(Player player : game.getPlayers()){
            // Server.getInstance().sendMessage(player.getNickname(), new DirectMessage(new StringResponse(username + " ha abbandonato il volo, vi reincontrerete nella fase finale del calcolo del punteggio\n")));
            bm.add(new BroadcastMessage(new StringResponse(username + " ha abbandonato il volo, vi reincontrerete nella fase finale del calcolo del punteggio\n")));
        //}
        /*Card currentCard = game.getCurrentCard();
        PassVisitor nextTurn = new PassVisitor();
        currentCard.call(nextTurn, username);
        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StartTurn(game.getCurrentPlayer().getNickname())));*/
        game.sortPlayersByPosition();
        game.getNextPlayer();
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForLeaveFlight(this, username);
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