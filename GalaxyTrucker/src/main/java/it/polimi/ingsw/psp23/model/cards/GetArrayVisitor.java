package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Item;

// uso la list in maniera RAW perchè voglio avere più tipi di array
public class GetArrayVisitor implements Visitor {

    public String[] visitForPlanets(Planets planets){
        return planets.getPlanetsOccupied();
    }

    public Void visitForAbandonedShip(AbandonedShip abandonedShip){
        return null;
    }

    public Void visitForAbandonedStation(AbandonedStation abandonedStation){
        return null;
    }

    public Void visitForCannonShot(CannonShot cannonShot){
        return null;
    }

    public Void visitForCombatZone(CombatZone combatZone){
        return null;
    }

    public Void visitForEpidemic(Epidemic epidemic){
        return null;
    }

    public Void visitForMeteorSwarm(MeteorSwarm meteorSwarm){
        return null;
    }

    public Void visitForOpenSpace(OpenSpace openSpace){
        return null;
    }

    public Void visitForPirates(Pirates pirates){
        return null;
    }

    public Void visitForSlavers(Slavers slavers){
        return null;
    }

    public Void visitForSmugglers(Smugglers smugglers){
        return null;
    }

    public Void visitForStardust(Stardust stardust){
        return null;
    }
}
