package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.AbstractMap;
import java.util.List;

public record FinalRanking(List<AbstractMap.SimpleEntry<String,Integer>> ranking) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showRanking(ranking);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForFinalRanking(this, viewAPI);
    }
}

