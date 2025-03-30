package it.polimi.ingsw.psp23.model.cards;

// questa interfaccia serve per il metodo setPlanetOccupation presente in Planets
// perchè richiede il passaggio di un parametro, quindi serve un metodo
// accept che, oltre al visitor, prenda in ingresso anche un parametro
public interface VisitorParametrico {
    public Object visitForPlanets(Planets planets, int index);

    public Object visitForCombatZone(CombatZone combatZone, int index);
}
