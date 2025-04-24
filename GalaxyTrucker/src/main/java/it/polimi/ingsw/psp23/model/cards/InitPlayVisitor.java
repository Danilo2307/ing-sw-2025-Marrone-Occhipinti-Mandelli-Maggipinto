package it.polimi.ingsw.psp23.model.cards;

public class InitPlayVisitor implements Visitor{

    public Void visitForPlanets(Planets planets){
        planets.initPlay();
        return null;
    }

    public Void visitForAbandonedShip(AbandonedShip abandonedShip){
        abandonedShip.initPlay();
        return null;
    }

    public Void visitForAbandonedStation(AbandonedStation abandonedStation){
        abandonedStation.initPlay();
        return null;
    }

    public Void visitForCannonShot(CannonShot cannonShot){
        cannonShot.initPlay();
        return null;
    }

    public Void visitForCombatZone(CombatZone combatZone){
        combatZone.initPlay();
        return null;
    }

    public Void visitForEpidemic(Epidemic epidemic){
        epidemic.initPlay();
        return null;
    }

    public Void visitForMeteorSwarm(MeteorSwarm meteorSwarm){
        meteorSwarm.initPlay();
        return null;
    }

    public Void visitForOpenSpace(OpenSpace openSpace){
        openSpace.initPlay();
        return null;
    }

    public Void visitForPirates(Pirates pirates){
        pirates.initPlay();
        return null;
    }

    public Void visitForSlavers(Slavers slavers){
        slavers.initPlay();
        return null;
    }

    public Void visitForSmugglers(Smugglers smugglers){
        smugglers.initPlay();
        return null;
    }

    public Void visitForStardust(Stardust stardust){
        stardust.initPlay();
        return null;
    }

}
