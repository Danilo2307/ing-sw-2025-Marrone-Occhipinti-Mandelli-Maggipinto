package it.polimi.ingsw.psp23.model.cards;

public class PassVisitor implements VisitorUsername<Void>{

    @Override
    public Void visitForPlanets(Planets planets, String username){
        planets.pass(username);
        return null;
    }

    @Override
    public Void visitForAbandonedShip(AbandonedShip abandonedShip, String username){
        abandonedShip.pass(username);
        return null;
    }

    @Override
    public Void visitForAbandonedStation(AbandonedStation abandonedStation, String username){
        abandonedStation.pass(username);
        return null;
    }

    @Override
    public Void visitForPirates(Pirates pirates, String username){
        pirates.pass(username);
        return null;
    }

    @Override
    public Void visitForSlavers(Slavers slavers, String username){
        slavers.pass(username);
        return null;
    }

    @Override
    public Void visitForSmugglers(Smugglers smugglers, String username){
        smugglers.pass(username);
        return null;
    }

    @Override
    public Void visitForCombatZone(CombatZone combatZone, String username){
        return null;
    }

    @Override
    public Void visitForMeteorSwarm(MeteorSwarm meteorSwarm, String username){
        return null;
    }

    @Override
    public Void visitForOpenSpace(OpenSpace openSpace, String username){
        return null;
    }

}
