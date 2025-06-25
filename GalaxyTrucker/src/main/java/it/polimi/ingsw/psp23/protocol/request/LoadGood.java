package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.visitor.LoadGoodsVisitor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;

public record LoadGood(int lx, int ly) implements Action {


    /**
     * Handles the action of loading goods for a game participant based on their username.
     * This method validates the game's current status and initiates the process to load goods,
     * using a visitor pattern to perform the specific action tied to the current card.
     * Load the good at ship[i][j]
     *
     * @param username the username of the player who is performing the load goods action
     * @throws InvalidActionException if the game is not in a valid state to perform this action
     */
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.END_ABANDONEDSTATION && game.getGameStatus() != GameStatus.END_PLANETS && game.getGameStatus() != GameStatus.END_SMUGGLERS){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Card currentCard = game.getCurrentCard();
        LoadGoodsVisitor loadGood = new LoadGoodsVisitor();
        currentCard.call(loadGood, username, lx, ly);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForLoadGoods(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
