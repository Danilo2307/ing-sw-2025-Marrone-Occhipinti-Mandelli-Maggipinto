package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class AbandonedShipOccupation extends Event {

    public AbandonedShipOccupation(GameStatus newStatus) {
        super(newStatus);
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("La nave abbandonata è stata acquistata!").append("\n");
        return sb.toString();
    }

}
