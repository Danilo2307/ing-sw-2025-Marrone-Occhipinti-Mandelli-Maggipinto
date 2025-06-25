package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.visitor.RemovePreciousItemVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;

public record RemovePreciousItem(int cx, int cy, int index) implements Action {

    /**
     * Removes an item from the specified coordinates (cx, cy) and index in the ship.
     * This method validates the game status before proceeding with the removal process.
     *
     * @param username the username of the player performing the action
     * @throws InvalidActionException if the action is attempted outside of the allowed game statuses
     */
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.SECOND_COMBATZONE && game.getGameStatus() != GameStatus.END_SMUGGLERS){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Card currentCard = game.getCurrentCard();
        RemovePreciousItemVisitor removeItem = new RemovePreciousItemVisitor();
        currentCard.call(removeItem, username, cx, cy, index);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForRemovePreciousItem(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
