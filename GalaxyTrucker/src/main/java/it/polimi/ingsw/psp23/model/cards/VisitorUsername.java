package it.polimi.ingsw.psp23.model.cards;

public interface VisitorUsername<T> {

    public T visitForPlanets(Planets planets, String username);

    public T visitForAbandonedShip(AbandonedShip abandonedShip, String username);

    public T visitForAbandonedStation(AbandonedStation abandonedStation, String username);

    public T visitForPirates(Pirates pirates, String username);

    public T visitForSlavers(Slavers slavers, String username);

    public T visitForSmugglers(Smugglers smugglers, String username);

    public T visitForCombatZone(CombatZone combatZone, String username);

    public T visitForMeteorSwarm(MeteorSwarm meteorSwarm, String username);

    public T visitForOpenSpace(OpenSpace openSpace, String username);

    public T visitForEpidemic(Epidemic epidemic, String username);

    public T visitForStardust(Stardust stardust, String username);

}
