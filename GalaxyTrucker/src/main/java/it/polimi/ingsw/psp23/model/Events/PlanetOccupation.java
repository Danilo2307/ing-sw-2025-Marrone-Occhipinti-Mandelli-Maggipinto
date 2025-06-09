package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class PlanetOccupation extends Event{

    int i;

    public PlanetOccupation(GameStatus newStatus, int i){
        super(newStatus);
        this.i = i;
    }

    @Override
    public String describe(int gameId) {
        StringBuilder sb = new StringBuilder();
        sb.append("È stato occupato il pianeta numero ").append(i).append("\n");
        return sb.toString();
    }
}
