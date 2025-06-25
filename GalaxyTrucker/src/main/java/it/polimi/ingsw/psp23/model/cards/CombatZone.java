package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.model.Events.CannonShotIncoming;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.helpers.CannonShot;
import it.polimi.ingsw.psp23.model.helpers.Utility;
import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Events.EventForCombatZone;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.Challenge;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Card representing a combat encounter where players undergo three sequential challenges
 * (cannon strength, engine strength, and final penalty resolution).
 */
public class CombatZone extends Card {
    private final int daysLost;
    private final int goodsLost;
    private final int membersLost;
    private final List<CannonShot> cannonShot;
    private final List<Challenge> penalties;

    private int countMember;
    private int countGood;
    private String loserSecondChallenge;
    private String loserThirdChallenge;
    private int cannonShotIndex;
    private final Set<Player> resolvers = new HashSet<>();
    private List<String> noGoods = new ArrayList<>();
    private List<String> noCrew = new ArrayList<>();

    /**
     * Constructs a CombatZone with specified difficulty and penalty sequence.
     *
     * @param level       difficulty level of the card
     * @param daysLost    days to deduct from the loser of the first challenge
     * @param goodsLost   goods to remove from the loser of the second challenge
     * @param membersLost crew members to remove in the second challenge
     * @param penalties   ordered list of challenges to apply
     * @param cannonShot  sequence of cannon shots for final resolution
     */
    public CombatZone(int level, int daysLost, int goodsLost, int membersLost,
                      List<Challenge> penalties, List<CannonShot> cannonShot, int id) {
        super(level, id);
        this.daysLost       = daysLost;
        this.goodsLost      = goodsLost;
        this.membersLost    = membersLost;
        this.penalties      = penalties;
        this.cannonShot     = cannonShot;
        this.countMember    = 0;
        this.countGood      = 0;
        this.loserSecondChallenge = null;
        this.loserThirdChallenge  = null;
        this.cannonShotIndex      = 0;
    }

    /**
     * Returns a copy of the cannon shot sequence for final resolution.
     *
     * @return list of CannonShot objects
     */
    public List<CannonShot> getCannonShot() {
        return new ArrayList<>(cannonShot);
    }

    /**
     * Finds the player with the smallest crew size among all players.
     *
     * @return Player with minimum crew count
     */
    private Player findMinMembers(List<Player> players) {
        // List<Player> players = UsersConnected.getInstance().getGameFromUsername(username).getPlayers();
        int minCrew = players.getFirst().getTruck().calculateCrew();
        Player minPlayer = players.getFirst();
        for (Player p : players) {
            int crew = p.getTruck().calculateCrew();
            if (crew < minCrew) {
                minPlayer = p;
                minCrew = crew;
            }
        }
        return minPlayer;
    }

    /**
     * Finds the player with the lowest cannon strength.
     *
     * @return Player with minimum cannon strength
     */
    private Player findMinCannonStrength(List<Player> players) {
        // List<Player> players = UsersConnected.getInstance().getGameFromUsername(username).getPlayers();
        double minStrength = players.getFirst().getTruck().calculateCannonStrength();
        Player minPlayer = players.getFirst();
        for (Player p : players.subList(1, players.size())) {
            double strength = p.getTruck().calculateCannonStrength();
            if (strength < minStrength) {
                minPlayer = p;
                minStrength = strength;
            }
        }
        return minPlayer;
    }

    /**
     * Finds the player with the lowest engine strength.
     *
     * @return Player with minimum engine strength
     */
    private Player findMinEngineStrength(List<Player> players) {
        // List<Player> players = UsersConnected.getInstance().getGameFromUsername(username).getPlayers();
        int minPower = players.getFirst().getTruck().calculateEngineStrength();
        Player minPlayer = players.getFirst();
        for (Player p : players.subList(1, players.size())) {
            int power = p.getTruck().calculateEngineStrength();
            if (power < minPower) {
                minPlayer = p;
                minPower = power;
            }
        }
        return minPlayer;
    }

    /**
     * Activates a cannon tile for the current player when allowed.
     * Applicable in the first or third combat phase if cannon strength is penalty.
     *
     * @param username nickname of the active player
     * @param i        row index of the tile
     * @param j        column index of the tile
     * @throws CardException if not in correct phase or not the player's turn
     */
    public void activeCannon(String username, int i, int j) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        GameStatus status = game.getGameStatus();
        if (!(status == GameStatus.FIRST_COMBATZONE ||
                (status == GameStatus.THIRD_COMBATZONE && penalties.get(2) == Challenge.CannonStrength))) {
            throw new CardException("Cannot activate cannon now: phase is " + game.getGameStatus());
        }
        if (!username.equals(game.getCurrentPlayer().getNickname())) {
            throw new CardException("Not " + username + "'s turn");
        }
        game.getPlayerFromNickname(username).getTruck().activeCannon(i, j);
    }

    /**
     * Activates a shield tile by the designated player in the third combat phase.
     *
     * @param username nickname of the shield-activating player
     * @param i        row index of the tile
     * @param j        column index of the tile
     * @throws CardException if not in correct phase or wrong player
     */
    public void activeShield(String username, int i, int j) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (game.getGameStatus() != GameStatus.ENDTHIRD_COMBATZONE) {
            throw new CardException("Cannot activate shield now: phase is "  + game.getGameStatus());
        }
        if (!loserThirdChallenge.equals(username)) {
            throw new CardException("The loser of the third challenge is " + loserThirdChallenge);
        }
        game.getPlayerFromNickname(username).getTruck().activeShield(i, j);
    }

    /**
     * Activates an engine tile by the losing player in the third combat phase.
     *
     * @param username nickname of the engine-activating player
     * @param i        row index of the tile
     * @param j        column index of the tile
     * @throws CardException if not in correct phase or wrong player
     */
    public void activeEngine(String username, int i, int j) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (game.getGameStatus() != GameStatus.SECOND_COMBATZONE) {
            throw new CardException("Cannot activate engine now: phase is " + game.getGameStatus());
        }
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer().getNickname());
        }
        game.getPlayerFromNickname(username).getTruck().activeEngine(i, j);
    }

    /**
     * Applies one cannon shot to the losing player based on random impact line.
     * Advances to Playing status after the final shot.
     */
    private void handleCannonShot(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (!loserThirdChallenge.equals(username)) {
            throw new CardException("The loser of the third challenge is" + loserThirdChallenge);
        }
        if(cannonShot.size() - cannonShotIndex == 2){
            for(CannonShot c : cannonShot.subList(cannonShotIndex, cannonShot.size())){
                int impactLine = Utility.roll2to12();
                game.fireEvent(new CannonShotIncoming(game.getGameStatus(), c.isBig(), impactLine, c.getDirection()), loserThirdChallenge);
                game.getPlayerFromNickname(loserThirdChallenge)
                        .getTruck().handleCannonShot(c, impactLine);
            }
            game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")), game.getId());
        }
        else{
            CannonShot shot = cannonShot.get(cannonShotIndex);
            int impactLine = Utility.roll2to12();
            game.fireEvent(new CannonShotIncoming(game.getGameStatus(), shot.isBig(), impactLine, shot.getDirection()), loserThirdChallenge);
            game.getPlayerFromNickname(loserThirdChallenge)
                    .getTruck().handleCannonShot(shot, impactLine);
            cannonShotIndex++;
        }
    }

    /**
     * Removes crew members from the specified housing unit as penalty.
     * Moves to third phase when required removals complete.
     *
     * @param username losing player's nickname
     * @param i        row index of the tile
     * @param j        column index of the tile
     * @param num      number of members to remove
     * @throws CardException if not in correct phase, wrong player, or invalid tile
     */
    public void reduceCrew(String username, int i, int j, int num) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (game.getGameStatus() != GameStatus.SECOND_COMBATZONE || membersLost <= 0) {
            throw new CardException("It's not required to remove crew");
        }
        if (!username.equals(loserSecondChallenge)) {
            throw new CardException("User '" + username + "' is not the loser of the second challenge");
        }
        Board board = game.getPlayerFromNickname(username).getTruck();
        try{
            board.reduceCrew(i, j, num);
            countMember += num;
            if (countMember == membersLost || board.calculateCrew() == 0) {
                if (penalties.get(2) == Challenge.Members) {
                    loserThirdChallenge = findMinMembers(game.getPlayers()).getNickname();
                }
                game.setGameStatus(GameStatus.THIRD_COMBATZONE);
            }
        }
        catch (InvalidCoordinatesException | ComponentMismatchException | CrewOperationException | TypeMismatchException e){
            throw new CrewOperationException("Errore nella scelta dell'equipaggio", e);
        }
    }

    /**
     * Removes the most valuable item from the specified container as penalty.
     * Moves to third phase when required removals complete.
     *
     * @param username losing player's nickname
     * @param i        row index of the tile
     * @param j        column index of the tile
     * @param num     index of the item within the container
     * @throws CardException if not in correct phase, wrong player, or wrong tile/item
     */
    public void removePreciousItem(String username, int i, int j, int num) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (game.getGameStatus() != GameStatus.SECOND_COMBATZONE || goodsLost <= 0) {
            throw new CardException("It's not required to remove goods");
        }
        if (!username.equals(loserSecondChallenge)) {
            throw new CardException("User '" + username + "' is not the loser of the second challenge");
        }
        if(game.getPlayerFromNickname(username).getTruck().calculateGoods() < num){
            throw new CardException("You can only lose batteries");
        }
        try {
            Board board = game.getPlayerFromNickname(username).getTruck();
            board.removePreciousItem(i, j, num);
            countGood += num;
            if (countGood == goodsLost || (board.calculateGoods() == 0 && board.calculateBatteriesAvailable() == 0)) {
                resolvers.clear();
                loserThirdChallenge = findMinMembers(game.getPlayers()).getNickname();
                game.setGameStatus(GameStatus.ENDTHIRD_COMBATZONE);
            }
        }
        catch (IllegalArgumentException | ComponentMismatchException | ContainerException |
               TypeMismatchException e){
            throw new ItemException("Scaricamento non valido", e);
        }
    }

    public void removeBatteries(String username, int i, int j, int num){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (game.getGameStatus() != GameStatus.SECOND_COMBATZONE || goodsLost <= 0) {
            throw new CardException("It's not required to remove goods");
        }
        if (!username.equals(loserSecondChallenge)) {
            throw new CardException("User '" + username + "' is not the loser of the second challenge");
        }
        if(game.getPlayerFromNickname(username).getTruck().calculateGoods() > 0){
            throw new CardException("You have to lose items first");
        }
        try {
            Board board = game.getPlayerFromNickname(username).getTruck();
            board.reduceBatteries(i, j, num);
            countGood += num;
            if (countGood == goodsLost || board.calculateBatteriesAvailable() == 0) {
                resolvers.clear();
                loserThirdChallenge = findMinMembers(game.getPlayers()).getNickname();
                game.setGameStatus(GameStatus.ENDTHIRD_COMBATZONE);
            }
        }
        catch (InvalidCoordinatesException | ComponentMismatchException | BatteryOperationException |
               TypeMismatchException e){
            throw new ItemException("Perdita batterie non valido", e);
        }
    }

    /**
     * Accepts a visitor according to the visitor pattern.
     *
     * @param visitor visitor to handle this card type
     * @return result of the visitor method
     */
    @Override
    public <T> T call(Visitor<T> visitor) {
        return visitor.visitForCombatZone(this);
    }


    public <T> T call(VisitorCoordinateNum<T> visitorCoordinateNum, String username, int i, int j, int num) {
        return visitorCoordinateNum.visitForCombatZone(this, username, i, j, num);
    }

    public <T> T call(VisitorCoordinate<T> visitorCoordinate, String username, int i, int j) {
        return visitorCoordinate.visitForCombatZone(this, username, i, j);
    }

    public <T> T call(VisitorUsername<T> visitorUsername, String username) {
        return visitorUsername.visitForCombatZone(this, username);
    }

    /**
     * Fires a combat event and sets the initial game phase based on the first penalty.
     */
    public void initPlay(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        game.setGameStatus(GameStatus.INIT_COMBATZONE);
        game.fireEvent(new EventForCombatZone(
                game.getGameStatus(), daysLost, goodsLost, membersLost, penalties, cannonShot));
        for(Player p : game.getPlayers()){
            if(p.getTruck().calculateCrew() == 0){
                noCrew.add(p.getNickname());
            }
            // if(p.getTruck().calculateGoods() + p.getTruck().calculateBatteriesAvailable() < goodsLost){
            if(p.getTruck().calculateGoods() == 0 && p.getTruck().calculateBatteriesAvailable() == 0){
                noGoods.add(p.getNickname());
            }
        }
        if (penalties.getFirst() == Challenge.CannonStrength) {
            game.setGameStatus(GameStatus.FIRST_COMBATZONE);
            game.setCurrentPlayer(game.getPlayers().getFirst());
        } else if (penalties.getFirst() == Challenge.Members) {
            Player minPlayer = findMinMembers(game.getPlayers());
            Utility.updatePosition(game.getPlayers(), game.getPlayers().indexOf(minPlayer), -daysLost);
            game.sortPlayersByPosition();
            game.setCurrentPlayer(game.getPlayers().getFirst());
            game.setGameStatus(GameStatus.SECOND_COMBATZONE);
        }
    }

    /**
     * Handles the first challenge where players compare cannon strength.
     * Moves to second phase after processing all players.
     *
     * @param username nickname of player signaling completion
     * @throws CardException if not the current player's turn
     */
    private void firstChallenge(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("User '" + username + "' is not the current player");
        }
        Player p = game.getPlayerFromNickname(username);
        if(resolvers.contains(p)){
            throw new CardException("You must wait other players!");
        }
        resolvers.add(p);
        if (!resolvers.containsAll(game.getPlayers())) {
            game.getNextPlayer(); // wait for all
        }
        else{
            Player minPlayer = findMinCannonStrength(game.getPlayers());
            Utility.updatePosition(game.getPlayers(), game.getPlayers().indexOf(minPlayer), -daysLost);
            game.sortPlayersByPosition();
            game.setCurrentPlayer(game.getPlayers().getFirst());
            resolvers.clear();
            game.setGameStatus(GameStatus.SECOND_COMBATZONE);
        }
    }

    /**
     * Handles the second challenge where players compare engine strength.
     * Determines the loser but does not change phase.
     *
     * @param username nickname of player signaling completion
     * @throws CardException if not the current player's turn
     */
    private void secondChallenge(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("User '" + username + "' is not the current player");
        }
        Player p = game.getPlayerFromNickname(username);
        if(resolvers.contains(p)){
            throw new CardException("You must wait other players!");
        }
        resolvers.add(p);
        if (!resolvers.containsAll(game.getPlayers())) {
            game.getNextPlayer(); // wait for all
        }
        else{
            resolvers.clear();
            loserSecondChallenge = findMinEngineStrength(game.getPlayers()).getNickname();
            if((noCrew.contains(loserSecondChallenge) && membersLost > 0) || (noGoods.contains(loserSecondChallenge) && goodsLost > 0)){
                game.setGameStatus(GameStatus.THIRD_COMBATZONE);
            }
            game.setCurrentPlayer(game.getPlayers().getFirst());
        }
    }

    /**
     * Handles the third challenge or initiates final cannon resolution.
     * Moves to final penalty phase after processing all players.
     *
     * @param username nickname of player signaling completion
     * @throws CardException if not the current player's turn
     */
    private void thirdChallenge(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("User '" + username + "' is not the current player");
        }
        Player p = game.getPlayerFromNickname(username);
        if(resolvers.contains(p)){
            throw new CardException("You must wait other players!");
        }
        resolvers.add(p);
        if (!resolvers.containsAll(game.getPlayers())) {
            game.getNextPlayer(); // wait for all
        }
        else{
            loserThirdChallenge = findMinEngineStrength(game.getPlayers()).getNickname();
            game.setCurrentPlayer(game.getPlayers().getFirst());
            game.setGameStatus(GameStatus.ENDTHIRD_COMBATZONE);
        }
    }

    /**
     * Provides a help string listing available commands based on current phase.
     *
     * @return help text for UI display
     */
    public String help(String username) {
        GameStatus status = UsersConnected.getInstance().getGameFromUsername(username).getGameStatus();
        return switch (status) {
            case FIRST_COMBATZONE   -> "Available commands: ATTIVACANNONE, READY\n";
            case SECOND_COMBATZONE  -> "Available commands: ATTIVAMOTORE, READY, REMOVEITEM, CREW\n";
            case THIRD_COMBATZONE   -> "Available commands: ATTIVAMOTORE, READY\n";
            case ENDTHIRD_COMBATZONE -> "Available commands: ATTIVASCUDO, READY\n";
            default                 -> "No commands available in current phase.\n";
        };
    }

    /**
     * Signals readiness in current combat phase and dispatches to appropriate handler.
     *
     * @param username nickname of player signaling readiness
     */
    public void ready(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        GameStatus status = game.getGameStatus();

        if (status == GameStatus.FIRST_COMBATZONE) {
            firstChallenge(username);
        } else if (status == GameStatus.SECOND_COMBATZONE) {
            secondChallenge(username);
        } else if (status == GameStatus.THIRD_COMBATZONE
                && penalties.get(2) == Challenge.CannonStrength) {
            thirdChallenge(username);
        } else if (status == GameStatus.ENDTHIRD_COMBATZONE) {
            handleCannonShot(username);
        }
        else {
            throw new CardException("Invalid phase for READY: " + game.getGameStatus());
        }
    }

    @Override
    public String toString(){
        if(penalties.get(0) == Challenge.CannonStrength){
            return
                    "è uscita la carta Combat Zone:\n La prima sfida riguarda la potenza di fuoco (penalità: 4 giorni)\n" +
                            "la seconda sfida riguarda la potenza motrice (penalità: 3 merci importanti)\n" +
                            "la terza sfida riguarda i membri dell'equipaggio (penalità colpi di cannone: "+ cannonShot.toString() + "\n";
        }else{
            return
                    "è uscita la carta Combat Zone:\n La prima sfida riguarda i membri dell'equipaggio (penalità: 3 giorni)\n" +
                            "la seconda sfida riguarda la potenza motrice (penalità: 2 membri dell'equipaggio)\n" +
                            "la terza sfida riguarda la potenza di fuoco (penalità colpi di cannone: " + cannonShot.toString() +" \n";
        }
    }
}
