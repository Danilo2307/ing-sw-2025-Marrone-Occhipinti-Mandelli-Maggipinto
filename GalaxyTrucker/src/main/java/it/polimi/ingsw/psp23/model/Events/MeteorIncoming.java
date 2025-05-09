// Viene mappato nell'evento omonimo indirizzato al client

package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.Direction;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class MeteorIncoming extends Event {

    int indice;
    Direction direction;

    public MeteorIncoming(GameStatus newStatus, int indice, Direction direction) {
        super(newStatus);
        this.indice = indice;
        this.direction = direction;
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("È in arrivo un meteorite da ").append(direction).append(" all'indice ").append(indice).append("\n");
        return sb.toString();
    }

}
