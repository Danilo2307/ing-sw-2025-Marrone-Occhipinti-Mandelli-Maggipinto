package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.Server;
import it.polimi.ingsw.psp23.protocol.response.MatchFinished;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

public record LeaveFlight() implements Action {


    /**
     * Handles the action of a player leaving the flight in the game.
     * Ensures that the game state allows this action, and notifies all relevant players.
     *
     * @param username the username of the player who is leaving the flight
     * @throws InvalidActionException if the action is not allowed in the current game state
     */
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.WAITING_FOR_NEW_CARD && game.getGameStatus() != GameStatus.SetCrew && game.getGameStatus() != GameStatus.CheckBoards && game.getGameStatus() != GameStatus.Building) {
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }

        game.getPlayerFromNickname(username).setInGame(false);
        Server.getInstance().sendMessage(username, new DirectMessage(new MatchFinished("Hai abbandonato il volo, attendi che finiscano anche gli altri giocatori\n")));

        game.removePlayersNotInFlight();

        for(Player player : game.getPlayers()){
            Server.getInstance().sendMessage(player.getNickname(), new DirectMessage(new StringResponse(username + " ha abbandonato il volo, vi reincontrerete nella fase finale del calcolo del punteggio\n")));
        }

    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForLeaveFlight(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}