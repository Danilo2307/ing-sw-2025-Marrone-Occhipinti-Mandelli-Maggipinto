package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.ActiveEngineVisitor;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

import java.util.ArrayList;
import java.util.List;

public record ActivateEngine(int ex, int ey, int bx, int by) implements Action {

    private static List<DirectMessage> dm = new ArrayList<>();
    private static List<BroadcastMessage> bm = new ArrayList<>();

    public void handle(String username){
        Game game = Game.getInstance();
        Board truck = game.getCurrentPlayer().getTruck();
        Component[][] nave = truck.getShip();
        int engineIndex = truck.getEngines().indexOf(nave[ex][ey]);
        if(game.getGameStatus() != GameStatus.SECOND_COMBATZONE && game.getGameStatus() != GameStatus.THIRD_COMBATZONE && game.getGameStatus() != GameStatus.INIT_OPENSPACE){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Card currentCard = game.getCurrentCard();
        if(engineIndex == -1){
            // Server.getInstance().sendMessage(username, new DirectMessage(new StringResponse("Non hai selezionato un motore\n")));
            dm.add(new DirectMessage(new StringResponse("Non hai selezionato un motore\n")));
        }
        else {
            if (truck.getEngines().get(engineIndex).isActive()) {
                // Server.getInstance().sendMessage(username, new DirectMessage(new StringResponse("Il motore è già stato attivato!\n")));
                dm.add(new DirectMessage(new StringResponse("Il motore è già stato attivato!\n")));
            } else {
                ActiveEngineVisitor activeEngine = new ActiveEngineVisitor();
                currentCard.call(activeEngine, username, ex, ey);
                game.getPlayerFromNickname(username).getTruck().reduceBatteries(bx, by, 1);
            }
        }
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForActivateEngine(this, username);
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

