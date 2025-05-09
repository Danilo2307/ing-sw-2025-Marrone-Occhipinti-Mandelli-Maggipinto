// Evento che notifica la quantità di merci da rimuovere

package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class NumItemsStolenFromSmugglers extends Event {

    int numItems;

    public NumItemsStolenFromSmugglers(GameStatus newStatus, int numItems) {
        super(newStatus);
        this.numItems = numItems;
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hai perso, perderai le tue ").append(numItems).append(" merci più importanti\n");
        return sb.toString();
    }

}
