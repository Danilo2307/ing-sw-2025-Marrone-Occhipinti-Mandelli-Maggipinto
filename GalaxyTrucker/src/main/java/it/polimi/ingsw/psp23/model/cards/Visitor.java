package it.polimi.ingsw.psp23.model.cards;

// questa è l'interfaccia visitor che conterrà i metodi su cui fare Override nelle varie istanze di visitor
// ritorna degli oggetti generici perchè, avendo la necessità di tornare sempre tipi diversi, usando i Generics
// posso usare gli oggetti Wrapper per i tipi primitivi ma possono contenere
// tutti gli altri tipi che mi servono. Quando creo un visitor(soprattutto quando mi ritorna un tipo non primitivo
// come ad esempio una lista che possiede quindi dei metodi che possono essere chiamati, motivo per il
// quale è importante riportare il tipo statico) dovrò creare il visitor specificando il tipo generico
// tra parentesi angolari, ad esempio:" Visitor<List<List<Item>>> visitor1 = new GetListVisitor();",
// oppure "Visitor<Integer> visitor2 = new getDaysVisitor()" quando so che mi ritornerà un intero.
// Nel caso di Visitor che mi ritornano "tipi primitiv", quindi ogetti su cui non chiamerò altri
// metodi come ad esempio "get" si potrebbe anche omettere la specifica del tipo tra parentesi angolari,
// tuttavia questa operazione prende il nome di "raw usage of parametrized class" e, oltre ad essere rischiosa,
// rende il codice meno leggibile!!!!

public interface Visitor<T> {

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
