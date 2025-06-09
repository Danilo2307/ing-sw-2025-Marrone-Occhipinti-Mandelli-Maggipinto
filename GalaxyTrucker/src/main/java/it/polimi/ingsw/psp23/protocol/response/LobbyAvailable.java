package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.ArrayList;
import java.util.List;


public record LobbyAvailable(List<List<Integer>> matchesAvailable) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showAvailableLobbies(matchesAvailable);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForLobbyAvailable(this, viewAPI);
    }
}
