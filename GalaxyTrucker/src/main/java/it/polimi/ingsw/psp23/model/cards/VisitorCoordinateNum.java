package it.polimi.ingsw.psp23.model.cards;

public interface VisitorCoordinateNum<T> {

    public T visitForAbandonedShip(AbandonedShip abandonedShip, String username, int i, int j, int num);

    public T visitForCombatZone(CombatZone combatZone, String username, int i, int j, int num);

    public T visitForSlavers(Slavers slavers, String username, int i, int j, int num);

    public T visitForSmugglers(Smugglers smugglers, String username, int i, int j, int num);

}
