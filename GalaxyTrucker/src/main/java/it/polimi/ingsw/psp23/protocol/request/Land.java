package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.visitor.LandOnPlanetVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;

public record Land(int pi) implements Action {


    /**
     * Handles the landing action for a player in the game. This method checks the current game status
     * to ensure the action can be performed. If the game is not in the initialization phase for planets,
     * an exception is thrown. The method uses a visitor pattern to execute the landing on a planet.
     *
     * @param username The username of the player attempting to perform the landing action.
     * @throws InvalidActionException If the action is attempted when the game status is not INIT_PLANETS.
     */
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.INIT_PLANETS){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
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
