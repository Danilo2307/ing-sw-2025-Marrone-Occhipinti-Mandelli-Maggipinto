package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.protocol.response.FlightBoardResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record RequestFlightBoard() implements Action{

    private static List<DirectMessage> dm = new ArrayList<>();
    private static List<BroadcastMessage> bm = new ArrayList<>();

    public void handle(String username) {
        dm.clear();
        bm.clear();
        Game game = Game.getInstance();
        Map<Color, Integer> flightMap = new HashMap();
        for (Player player : game.getPlayers()) {
            flightMap.put(player.getColor(), player.getPosition());
        }
        DirectMessage directMessage = new DirectMessage(new FlightBoardResponse(flightMap));
        dm.add(directMessage);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRequestFlightBoard(this, username);
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
