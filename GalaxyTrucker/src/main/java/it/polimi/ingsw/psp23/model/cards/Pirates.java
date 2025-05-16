package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.model.Events.*;
import it.polimi.ingsw.psp23.model.Game.Utility;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the Pirates card and handles the encounter logic with pirates.
 */
public class Pirates extends Card {
    private final int prize;                 // Cosmic credits reward
    private final int days;                  // Days of rest to apply
    private final int firepower;             // Pirates' firepower strength
    private final List<CannonShot> cannonShot; // Sequence of cannon shots
    private List<String> losers = new ArrayList<>();             // Players who lost initial encounters
    private final Set<String> resolvers = new HashSet<String>();      ;     // Players who confirmed READY
    private int countCannonShot = 0;             // Index of the current cannon shot
    private String winner = null;                   // Winner of the challenge

    /**
     * Constructs a Pirates card with specified level and encounter parameters.
     *
     * @param level the difficulty level of the card
     * @param prize the cosmic credits awarded to the winner
     * @param days number of rest days applied to the winner
     * @param firepower the firepower threshold to beat
     * @param cannonShot list of cannon shots for damage resolution
     */
    public Pirates(int level, int prize, int days, int firepower, List<CannonShot> cannonShot, int id) {
        super(level, id);
        this.prize = prize;
        this.days = days;
        this.firepower = firepower;
        this.cannonShot = cannonShot;
        this.countCannonShot = 0;
        this.winner = null;
    }

    // --- Getters ---
    public int getPrize() { return prize; }
    public int getDays() { return days; }
    public int getFirepower() { return firepower; }
    public List<CannonShot> getCannonShot() { return new ArrayList<>(cannonShot); }

    @Override
    public <T> T call(Visitor<T> visitor) {
        return visitor.visitForPirates(this);
    }

    @Override
    public <T> T call(VisitorUsername<T> visitorUsername, String username) {
        return visitorUsername.visitForPirates(this, username);
    }

    @Override
    public <T> T call(VisitorCoordinate<T> visitorCoordinate, String username, int i, int j) {
        return visitorCoordinate.visitForPirates(this, username, i, j);
    }

    /**
     * Starts the pirate encounter by setting the game status and firing the event.
     */
    public void initPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_PIRATES);
        game.fireEvent(new EventForPirates(
                game.getGameStatus(), days, firepower, prize, cannonShot
        ));
        Game.getInstance().setCurrentPlayer(Game.getInstance().getPlayers().getFirst());
    }

    /**
     * Allows the current player to place a cannon shot during the INIT_PIRATES phase.
     *
     * @param username the nickname of the current player
     * @param i the row position for the cannon
     * @param j the column position for the cannon
     */
    public void activeCannon(String username, int i, int j) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_PIRATES) {
            throw new CardException("Cannot activate cannon now: phase is " + game.getGameStatus());
        }
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer().getNickname());
        }
        game.getPlayerFromNickname(username)
                .getTruck()
                .activeCannon(i, j);
    }

    /**
     * Allows a loser to activate shield during the END_PIRATES phase.
     *
     * @param username the nickname of the losing player
     * @param i the row position for the shield
     * @param j the column position for the shield
     */
    public void activeShield(String username, int i, int j) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.END_PIRATES) {
            throw new CardException("Cannot activate shield now: phase is " + game.getGameStatus());
        }
        if (!losers.contains(username)) {
            throw new CardException("You are not a loser!");
        }
        game.getPlayerFromNickname(username)
                .getTruck()
                .activeShield(i, j);
    }

    /**
     * Awards cosmic credits to the winner and applies rest days.
     * Then resets encounter state or ends the pirate phase.
     *
     * @param username the nickname of the winning player
     */
    public void getCosmicCredits(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_PIRATES) {
            throw new CardException("Cannot get money now: phase is " + game.getGameStatus());
        }
        if (!username.equals(winner)) {
            throw new CardException("You did not defeat the pirates: " + username);
        }
        game.getPlayerFromNickname(winner).updateMoney(prize);
        int idx = game.getPlayers().indexOf(game.getPlayerFromNickname(username));
        Utility.updatePosition(game.getPlayers(), idx, -days);
        game.sortPlayersByPosition();
        if (losers.isEmpty()) {
            game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")));
        }
        else{
            game.setGameStatus(GameStatus.END_PIRATES);
            game.setCurrentPlayer(game.getPlayerFromNickname(losers.getFirst()));
            if(getLevel() == 2){
                CannonShot c = cannonShot.get(countCannonShot);
                int impactLine = Utility.roll2to12();
                game.fireEvent(new CannonShotIncoming(game.getGameStatus(), c.isBig(), impactLine, c.getDirection()));
                for (String player : losers) {
                    game.getPlayerFromNickname(player)
                            .getTruck()
                            .handleCannonShot(c, impactLine);
                }
                countCannonShot++;
            }
        }
    }

    /**
     * Allows the winner to pass on claiming the reward.
     *
     * @param username the nickname of the winning player
     */
    public void pass(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_PIRATES) {
            throw new CardException("Cannot get money now: phase is " + game.getGameStatus());
        }
        if (!username.equals(winner)) {
            throw new CardException("You did not defeat the pirates: " + username);
        }
        if (losers.isEmpty()) {
            game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")));
        }
        else{
            game.setGameStatus(GameStatus.END_PIRATES);
            game.setCurrentPlayer(game.getPlayerFromNickname(losers.getFirst()));
            if(getLevel() == 2){
                CannonShot c = cannonShot.get(countCannonShot);
                int impactLine = Utility.roll2to12();
                game.fireEvent(new CannonShotIncoming(game.getGameStatus(), c.isBig(), impactLine, c.getDirection()));
                for (String player : losers) {
                    game.getPlayerFromNickname(player)
                            .getTruck()
                            .handleCannonShot(c, impactLine);
                }
                countCannonShot++;
            }
        }
    }

    /**
     * Handles the READY command by delegating to the appropriate phase handler.
     *
     * @param username the nickname of the player issuing READY
     */
    public void ready(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() == GameStatus.INIT_PIRATES) {
            readyStartPhase(username);
        } else if (game.getGameStatus() == GameStatus.END_PIRATES) {
            readyResolutionPhase(username);
        }
        else{
            throw new CardException("Invalid phase for READY:" + game.getGameStatus());
        }
    }

    /**
     * Handles the READY logic for the INIT_PIRATES phase.
     */
    private void readyStartPhase(String username) {
        Game game = Game.getInstance();
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer().getNickname());
        }
        double playerFirepower = game.getPlayerFromNickname(username)
                .getTruck()
                .calculateCannonStrength();
        if (playerFirepower > firepower) {
            winner = username;
            game.fireEvent(new EnemyDefeated(game.getGameStatus()));
            game.fireEvent(new CosmicCreditsEarned(game.getGameStatus()), winner);
        } else if(playerFirepower < firepower){
            game.fireEvent(new DefeatedFromPirates(game.getGameStatus()), username);
            losers.add(username);
        }
        if (game.getCurrentPlayerIndex() >= game.getPlayers().size() - 1) {
            game.setCurrentPlayer(
                    game.getPlayerFromNickname(losers.getFirst())
            );
            game.setGameStatus(GameStatus.END_PIRATES);
            if(getLevel() == 2){
                CannonShot c = cannonShot.get(countCannonShot);
                int impactLine = Utility.roll2to12();
                game.fireEvent(new CannonShotIncoming(game.getGameStatus(), c.isBig(), impactLine, c.getDirection()));
                for (String player : losers) {
                    game.getPlayerFromNickname(player)
                            .getTruck()
                            .handleCannonShot(c, impactLine);
                }
                countCannonShot++;
            }
        }
        else{
            game.getNextPlayer();
            game.fireEvent(new TurnOf(game.getGameStatus(), game.getCurrentPlayer().getNickname()));
        }
    }

    /**
     * Handles the READY logic for the END_PIRATES resolution phase.
     */
    private void readyResolutionPhase(String username) {
        Game game = Game.getInstance();
        if (!losers.contains(username)) {
            throw new CardException("You are not a loser");
        }
        resolvers.add(username);
        if (!resolvers.containsAll(losers)) {
            return;
        }
        if(getLevel() == 2){
            for (CannonShot c : cannonShot.subList(countCannonShot, cannonShot.size())){
                int impactLine = Utility.roll2to12();
                game.fireEvent(new CannonShotIncoming(game.getGameStatus(), c.isBig(), impactLine, c.getDirection()));
                for (String player : losers) {
                    game.getPlayerFromNickname(player)
                            .getTruck()
                            .handleCannonShot(c, impactLine);
                }
            }
            game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")));
        }
        else {
            if (countCannonShot == 0) {
                for (CannonShot c : cannonShot.subList(countCannonShot, cannonShot.size() - 1)) {
                    int impactLine = Utility.roll2to12();
                    game.fireEvent(new CannonShotIncoming(game.getGameStatus(), c.isBig(), impactLine, c.getDirection()));
                    for (String player : losers) {
                        game.getPlayerFromNickname(player)
                                .getTruck()
                                .handleCannonShot(c, impactLine);
                    }
                    countCannonShot++;
                    resolvers.clear();
                }
            } else {
                CannonShot c = cannonShot.get(countCannonShot);
                int impactLine = Utility.roll2to12();
                game.fireEvent(new CannonShotIncoming(game.getGameStatus(), c.isBig(), impactLine, c.getDirection()));
                for (String player : losers) {
                    game.getPlayerFromNickname(player)
                            .getTruck()
                            .handleCannonShot(c, impactLine);
                }
                game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
                Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")));
            }
        }
    }

    /**
     * Returns help text based on the current game phase.
     *
     * @return available commands as a string
     */
    public String help() {
        Game game = Game.getInstance();
        GameStatus status = game.getGameStatus();
        switch (status) {
            case INIT_PIRATES:
                return "Available commands: ACTIVECANNON, READY\n";
            case END_PIRATES:
                return "Available commands: ACTIVESHIELD, READY, PASS, CREDIT\n";
            default:
                return "No commands available in current phase.\n";
        }
    }

    @Override
    public String toString(){
        return
                "È uscita la carta Pirates \n" +
                "la potenza di fuoco è " + getFirepower() + "\n" +
                "i crediti cosmici sono " + getPrize() +"\n" +
                "i giorni persi sono " + getDays() + "\n" +
                "i colpi di cannone sono: " + getCannonShot().toString() + "\n";
    }
}
