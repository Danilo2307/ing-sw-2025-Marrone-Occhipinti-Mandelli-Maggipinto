package it.polimi.ingsw.psp23.model.cards;

public class ActiveCannonVisitor implements VisitorCoordinate<Void> {

    @Override
    public Void visitForAbandonedStation(AbandonedStation abandonedStation, String username, int i, int j){
        return null;
    }

    @Override
    public Void visitForPlanets(Planets planets, String username, int i, int j){
        return null;
    }

    @Override
    public Void visitForSmugglers(Smugglers smugglers, String username, int i, int j){
        smugglers.activeCannon(username, i, j);
        return null;
    }

    @Override
    public Void visitForCombatZone(CombatZone combatZone, String username, int i, int j){
        combatZone.activeCannon(username, i, j);
        return null;
    }

    @Override
    public Void visitForMeteorSwarm(MeteorSwarm meteorSwarm, String username, int i, int j){
        meteorSwarm.activeCannon(username, i, j);
        return null;
    }

    @Override
    public Void visitForPirates(Pirates pirates, String username, int i, int j){
        pirates.activeCannon(username, i, j);
        return null;
    }

    @Override
    public Void visitForSlavers(Slavers slavers, String username, int i, int j){
        slavers.activeCannon(username, i, j);
        return null;
    }

    @Override
    public Void visitForOpenSpace(OpenSpace openSpace, String username, int i, int j){
        return null;
    }

}
