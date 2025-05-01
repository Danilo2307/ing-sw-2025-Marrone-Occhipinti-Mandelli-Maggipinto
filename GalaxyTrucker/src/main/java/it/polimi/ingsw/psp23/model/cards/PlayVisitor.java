package it.polimi.ingsw.psp23.model.cards;

public class PlayVisitor implements VisitorInput<Void> {

    public Void visitForPlanets(Planets planets, InputObject inputObject){
        planets.play();
        return null;
    }

    public Void visitForAbandonedShip(AbandonedShip abandonedShip, InputObject inputObject){
        abandonedShip.play();
        return null;
    }

    public Void visitForAbandonedStation(AbandonedStation abandonedStation, InputObject inputObject){
        abandonedStation.play();
        return null;
    }

    public Void visitForCombatZone(CombatZone combatZone, InputObject inputObject){
        combatZone.play(inputObject);
        return null;
    }

    public Void visitForEpidemic(Epidemic epidemic, InputObject inputObject){
        epidemic.play(inputObject);
        return null;
    }

    public Void visitForMeteorSwarm(MeteorSwarm meteorSwarm, InputObject inputObject){
        meteorSwarm.play(inputObject);
        return null;
    }

    public Void visitForOpenSpace(OpenSpace openSpace, InputObject inputObject){
        openSpace.play(inputObject);
        return null;
    }

    public Void visitForPirates(Pirates pirates, InputObject inputObject){
        pirates.play(inputObject);
        return null;
    }

    public Void visitForSlavers(Slavers slavers, InputObject inputObject){
        slavers.play(inputObject);
        return null;
    }

    public Void visitForSmugglers(Smugglers smugglers, InputObject inputObject){
        smugglers.play(inputObject);
        return null;
    }

    public Void visitForStardust(Stardust stardust, InputObject inputObject){
        stardust.play(inputObject);
        return null;
    }
}
