package it.polimi.ingsw.psp23.model.cards;

public class PlayVisitor implements Visitor{

    public Void visitForPlanets(Planets planets){
        planets.play();
        return null;
    }

    public Void visitForAbandonedShip(AbandonedShip abandonedShip){
        abandonedShip.play();
        return null;
    }

    public Void visitForAbandonedStation(AbandonedStation abandonedStation){
        abandonedStation.play();
        return null;
    }

    public Void visitForCannonShot(CannonShot cannonShot){
        cannonShot.play();
        return null;
    }

    public Void visitForCombatZone(CombatZone combatZone){
        combatZone.play();
        return null;
    }

    public Void visitForEpidemic(Epidemic epidemic){
        epidemic.play();
        return null;
    }

    public Void visitForMeteorSwarm(MeteorSwarm meteorSwarm){
        meteorSwarm.play();
        return null;
    }

    public Void visitForOpenSpace(OpenSpace openSpace){
        openSpace.play();
        return null;
    }

    public Void visitForPirates(Pirates pirates){
        pirates.play();
        return null;
    }

    public Void visitForSlavers(Slavers slavers){
        slavers.play();
        return null;
    }

    public Void visitForSmugglers(Smugglers smugglers){
        smugglers.play();
        return null;
    }

    public Void visitForStardust(Stardust stardust){
        stardust.play();
        return null;
    }
}
