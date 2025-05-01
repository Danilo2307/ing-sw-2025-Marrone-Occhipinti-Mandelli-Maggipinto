package it.polimi.ingsw.psp23.model.cards;

public interface VisitorInput<T> {

    public T visitForPlanets(Planets planets);

    public T visitForAbandonedShip(AbandonedShip abandonedShip);

    public T visitForAbandonedStation(AbandonedStation abandonedStation);

    public T visitForCombatZone(CombatZone combatZone);

    public T visitForEpidemic(Epidemic epidemic);

    public T visitForMeteorSwarm(MeteorSwarm meteorSwarm);

    public T visitForOpenSpace(OpenSpace openSpace);

    public T visitForPirates(Pirates pirates);

    public T visitForSlavers(Slavers slavers);

    public T visitForSmugglers(Smugglers smugglers);

    public T visitForStardust(Stardust stardust);

}
