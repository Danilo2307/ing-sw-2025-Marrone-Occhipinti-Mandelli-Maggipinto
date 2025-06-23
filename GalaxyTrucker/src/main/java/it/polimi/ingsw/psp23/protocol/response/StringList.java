package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.List;

public record StringList(List<String> players) implements Event {

        public void handle(ViewAPI viewAPI) {
            viewAPI.savePlayersNames(players);
        }

        @Override
        public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
            return eventVisitor.visitForStringList(this, viewAPI);
        }


}
