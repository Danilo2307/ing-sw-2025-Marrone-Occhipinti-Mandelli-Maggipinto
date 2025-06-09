package it.polimi.ingsw.psp23.model.cards;

public class InitPlayVisitor implements VisitorUsername<Void>{

    public Void visitForPlanets(Planets planets, String username){
        planets.initPlay(username);
        return null;
    }

    public Void visitForAbandonedShip(AbandonedShip abandonedShip, String username){
        abandonedShip.initPlay(username);
        return null;
    }

    public Void visitForAbandonedStation(AbandonedStation abandonedStation, String username){
        abandonedStation.initPlay(username);
        return null;
    }

    public Void visitForCombatZone(CombatZone combatZone, String username){
        combatZone.initPlay(username);
        return null;
    }

    public Void visitForEpidemic(Epidemic epidemic, String username){
        epidemic.initPlay(username);
        return null;
    }

    public Void visitForMeteorSwarm(MeteorSwarm meteorSwarm, String username){
        meteorSwarm.initPlay(username);
        return null;
    }

    public Void visitForOpenSpace(OpenSpace openSpace, String username){
        openSpace.initPlay(username);
        return null;
    }

    public Void visitForPirates(Pirates pirates, String username){
        pirates.initPlay(username);
        return null;
    }

    public Void visitForSlavers(Slavers slavers, String username){
        slavers.initPlay(username);
        return null;
    }

    public Void visitForSmugglers(Smugglers smugglers, String username){
        smugglers.initPlay(username);
        return null;
    }

    public Void visitForStardust(Stardust stardust, String username){
        stardust.initPlay(username);
        return null;
    }

}
