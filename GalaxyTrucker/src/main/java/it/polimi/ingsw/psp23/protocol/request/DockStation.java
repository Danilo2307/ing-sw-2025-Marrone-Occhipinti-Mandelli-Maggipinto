package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.visitor.DockStationVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;

public record DockStation() implements Action {


    /**
     * Handles the action of docking at a station for a specific user. This method ensures that the game
     * is in the appropriate state for the action to be performed and processes the action using a DockStationVisitor.
     *
     * @param username the username of the player attempting to perform the docking action
     * @throws InvalidActionException if the game is not in the INIT_ABANDONEDSTATION state
     */
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.INIT_ABANDONEDSTATION){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Card currentCard = game.getCurrentCard();
        DockStationVisitor dockStation = new DockStationVisitor();
        currentCard.call(dockStation, username);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForDockStation(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
