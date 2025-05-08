package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Utility;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.exceptions.ContainerException;
import it.polimi.ingsw.psp23.model.Events.EventForAbandonedStation;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.Container;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.List;

public class AbandonedStation extends Card {
    private final int days;
    private final int numMembers;
    private final List<Item> prize;
    private String isSold = null;
    private int counterItem = 0;

    public AbandonedStation(int level, int days, int numMembers, List<Item> prize) {
        super(level);
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

    public List<Item> getPrize() {
        return new ArrayList<>(prize);
    }

    public void initPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_ABANDONEDSTATION);
        game.fireEvent(new EventForAbandonedStation(game.getGameStatus(), days, numMembers, prize));
    }

    /**
     * Allows the player to dock at the station during INIT_ABANDONEDSTATION.
     * Throws CardException if called in the wrong phase or by a non-current player, or if crew is insufficient.
     */
    public void dockStation(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_ABANDONEDSTATION) {
            throw new CardException("Cannot dock station in phase: " + game.getGameStatus());
        }
        if (!username.equals(game.getPlayers().get(game.getTurn()))) {
            throw new CardException("User '" + username + "' is not the current player");
        }
        Player p = game.getPlayerFromNickname(username);
        if (p.getTruck().calculateCrew() < numMembers) {
            throw new CardException("Not enough crew to dock station");
        }
        isSold = username;
        Utility.updatePosition(game.getPlayers(), game.getPlayers().indexOf(p), -days);
        game.setGameStatus(GameStatus.END_ABANDONEDSTATION);
    }

    /**
     * Loads prize items into containers during END_ABANDONEDSTATION.
     * Throws ContainerException for container-specific failures, or CardException if used in wrong phase or by wrong user.
     */
    public void loadGoods(String username, int i, int j) throws ContainerException {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.END_ABANDONEDSTATION) {
            throw new CardException("Cannot load goods in phase: " + game.getGameStatus());
        }
        if (!username.equals(isSold)) {
            throw new CardException("User '" + username + "' did not dock the station");
        }
        Board board = game.getPlayerFromNickname(username).getTruck();
        if (!board.isValid(i, j) || board.isFree(i, j)) {
            throw new CardException("Invalid coordinates or empty cell: [" + i + "][" + j + "]");
        }
        Component tile = board.getShip()[i][j];
        switch (tile) {
            case Container container -> {
                int idx = board.getContainers().indexOf(container);
                if (idx == -1) {
                    throw new CardException("Container not found in list");
                }
                try {
                    container.loadItem(prize.get(counterItem));
                    counterItem++;
                    if (counterItem == prize.size()) {
                        game.getNextCard();
                    }
                } catch (ContainerException e) {
                    throw new ContainerException(
                            "Cannot load item " + counterItem + " into container at [" + i + "][" + j + "]: " + e.getMessage()
                    );
                }
            }
            default -> throw new CardException("Component at [" + i + "][" + j + "] is not a container");
        }
    }

    /**
     * Allows passing during INIT_ABANDONEDSTATION: moves to next player or next card.
     */
    public void pass(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_ABANDONEDSTATION) return;

        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer().getNickname());
        }

        if (game.getTurn() < game.getPlayers().size() - 1) {
            game.getNextPlayer();
        } else {
            game.setGameStatus(GameStatus.END_ABANDONEDSTATION);
        }
    }

    /**
     * Returns available commands based on current AbandonedStation phase.
     * @return help message
     */
    public String help() {
        GameStatus status = Game.getInstance().getGameStatus();
        return switch (status) {
            case INIT_ABANDONEDSTATION -> "Available commands: DOCKSTATION, PASSA";
            case END_ABANDONEDSTATION -> "Available commands: LOADGOODS";
            default -> "No commands available in current phase.";
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
