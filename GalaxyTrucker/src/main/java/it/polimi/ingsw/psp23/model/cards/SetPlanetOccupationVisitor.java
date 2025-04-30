package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.model.enumeration.Challenge;

public class SetPlanetOccupationVisitor implements VisitorParametrico {

    public Void visitForPlanets(Planets planets, int index, String player) {
        planets.setPlanetOccupation(index, player);
        return null;
    }

    public Challenge visitForCombatZone(CombatZone combatZone, int index) {
        return null;
    }

}
