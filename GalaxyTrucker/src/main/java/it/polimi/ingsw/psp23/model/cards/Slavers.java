package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Events.CosmicCreditsEarned;
import it.polimi.ingsw.psp23.model.Events.EnemyDefeated;
import it.polimi.ingsw.psp23.model.Events.TurnOf;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Utility;
import it.polimi.ingsw.psp23.model.Events.EventForSlavers;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Slavers card effect.
 * Manages the slavers encounter phases including:
 * - INIT_SLAVERS: players activate cannon or ready
 * - END_SLAVERS: losers reduce crew, winner claims prize or passes
 */
public class Slavers extends Card {
    /** required cannon strength to win the encounter */
    private final int cannonStrength;
    /** number of crew members to steal if losers */
    private final int membersStolen;
    /** cosmic credits awarded to the winner */
    private final int prize;
    /** days lost penalty for the winner */
    private final int days;
    /** count of removed crew members */
    private int counterMember = 0;
    /** nickname of the encounter winner */
    private String winner = null;
    /** list of nicknames of players who lost */
    private String loser = null;
    private List<String> noCrew = new ArrayList<>();

    /**
     * Constructs the Slavers card with specified parameters.
     * @param level card level
     * @param cannonStrength required firepower to win
     * @param membersStolen crew members stolen if lost
     * @param prize credits awarded to the winner
     * @param days days lost penalty for the winner
     */
    public Slavers(int level, int cannonStrength, int membersStolen, int prize, int days, int id) {
        super(level, id);
        this.cannonStrength = cannonStrength;
        this.membersStolen = membersStolen;
        this.prize = prize;
        this.days = days;
    }

    /** @return cannon firepower threshold */
    public int getCannonStrength() {
        return cannonStrength;
    }

    /** @return members stolen count */
    public int getMembersStolen() {
        return membersStolen;
    }

    /** @return prize credits */
    public int getPrize() {
        return prize;
    }

    /** @return days lost penalty */
    public int getDays() {
        return days;
    }

    /** @return current winner nickname or null */
    public String getWinner() {
        return winner;
    }

    /** sets the encounter winner */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Override
    public <T> T call(Visitor<T> visitor) {
        return visitor.visitForSlavers(this);
    }

    @Override
    public <T> T call(VisitorUsername<T> visitorUsername, String username) {
        return visitorUsername.visitForSlavers(this, username);
    }

    @Override
    public <T> T call(VisitorCoordinateNum<T> visitorCoordinateNum, String username, int i, int j, int num) {
        return visitorCoordinateNum.visitForSlavers(this, username, i, j, num);
    }

    @Override
    public <T> T call(VisitorCoordinate<T> visitorCoordinate, String username, int i, int j) {
        return visitorCoordinate.visitForSlavers(this, username, i, j);
    }

    /**
     * Starts the slavers encounter phase.
     */
    public void initPlay() {
        Game.getInstance().setGameStatus(GameStatus.INIT_SLAVERS);
        Game.getInstance().fireEvent(new EventForSlavers(
                Game.getInstance().getGameStatus(),
                cannonStrength, membersStolen, prize, days));
        Game.getInstance().setCurrentPlayer(Game.getInstance().getPlayers().getFirst());
        for(Player player : Game.getInstance().getPlayers()) {
            if(player.getTruck().calculateCrew() == 0){
                noCrew.add(player.getNickname());
            }
        }
    }

    /**
     * Activates cannon during INIT_SLAVERS phase.
     * @param username player nickname
     * @param i row index
     * @param j column index
     */
    public void activeCannon(String username, int i, int j) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_SLAVERS) {
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
     * Awards prize and days penalty, resets or ends phase.
     * @param username winner nickname
     */
    public void getCosmicCredits(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.END_SLAVERS) {
            throw new CardException("Winner has not been determined yet");
        }
        if (!username.equals(winner)) {
            throw new CardException("You did not defeat the slavers: " + username);
        }
        game.getPlayerFromNickname(winner).updateMoney(prize);
        int idx = game.getPlayers().indexOf(
                game.getPlayerFromNickname(username));
        Utility.updatePosition(game.getPlayers(), idx, -days);
        game.sortPlayersByPosition();
        game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")));
    }

    /**
     * Winner passes claim, resets or continues.
     * @param username winner nickname
     */
    public void pass(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.END_SLAVERS) {
            throw new CardException("Winner has not been determined yet");
        }
        if (!username.equals(winner)) {
            throw new CardException("You did not defeat the slavers: " + username);
        }
        game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")));
    }

    /**
     * Processes READY command in INIT_SLAVERS.
     * @param username player nickname
     */
    public void ready(String username) {
        Game game = Game.getInstance();
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer().getNickname());
        }
        if (game.getGameStatus() == GameStatus.INIT_SLAVERS) {
            readyStartPhase(username);
        }
        else{
            throw new CardException("Invalid phase for READY:" + game.getGameStatus());
        }
    }

    /**
     * READY logic for INIT_SLAVERS.
     */
    private void readyStartPhase(String username) {
        Game game = Game.getInstance();
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("User '" + username + "' is not the current player");
        }
        double playerFirepower = game.getPlayerFromNickname(username)
                .getTruck()
                .calculateCannonStrength();

        if (playerFirepower > cannonStrength) {
            winner = username;
            game.fireEvent(new EnemyDefeated(game.getGameStatus()));
            game.fireEvent(new CosmicCreditsEarned(game.getGameStatus()), username);
            game.setGameStatus(GameStatus.END_SLAVERS);
        } else if (playerFirepower < cannonStrength){
            loser = username;
            if(noCrew.contains(loser)){
                if(game.getCurrentPlayerIndex() >= game.getPlayers().size() - 1){
                    game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
                    Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")));
                }
                else{
                    game.getNextPlayer();
                    game.fireEvent(new TurnOf(game.getGameStatus(), game.getCurrentPlayer().getNickname()));
                }
            }
            else {
                game.setGameStatus(GameStatus.END_SLAVERS);
            }
        }
        else{
            if(game.getCurrentPlayerIndex() >= game.getPlayers().size() - 1){
                game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
                Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")));
            }
            else{
                game.getNextPlayer();
                game.fireEvent(new TurnOf(game.getGameStatus(), game.getCurrentPlayer().getNickname()));
            }
        }
    }

    /**
     * Removes crew members during END_SLAVERS.
     * @param username loser nickname
     * @param i row index
     * @param j column index
     * @param num members to remove
     */
    public void reduceCrew(String username, int i, int j, int num) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.END_SLAVERS) {
            throw new CardException("User '" + username + "' cannot remove crew in phase: " + game.getGameStatus());
        }
        if (!loser.equals(username)) {
            throw new CardException("User '" + username + "' is not loser");
        }
        Board board = game.getPlayerFromNickname(username).getTruck();
        try{
            board.reduceCrew(i, j, num);
            counterMember += num;
            if(counterMember == membersStolen || board.calculateCrew() == 0){
                if(game.getCurrentPlayerIndex() >= (game.getPlayers().size() - 1)){
                    game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
                    Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")));
                }
                else{
                    game.setGameStatus(GameStatus.INIT_SLAVERS);
                    loser = null;
                    counterMember = 0;
                    game.getNextPlayer();
                    game.fireEvent(new TurnOf(game.getGameStatus(), game.getCurrentPlayer().getNickname()));
                }
            }
        }
        catch (InvalidCoordinatesException | ComponentMismatchException | CrewOperationException |
               TypeMismatchException e){
            throw new CrewOperationException("Errore nella scelta dell'equipaggio", e);
        }
    }

    /**
     * Returns help text for current game phase.
     * @return available commands string
     */
    public String help() {
        Game game = Game.getInstance();
        GameStatus status = game.getGameStatus();
        switch (status) {
            case INIT_SLAVERS:
                return "Available commands: ACTIVECANNON, READY\n";
            case END_SLAVERS:
                return "Available commands: CREW, PASS, CREDIT\n";
            default:
                return "No commands available in current phase.\n";
        }
    }

    @Override
    public String toString(){
        return
                "è uscita la carta Slavers\n" +
                "la potenza di fuoco è " + getCannonStrength() +"\n" +
                "i membri eventualmente persi sono " +getMembersStolen() +"\n" +
                "i giorni persi sarebbero " + getDays() + "\n" +
                "i crediti cosmici sono " + getPrize() +"\n";
    }
}
