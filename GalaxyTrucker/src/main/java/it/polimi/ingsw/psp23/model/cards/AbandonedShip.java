package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Events.AbandonedShipOccupation;
import it.polimi.ingsw.psp23.model.Events.CosmicCreditsEarned;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.helpers.Utility;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

public class AbandonedShip extends Card {
    private final int days;
    private final int cosmicCredits;
    private final int numMembers;
    private int countMember;
    private String isSold;

    public AbandonedShip(int level, int days, int cosmicCredits, int numMembers, int id) {
        super(level,id);
        this.days = days;
        this.cosmicCredits = cosmicCredits;
        this.numMembers = numMembers;
        this.countMember = 0;
        this.isSold = null;
    }

    public int getDays() {
        return days;
    }

    public int getCosmicCredits() {
        return cosmicCredits;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public void initPlay(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        game.setGameStatus(GameStatus.INIT_ABANDONEDSHIP);
        game.setCurrentPlayer(game.getPlayers().getFirst());
    }

    /**
     * Allows the current player to pass their decision.
     * If not the last player, advances to the next player's turn.
     * If the last player, skips to the next card.
     */
    public void pass(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (game.getGameStatus() != GameStatus.INIT_ABANDONEDSHIP) {
            throw new CardException(username + " non può comprare la nave nella fase: " + game.getGameStatus());
        }
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("E' il turno di " + game.getCurrentPlayer().getNickname());
        }
        if (game.getCurrentPlayerIndex() < game.getPlayers().size() - 1) {
            game.getNextPlayer();
            // String currentPlayerNickname = game.getCurrentPlayer().getNickname();
            // game.fireEvent(new TurnOf(game.getGameStatus(), currentPlayerNickname));
        } else {
            game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")), game.getId());
        }
    }

    /**
     * Verifies that the game is in the INIT_ABANDONEDSHIP phase before allowing purchase.
     * Ensures that only the player whose turn it is can buy the ship.
     * Throws a CardException if conditions are not met.
     */
    public void buyShip(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(isSold != null){
            throw new CardException("La nave è stata già comprata da " + isSold );
        }
        if (game.getGameStatus() != GameStatus.INIT_ABANDONEDSHIP) {
            throw new CardException(username + " non può comprare la nave nella fase: " + game.getGameStatus());
        }
        if (!username.equals(game.getCurrentPlayer().getNickname())) {
            throw new CardException(username + " non è il giocatore corrente");
        }
        Player p = game.getPlayerFromNickname(username);
        int crew = p.getTruck().calculateCrew();
        if (crew < numMembers) {
            throw new CardException(username + "non può comprare la nave, non ha abbastanza equipaggio");
        }
        isSold = username;
        game.fireEvent(new AbandonedShipOccupation(game.getGameStatus()));
        game.fireEvent(new CosmicCreditsEarned(game.getGameStatus()), username);
        Utility.updatePosition(game.getPlayers(), game.getPlayers().indexOf(p), -days);
        game.sortPlayersByPosition();
        p.updateMoney(cosmicCredits);
        game.fireEvent(new CosmicCreditsEarned(game.getGameStatus()), isSold);
        game.setGameStatus(GameStatus.END_ABANDONEDSHIP);
        game.setCurrentPlayer(game.getPlayerFromNickname(isSold));
    }

    /**
     * Verifies that the game is in the END_ABANDONEDSHIP phase before allowing crew removal.
     * Ensures that only the buyer can remove crew members from housing units.
     * Throws a CardException if conditions are not met.
     */
    public void reduceCrew(String username, int i, int j, int num) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (game.getGameStatus() != GameStatus.END_ABANDONEDSHIP) {
            throw new CardException(username + " non può rimuovere la crew nella fase: " + game.getGameStatus());
        }
        if (!username.equals(isSold)) {
            throw new CardException(username + " non ha comprato la nave");
        }
        Board board = game.getPlayerFromNickname(username).getTruck();
        try{
            board.reduceCrew(i, j, num);
            countMember += num;
            if (countMember == numMembers) {
                game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
                Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")), game.getId());
            }
        }
        catch (InvalidCoordinatesException | ComponentMismatchException | CrewOperationException | TypeMismatchException e){
            throw new CrewOperationException("Errore nella scelta dell'equipaggio", e);
        }
    }

    /**
     * Returns a help string listing available commands depending on current game status.
     * @return help message
     */
    public String help(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        GameStatus status = game.getGameStatus();
        return switch (status) {
            case INIT_ABANDONEDSHIP -> "Available commands: COMPRANAVE, PASSA\n";
            case END_ABANDONEDSHIP -> "Available commands: REDUCECREW\n";
            default -> "No commands available in current phase.\n";
        };
    }

    @Override
    public <T> T call(Visitor<T> visitor) {
        return visitor.visitForAbandonedShip(this);
    }

    @Override
    public <T> T call(VisitorUsername<T> visitorUsername, String username) {
        return visitorUsername.visitForAbandonedShip(this, username);
    }

    @Override
    public <T> T call(VisitorCoordinateNum<T> visitorUsernameCoordinateNum, String username, int i, int j, int num) {
        return visitorUsernameCoordinateNum.visitForAbandonedShip(this, username, i, j, num);
    }

    @Override
    public String toString(){
        return
                "è uscita la carta Abandoned Ship\n" +
                        "l'equipaggio da perdere ammonta a "+ getNumMembers() +
                "\ni crediti cosmici disponibili sono "+ getCosmicCredits() +
                "\ni giorni persi sarebbero " + getDays() + "\n";
    }



}
