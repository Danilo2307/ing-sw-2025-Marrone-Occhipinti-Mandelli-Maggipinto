package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class AbandonedStationOccupation extends Event {

    public AbandonedStationOccupation(GameStatus newStatus) {
        super(newStatus);
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("La stazione abbandonata è stata occupata!").append("\n");
        return sb.toString();
    }

}
