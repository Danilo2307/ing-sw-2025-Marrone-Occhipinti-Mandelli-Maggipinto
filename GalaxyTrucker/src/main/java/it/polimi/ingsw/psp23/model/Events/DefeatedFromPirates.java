package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class DefeatedFromPirates extends Event {

    public DefeatedFromPirates(GameStatus newStatus) {
        super(newStatus);
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hai perso, ora devi resistere alle cannonate!\n");
        return sb.toString();
    }

}
