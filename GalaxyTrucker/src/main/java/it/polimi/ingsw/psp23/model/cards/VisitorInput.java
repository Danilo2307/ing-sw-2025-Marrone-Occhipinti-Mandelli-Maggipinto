package it.polimi.ingsw.psp23.model.cards;

public interface VisitorInput<T> {

    public T visitForPlanets(Planets planets, InputObject inputObject);

    public T visitForAbandonedShip(AbandonedShip abandonedShip, InputObject inputObject);

    public T visitForAbandonedStation(AbandonedStation abandonedStation, InputObject inputObject);

    public T visitForCannonShot(CannonShot cannonShot, InputObject inputObject);

    public T visitForCombatZone(CombatZone combatZone, InputObject inputObject);

    public T visitForEpidemic(Epidemic epidemic, InputObject inputObject);

    public T visitForMeteorSwarm(MeteorSwarm meteorSwarm, InputObject inputObject);

    public T visitForOpenSpace(OpenSpace openSpace, InputObject inputObject);

    public T visitForPirates(Pirates pirates, InputObject inputObject);

    public T visitForSlavers(Slavers slavers, InputObject inputObject);

    public T visitForSmugglers(Smugglers smugglers, InputObject inputObject);

    public T visitForStardust(Stardust stardust, InputObject inputObject);

}
