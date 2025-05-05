package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.ActiveCannonVisitor;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.LandOnPlanetVisitor;

public record Land(int pi) implements Action {
    public void handle(String username){
        Game game = Game.getInstance();
        Card currentCard = game.getCurrentCard();
        LandOnPlanetVisitor land = new LandOnPlanetVisitor();
        currentCard.call(land, username, pi);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForLandOnPlanet(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }
}
