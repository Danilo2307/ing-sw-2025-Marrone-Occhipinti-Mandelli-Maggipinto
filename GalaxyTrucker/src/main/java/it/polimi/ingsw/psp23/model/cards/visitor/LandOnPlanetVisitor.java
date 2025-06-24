package it.polimi.ingsw.psp23.model.cards.visitor;

import it.polimi.ingsw.psp23.model.cards.Planets;
import it.polimi.ingsw.psp23.model.cards.VisitorUsernameIntero;

public class LandOnPlanetVisitor implements VisitorUsernameIntero<Void> {

    public Void visitForPlanets(Planets planets, String username, int i){
        planets.landOnPlanet(username, i);
        return null;
    }
}
