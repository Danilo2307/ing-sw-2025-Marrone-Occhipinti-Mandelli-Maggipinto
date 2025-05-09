package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class PlanetOccupation extends Event{

    int i;

    public PlanetOccupation(GameStatus newstatus, int i){
        super(newstatus);
        this.i = i;
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("È stato occupato il pianeta numero ").append(i).append("\n");
        return sb.toString();
    }
}
