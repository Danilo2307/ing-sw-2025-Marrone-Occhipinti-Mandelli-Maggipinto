package it.polimi.ingsw.psp23.model.cards;

public interface VisitorCoordinate<T> {

    public T visitForAbandonedStation(AbandonedStation abandonedStation, String username, int i, int j);

    public T visitForPlanets(Planets planets, String username, int i, int j);

    public T visitForSmugglers(Smugglers smugglers, String username, int i, int j);

    public T visitForCombatZone(CombatZone combatZone, String username, int i, int j);

    public T visitForMeteorSwarm(MeteorSwarm meteorSwarm, String username, int i, int j);

    public T visitForPirates(Pirates pirates, String username, int i, int j);

    public T visitForSlavers(Slavers slavers, String username, int i, int j);

    public T visitForOpenSpace(OpenSpace openSpace, String username, int i, int j);

}
