package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

public record ShowPlayersPositions() implements Action{


    /**
     * Handles the action of displaying the positions of all players
     * in the game associated with the provided username. The player
     * will receive a direct message containing the list of all players
     * and their positions in the game.
     *
     * @param username the username of the player requesting the list of players and their positions
     */
    public void handle(String username) {
        StringBuilder sb = new StringBuilder();
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        for (Player player : game.getPlayers()) {
            sb.append(player.getNickname() + ": " + player.getPosition()).append("\n");
        }
        DirectMessage dm = new DirectMessage(new StringResponse(sb.toString()));
        Server.getInstance().sendMessage(username, dm);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForShowPlayersPositions(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar) {
        return null;
    }

}
