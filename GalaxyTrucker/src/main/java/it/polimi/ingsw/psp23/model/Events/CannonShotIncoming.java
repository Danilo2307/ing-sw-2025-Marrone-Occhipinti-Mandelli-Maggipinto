// Viene mappato nell'evento omonimo indirizzato al client

package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.Direction;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class CannonShotIncoming extends Event {

    int indice;
    Direction direction;
    boolean isBig;

    public CannonShotIncoming(GameStatus newStatus, boolean isBig, int indice, Direction direction) {
        super(newStatus);
        this.indice = indice;
        this.direction = direction;
        this.isBig = isBig;
    }

    @Override
    public String describe(int gameId) {
        StringBuilder sb = new StringBuilder();
        if(isBig) {
            sb.append("È in arrivo una cannonata grossa da ").append(direction).append(" all'indice ").append(indice).append("\n");
        }
        else{
            sb.append("È in arrivo una cannonata piccola da ").append(direction).append(" all'indice ").append(indice).append("\n");
        }
        return sb.toString();
    }

}
