package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.Map;

public record FlightBoardResponse(Map<Color, Integer> flightMap) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showFlightBoard(flightMap);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForFlightBoardResponse(this, viewAPI);
    }


}
