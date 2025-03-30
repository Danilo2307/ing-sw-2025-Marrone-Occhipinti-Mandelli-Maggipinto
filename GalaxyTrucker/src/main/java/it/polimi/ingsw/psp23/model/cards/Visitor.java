package it.polimi.ingsw.psp23.model.cards;

// questa è l'interfaccia visitor che conterrà i metodi su cui fare Override nelle varie istanze di visitor
// ritorna degli oggetti perchè, avendo la necessità di tornare sempre tipi diversi, usando gli Object
// posso usare gli oggetti Wrapper che il compilatore vede sempre come oggetti ma possono contenere
// tutti i tipi che mi servono
public interface Visitor {

    public Object visitForPlanets(Planets planets);

    public Object visitForAbandonedShip(AbandonedShip abandonedShip);

    public Object visitForAbandonedStation(AbandonedStation abandonedStation);

    public Object visitForCannonShot(CannonShot cannonShot);

    public Object visitForCombatZone(CombatZone combatZone);

    public Object visitForEpidemic(Epidemic epidemic);

    public Object visitForMeteorSwarm(MeteorSwarm meteorSwarm);

    public Object visitForOpenSpace(OpenSpace openSpace);

    public Object visitForPirates(Pirates pirates);

    public Object visitForSlavers(Slavers slavers);

    public Object visitForSmugglers(Smugglers smugglers);

    public Object visitForStardust(Stardust stardust);

}
