package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Events.AbandonedStationOccupation;
import it.polimi.ingsw.psp23.model.Events.ItemsEarned;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.helpers.Item;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.helpers.Utility;
import it.polimi.ingsw.psp23.model.Events.EventForAbandonedStation;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

import java.util.List;

public class AbandonedStation extends Card {
    private final int days;
    private final int numMembers;
    private final List<Item> prize;
    private String isSold = null;
    private int counterItem = 0;

    public AbandonedStation(int level, int days, int numMembers, List<Item> prize, int id) {
        super(level, id);
        this.days = days;
        this.numMembers = numMembers;
        this.prize = prize;
    }

    public int getDays() {
        return days;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public void initPlay(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        game.setGameStatus(GameStatus.INIT_ABANDONEDSTATION);
        game.fireEvent(new EventForAbandonedStation(game.getGameStatus(), days, numMembers, prize));
        game.setCurrentPlayer(game.getPlayers().getFirst());
    }

    /**
     * Allows the player to dock at the station during INIT_ABANDONEDSTATION.
     * Throws CardException if called in the wrong phase or by a non-current player, or if crew is insufficient.
     */
    public void dockStation(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(isSold != null){
            throw new CardException("Station was docked by " + isSold );
        }
        if (game.getGameStatus() != GameStatus.INIT_ABANDONEDSTATION) {
            throw new CardException("User '" + username + "' cannot dock the station in phase: " + game.getGameStatus());
        }
        if (!username.equals(game.getCurrentPlayer().getNickname())) {
            throw new CardException("User '" + username + "' is not the current player");
        }
        Player p = game.getPlayerFromNickname(username);
        int crew = p.getTruck().calculateCrew();
        if (crew < numMembers) {
            throw new CardException("User '" + username + "' can't dock station, he does not have enough crew");
        }
        isSold = username;
        game.fireEvent(new AbandonedStationOccupation(game.getGameStatus()));
        Utility.updatePosition(game.getPlayers(), game.getPlayers().indexOf(p), -days);
        game.sortPlayersByPosition();
        game.fireEvent(new ItemsEarned(game.getGameStatus()), isSold);
        game.setGameStatus(GameStatus.END_ABANDONEDSTATION);
        game.setCurrentPlayer(game.getPlayerFromNickname(isSold));
    }

    /**
     * Loads prize items into containers during END_ABANDONEDSTATION.
     * Throws ContainerException for container-specific failures, or CardException if used in wrong phase or by wrong user.
     */
    public void loadGoods(String username, int i, int j) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (game.getGameStatus() != GameStatus.END_ABANDONEDSTATION) {
            throw new CardException("Cannot load goods in phase: " + game.getGameStatus());
        }
        if (!username.equals(isSold)) {
            throw new CardException("User '" + username + "' did not dock the station");
        }
        try {
            Board board = game.getPlayerFromNickname(username).getTruck();
            board.loadGood(prize.get(counterItem), i, j);
            counterItem++;
            if (counterItem == prize.size()) {
                game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
                Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")), game.getId());
            }
        }
        catch (InvalidCoordinatesException | ComponentMismatchException | ContainerException |
               TypeMismatchException e){
            throw new ItemException("Caricamento non valido", e);
        }
    }

    /**
     * Allows passing during INIT_ABANDONEDSTATION: moves to next player or next card.
     * Allows passing during END_ABANDONEDSTATION: moves to next card.
     */
    public void pass(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer().getNickname());
        }
        if (game.getGameStatus() == GameStatus.END_ABANDONEDSTATION){
            game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")), game.getId());
        }
        else{
            if (game.getCurrentPlayerIndex() < game.getPlayers().size() - 1) {
                game.getNextPlayer();
//            String currentPlayerNickname = game.getCurrentPlayer().getNickname();
//            game.fireEvent(new TurnOf(game.getGameStatus(), currentPlayerNickname));
            } else {
                game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
                Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")), game.getId());
            }
        }
    }

    /**
     * Returns available commands based on current AbandonedStation phase.
     * @return help message
     */
    public String help(String username) {
        GameStatus status = UsersConnected.getInstance().getGameFromUsername(username).getGameStatus();
        return switch (status) {
            case INIT_ABANDONEDSTATION -> "Available commands: ATTRACCA, PASSA\n";
            case END_ABANDONEDSTATION -> "Available commands: LOADGOODS, PERDI, PASSA\n";
            default -> "No commands available in current phase.\n";
        };
    }

    @Override
    public <T> T call(Visitor<T> visitor) {
        return visitor.visitForAbandonedStation(this);
    }

    @Override
    public <T> T call(VisitorUsername<T> visitorUsername, String username) {
        return visitorUsername.visitForAbandonedStation(this, username);
    }

    @Override
    public <T> T call(VisitorCoordinate<T> visitorUsernameCoordinate, String username, int i, int j) {
        return visitorUsernameCoordinate.visitForAbandonedStation(this, username, i, j);
    }

    @Override
    public String toString(){
        return
                "è uscita la carta Abandoned Station\n" +
                        "i membri richiesti sono "+ getNumMembers() +
                        "\nle merci disponibili sono "+ prize.toString() +
                        "\ni giorni persi sarebbero " + getDays();
    }
}
