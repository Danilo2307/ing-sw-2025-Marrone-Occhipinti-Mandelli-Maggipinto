package it.polimi.ingsw.psp23.model.cards;

// questa è l'interfaccia visitor che conterrà i metodi su cui fare Override nelle varie istanze di visitor
// ritorna degli oggetti generici perchè, avendo la necessità di tornare sempre tipi diversi, usando i Generics
// posso usare gli oggetti Wrapper per i tipi primitivi ma possono contenere
// tutti gli altri tipi che mi servono. Quando creo un visitor che mi ritorna un tipo non primitivo
// come ad esempio una lista(che possiede quindi dei metodi che possono essere chiamati, motivo per il
// quale è importante riportare il tipo statico) dovrò creare il visitor specificando il tipo generico
// tra parentesi angolari, ad esempio:" Visitor<List<List<Item>>> visitor1 = new GetListVisitor();"
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
