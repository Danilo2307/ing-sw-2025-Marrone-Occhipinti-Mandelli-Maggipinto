package it.polimi.ingsw.psp23.model.cards;

public class ReadyVisitor implements VisitorUsername<Void> {

    @Override
    public Void visitForPlanets(Planets planets, String username){
        return null;
    }

    @Override
    public Void visitForAbandonedShip(AbandonedShip abandonedShip, String username){
        return null;
    }

    @Override
    public Void visitForAbandonedStation(AbandonedStation abandonedStation, String username){
        return null;
    }

    @Override
    public Void visitForPirates(Pirates pirates, String username){
        pirates.ready(username);
        return null;
    }

    @Override
    public Void visitForSlavers(Slavers slavers, String username){
        slavers.ready(username);
        return null;
    }

    @Override
    public Void visitForSmugglers(Smugglers smugglers, String username){
        smugglers.ready(username);
        return null;
    }

    @Override
    public Void visitForCombatZone(CombatZone combatZone, String username){
        combatZone.ready(username);
        return null;
    }

    @Override
    public Void visitForMeteorSwarm(MeteorSwarm meteorSwarm, String username){
        meteorSwarm.ready(username);
        return null;
    }

    @Override
    public Void visitForOpenSpace(OpenSpace openSpace, String username){
        openSpace.ready(username);
        return null;
    }

    @Override
    public Void visitForEpidemic(Epidemic epidemic, String username){
        return null;
    }

    @Override
    public Void visitForStardust(Stardust stardust, String username){
        return null;
    }
}
