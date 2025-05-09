// Evento che notifica la quantità di membri dell'equipaggio da rimuovere

package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class NumMembersStolenFromSlavers extends Event {

    int numMembers;

    public NumMembersStolenFromSlavers(GameStatus newStatus, int numMembers) {
        super(newStatus);
        this.numMembers = numMembers;
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hai perso, rimuovi ").append(numMembers).append(" membri dell'equipaggio\n");
        return sb.toString();
    }

}
