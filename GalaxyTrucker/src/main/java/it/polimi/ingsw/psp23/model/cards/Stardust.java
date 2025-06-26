package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.helpers.Utility;
import it.polimi.ingsw.psp23.model.Events.EventForStardust;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

import java.util.List;

/**
 * Represents the "Stardust" special event card.
 * <p>
 * When played, each player in reverse flight order loses one space
 * (flight day) for every exposed connector on their ship.
 * </p>
 */
public class Stardust extends Card {
    /**
     * Constructs a Stardust card at the specified adventure level.
     *
     * @param level the difficulty level of this card
     */
    public Stardust(int level, int id) {
        super(level, id);
    }

    /**
     * Prepares the game to resolve this card by setting the status to INIT_STARDUST
     * and notifying any observers of the status change, then immediately executes the effect.
     *
     * @implNote After firing the event, this method directly calls .
     */
    public void initPlay(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        game.setGameStatus(GameStatus.INIT_STARDUST);
        game.fireEvent(new EventForStardust(game.getGameStatus()));
        applyEffect(username);
    }

    /**
     * Executes the effect of the Stardust card:
     * <ol>
     *   <li>Retrieve the current list of players and invert it to process from last to first.</li>
     *   <li>For each player in reverse order, calculate how many connectors are exposed on their ship.</li>
     *   <li>Move that player backward by the number of exposed connectors.</li>
     *   <li>Set the game status to {@link GameStatus#Playing} to resume normal play.</li>
     * </ol>
     *
     * @implNote This method mutates each player's position based on exposed connectors.
     */
    private void applyEffect(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        List<Player> players = game.getPlayers();
        for (Player p : players.reversed()) {
            int penalty = p.getTruck().calculateExposedConnectors();
            Utility.updatePosition(players, players.indexOf(p), -penalty);
        }
        game.sortPlayersByPosition();
        game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")), game.getId());
    }
    /**
     * Provides help information for the Stardust card.
     *
     * @return default help text
     */
    public String help(String username) {
        return "No commands available for Stardust; effect is automatic.";
    }

    @Override
    public String toString(){
        return
                "è uscita la carta Stardust\n" +
                "si perdono tanti giorni quanti sono i connettori esposti";
    }

    public <T> T call(Visitor<T> visitor) {
        return visitor.visitForStardust(this);
    }

    public <T> T call(VisitorUsername<T> visitor, String username) {
        return visitor.visitForStardust(this, username);
    }

}
