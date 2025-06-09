// Evento che notifica tutti i client quando un nemico(smugglers, pirates o slavers)

package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.Direction;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class EnemyDefeated extends Event {

    public EnemyDefeated(GameStatus newStatus) {
        super(newStatus);
    }

    @Override
    public String describe(int gameId) {
        StringBuilder sb = new StringBuilder();
        sb.append("I nemici sono stati sconfitti!\n");
        return sb.toString();
    }
}
