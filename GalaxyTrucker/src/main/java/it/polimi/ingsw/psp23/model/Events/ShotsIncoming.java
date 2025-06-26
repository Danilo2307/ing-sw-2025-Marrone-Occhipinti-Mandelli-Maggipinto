// Viene mappato nell'evento omonimo indirizzato al client

package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.Direction;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.helpers.CannonShot;

import java.util.List;


public class ShotsIncoming extends Event {

    List<CannonShot> cannonShots;

    public ShotsIncoming(GameStatus newStatus, List<CannonShot> cs) {
        super(newStatus);
        this.cannonShots = cs;
    }

    @Override
    public String describe(int gameId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Attento! Dovrai affrontare queste cannonate:\n");
        for(CannonShot c : cannonShots){
            if(c.isBig()) {
                sb.append("Una cannonata grossa da ").append(c.getDirection()).append(" all'indice ").append(c.getImpactLine()).append("\n");
            }
            else{
                sb.append("Una cannonata piccola da ").append(c.getDirection()).append(" all'indice ").append(c.getImpactLine()).append("\n");
            }
        }
        return sb.toString();
    }
}
