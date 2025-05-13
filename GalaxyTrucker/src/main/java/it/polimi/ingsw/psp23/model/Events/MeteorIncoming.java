// Viene mappato nell'evento omonimo indirizzato al client

package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.Direction;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class MeteorIncoming extends Event {

    int indice;
    Direction direction;
    boolean grande;


    public MeteorIncoming(GameStatus newStatus, boolean grande, int indice, Direction direction) {
        super(newStatus);
        this.indice = indice;
        this.direction = direction;
        this.grande = grande;
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        if(grande) {
            sb.append("È in arrivo un meteorite grosso da ").append(direction).append(" all'indice ").append(indice).append("\n");
        }
        else{
            sb.append("È in arrivo un meteorite piccolo da ").append(direction).append(" all'indice ").append(indice).append("\n");
        }
        return sb.toString();
    }

}
