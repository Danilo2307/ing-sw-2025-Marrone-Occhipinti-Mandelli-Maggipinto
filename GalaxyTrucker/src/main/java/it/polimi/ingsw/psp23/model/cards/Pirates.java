package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.model.Game.Utility;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Events.EventForPirates;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

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

    private Game game;                       // Game instance
    private List<String> losers;             // Players who lost initial encounters
    private final Set<String> resolvers;     // Players who confirmed READY
    private int countCannonShot;             // Index of the current cannon shot
    private String winner;                   // Winner of the challenge

    /**
     * Constructs a Pirates card with specified level and encounter parameters.
     *
     * @param level the difficulty level of the card
     * @param prize the cosmic credits awarded to the winner
     * @param days number of rest days applied to the winner
     * @param firepower the firepower threshold to beat
     * @param cannonShot list of cannon shots for damage resolution
     */
    public Pirates(int level, int prize, int days, int firepower, List<CannonShot> cannonShot) {
        super(level);
        this.prize = prize;
        this.days = days;
        this.firepower = firepower;
        this.cannonShot = cannonShot;
        this.losers = new ArrayList<>();
        this.resolvers = new HashSet<>();
        this.countCannonShot = 0;
        this.winner = null;
    }

    // --- Getters ---
    public int getPrize() { return prize; }
    public int getDays() { return days; }
    public int getFirepower() { return firepower; }
    public List<CannonShot> getCannonShot() { return new ArrayList<>(cannonShot); }

    @Override
    public Object call(Visitor visitor) {
        return visitor.visitForPirates(this);
    }

    @Override
    public Object call(VisitorUsername visitorUsername, String username) {
        return visitorUsername.visitForPirates(this, username);
    }

    @Override
    public Object call(VisitorCoordinate visitorCoordinate, String username, int i, int j) {
        return visitorCoordinate.visitForPirates(this, username, i, j);
    }

    /**
     * Starts the pirate encounter by setting the game status and firing the event.
     */
    public void initPlay() {
        game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_PIRATES);
        game.fireEvent(new EventForPirates(
                game.getGameStatus(), days, firepower, prize, cannonShot
        ));
    }

    /**
     * Allows the current player to place a cannon shot during the INIT_PIRATES phase.
     *
     * @param username the nickname of the current player
     * @param i the row position for the cannon
     * @param j the column position for the cannon
     */
    public void activeCannon(String username, int i, int j) {
        if (game.getGameStatus() != GameStatus.INIT_PIRATES) {
            throw new CardException("Cannot activate cannon now: phase is " + game.getGameStatus());
        }
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer());
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
        if (game.getGameStatus() != GameStatus.END_PIRATES) {
            throw new CardException("Winner has not been determined yet");
        }
        if (!username.equals(winner)) {
            throw new CardException("You did not defeat the pirates: " + username);
        }

        game.getPlayerFromNickname(winner).updateMoney(prize);
        int idx = game.getPlayers().indexOf(game.getPlayerFromNickname(username));
        Utility.updatePosition(game.getPlayers(), idx, -days);

        winner = null;
        if (countCannonShot == cannonShot.size() - 1) {
            game.nextCard();
        }
    }

    /**
     * Allows the winner to pass on claiming the reward.
     *
     * @param username the nickname of the winning player
     */
    public void pass(String username) {
        if (game.getGameStatus() != GameStatus.END_PIRATES) {
            throw new CardException("Winner has not been determined yet");
        }
        if (!username.equals(winner)) {
            throw new CardException("You did not defeat the pirates: " + username);
        }

        winner = null;
        if (countCannonShot == cannonShot.size() - 1) {
            game.nextCard();
        }
    }

    /**
     * Handles the READY command by delegating to the appropriate phase handler.
     *
     * @param username the nickname of the player issuing READY
     */
    public void ready(String username) {
        if (game.getGameStatus() == GameStatus.INIT_PIRATES) {
            readyStartPhase(username);
        } else if (game.getGameStatus() == GameStatus.END_PIRATES) {
            readyResolutionPhase(username);
        }
    }

    /**
     * Handles the READY logic for the INIT_PIRATES phase.
     */
    private void readyStartPhase(String username) {
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("User '" + username + "' is not the current player");
        }
        double playerFirepower = game.getPlayerFromNickname(username)
                .getTruck()
                .calculateCannonStrength();

        if (playerFirepower > firepower) {
            winner = username;
            if (!losers.isEmpty()) {
                game.setCurrentPlayer(
                        game.getPlayerFromNickname(losers.getFirst())
                );
            }
            game.setGameStatus(GameStatus.END_PIRATES);
        } else {
            losers.add(username);
        }

        if (game.getCurrentPlayerIndex() >= game.getPlayers().size()) {
            game.setCurrentPlayer(
                    game.getPlayerFromNickname(losers.getFirst())
            );
            game.setGameStatus(GameStatus.END_PIRATES);
        }
        else{
            game.getNextPlayer();
        }
    }

    /**
     * Handles the READY logic for the END_PIRATES resolution phase.
     */
    private void readyResolutionPhase(String username) {
        resolvers.add(username);
        if (resolvers.size() < losers.size()) {
            return;
        }

        CannonShot c = cannonShot.get(countCannonShot);
        int impactLine = Utility.roll2to12();
        for (String player : losers) {
            game.getPlayerFromNickname(player)
                    .getTruck()
                    .handleCannonShot(c, impactLine);
        }
        countCannonShot++;

        if (countCannonShot == cannonShot.size() - 1 && winner == null) {
            game.nextCard();
        }
    }

    /**
     * Returns help text based on the current game phase.
     *
     * @return available commands as a string
     */
    public String help() {
        GameStatus status = game.getGameStatus();
        switch (status) {
            case INIT_PIRATES:
                return "Available commands: ACTIVECANNON, READY";
            case END_PIRATES:
                return "Available commands: ACTIVESHIELD, READY, PASS, CREDIT";
            default:
                return "No commands available in current phase.";
        }
    }

    @Override
    public String toString(){
        return
                "è uscita la carta pirates \n";
    }
}
