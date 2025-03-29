package it.polimi.ingsw.psp23.model.cards;

// ho definito l'interfaccia di tipo generico in modo da poter fare vari override dei metodi che ritornano tipi diversi
public interface Visitor<T> {

    public T visitForPlanets(Planets planets);

    public T visitForAbandonedShip(AbandonedShip abandonedShip);

    public T visitForAbandonedStation(AbandonedStation abandonedStation);

    public T visitForCannonShot(CannonShot cannonShot);

    public T visitForCombatZone(CombatZone combatZone);

    public T visitForEpidemic(Epidemic epidemic);

    public T visitForMeteorSwarm(MeteorSwarm meteorSwarm);

    public T visitForOpenSpace(OpenSpace openSpace);

    public T visitForPirates(Pirates pirates);

    public T visitForSlavers(Slavers slavers);

    public T visitForSmugglers(Smugglers smugglers);

    public T visitForStardust(Stardust stardust);

}
