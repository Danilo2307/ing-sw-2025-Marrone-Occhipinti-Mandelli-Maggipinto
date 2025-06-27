package it.polimi.ingsw.psp23.controller;

import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.visitor.*;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The Controller class manages the game lifecycle, player interactions, and game state transitions.
 * It acts as an intermediary between the player's actions and the game logic, ensuring event handling
 * and updates are correctly propagated among game participants and server observers.
 */
public class Controller {
    private Timer timer;
    private boolean isFirstBuildingPhaseEnded; // variabile che serve all'handle timeout per capire se la clessidra deve ancora essere girata
    private int currentPosition;
    List<Player> crewPositioned = new ArrayList<>();
    int gameId;
    Boolean isTimerRunning = false;


    /**
     * Constructs a new Controller for the game identified by the given game ID.
     * Initializes internal fields and sets up event listeners for the specified game.
     *
     * @param gameId the unique identifier of the game this Controller will manage
     */
    public Controller(int gameId) {
        timer = new Timer();
        isFirstBuildingPhaseEnded = false;
        currentPosition = 0;
        this.gameId = gameId;
        Server.getInstance().getGame(gameId).setEventListener(this::onGameEvent);
        Server.getInstance().getGame(gameId).setEventListener2(this::onGameEventString);
    }

    /**
     * Adds a player to the game identified by the associated game ID.
     * This method checks if the game is currently in the setup phase and if the maximum
     * number of players has not been reached. If the conditions are met, the player with
     * the specified nickname is added to the game. Throws an exception if the player already
     * exists or if the game has reached its maximum capacity.
     *
     * @param nickname the unique nickname of the player to be added to the game
     * @throws PlayerExistsException if a player with the specified nickname already exists in the game
     * @throws GameFullException if the game has reached its maximum allowed number of players
     */
    public void addPlayerToGame(String nickname) throws PlayerExistsException, GameFullException {
        Game game = Server.getInstance().getGame(gameId);

        if(game.getGameStatus() == GameStatus.Setup) {
            if (game.getPlayers().size() <= game.getNumRequestedPlayers() || game.getNumRequestedPlayers() == -1)
                game.addPlayer(nickname);
        }
    }

    /**
     * Assigns a color to the specified player based on the provided count.
     * The color is determined by the count value, where each value corresponds
     * to a specific predefined color (e.g., Blue, Green, Red, Yellow).
     *
     * @param player the player to whom the color will be assigned
     * @param count an integer representing the index of the color to assign
     */
    private void setPlayersColors(Player player, int count) {
        switch (count) {
            case 0 -> player.setColor(Color.Blue);
            case 1 -> player.setColor(Color.Green);
            case 2 -> player.setColor(Color.Red);
            case 3 -> player.setColor(Color.Yellow);
        }
    }

    /**
     * Initiates the building phase of the game by setting up the initial state for all players
     * and transitioning the game to the building phase. This includes assigning a central cabin
     * to each player's truck, setting their colors, notifying all players of the game state change,
     * and, if applicable, starting a timer for this phase.
     *
     * The method performs the following steps:
     * 1. Retrieves the game instance using the associated game ID.
     * 2. Iterates through all players in the game:
     *    - Creates a central cabin as the first housing unit for each player.
     *    - Assigns the cabin to the player's truck at a default position.
     *    - Assigns a unique color to each player based on their index.
     *    - Sends the cabin's details to the respective player using a direct message.
     * 3. Notifies all players of the connected users in the game.
     * 4. Updates the game status to the building phase and notifies players of the state change.
     * 5. Starts a timer for the building phase if the game level is not zero.
     */
    public void startBuildingPhase() {
        Game game = Server.getInstance().getGame(gameId);
        int count = 0;
        for (Player player : game.getPlayers()) {
            HousingUnit centralCabin = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true, count+900);
            player.getTruck().addComponent(centralCabin,2,3);
            setPlayersColors(player, count);
            Server.getInstance().sendMessage(player.getNickname(), new DirectMessage(new TileResponse(centralCabin)));
            count++;
        }//questo for inizializza la cabina centrale dei player con la prima housing unit

        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringList(UsersConnected.getInstance().getClients(gameId))), gameId);
        game.setGameStatus(GameStatus.Building);
        // game.getPlayers().forEach(player -> {
           // inizializzazioneNave.getInstance().popolaNave(player);});
        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StateChanged(GameStatus.Building)), gameId);


        if (game.getLevel() != 0) {
            startTimer();
        }
    }

    /**
     * Starts the timer for the current game session.
     * This method is responsible for initiating a countdown timer for the game
     * and specifying the behavior in case of a timeout.
     *
     * Before starting the timer, this method verifies that the game's level is not zero.
     * If the level is zero, it indicates that the game is in a test flight phase where
     * a timer should not be utilized. In such cases, a LevelException is thrown.
     *
     * Once the level check is passed, the method starts a countdown timer for a duration
     * of one and a half minutes (90 seconds). When the countdown ends, a callback
     * method is triggered to handle the timeout scenario.
     *
     * @throws LevelException if the game level is zero, indicating that a timer
     *         is not applicable in the current game phase
     */
    public void startTimer() {
        if (Server.getInstance().getGame(gameId).getLevel() == 0) {
            throw new LevelException("Non esiste la clessidra nel volo di prova!");
        }

        //la clessidra dura un minuto e mezzo
        synchronized (isTimerRunning) {
            if (!isTimerRunning ) {
                isTimerRunning = true;
                timer.startCountdown(90, this::handleTimeout);
                BroadcastMessage bm = new BroadcastMessage(new StringResponse("La clessidra è stata girata hai 90 secondi !"));
                Server.getInstance().notifyAllObservers(bm, gameId);
            }
        }
    }


    /**
     * Handles the timeout event in the game, transitioning game phases or taking corrective actions
     * as needed. This method is invoked when the game timer expires.
     *
     * If the first building phase has not been completed, this method will:
     * 1. Mark the first building phase as ended.
     * 2. Notify all game observers of the timeout using a broadcast message containing a
     *    {@code TimeExpired} event.
     *
     * If the first building phase has already concluded:
     * 1. It attempts to initiate the game board check phase by calling the {@code startCheckBoard} method.
     * 2. If an {@code IllegalStateException} is encountered while starting the board check, the timeout
     *    handling logic is recursively re-invoked to ensure proper continuity.
     *
     * This method operates based on the current game state and ensures that the game progresses in a
     * defined and timely manner.
     */
    public void handleTimeout() {
        if (!isFirstBuildingPhaseEnded) {
            isTimerRunning = false;
            isFirstBuildingPhaseEnded = true;
            BroadcastMessage bm = new BroadcastMessage(new TimeExpired());
            Server.getInstance().notifyAllObservers(bm, gameId);
        } else {
            try {
                startCheckBoard();
            }catch (IllegalStateException e) {
                handleTimeout();
            }
        }
    }


    /**
     * Initiates the check board phase of the game, analyzing the state of each player's truck
     * and transitioning the game to the next phase if all checks pass.
     *
     * This method performs the following actions:
     * 1. Checks if the current game state is in the "Building" phase. If so:
     *    - Stops the ongoing timer.
     *    - Validates the reserved tiles for the game.
     *    - Updates the game status to "CheckBoards".
     *    - Notifies all observers about the state change.
     * 2. Iterates through all players in the game and verifies each player's truck:
     *    - If any player’s truck fails the validation, an {@code IllegalTruck} message
     *      is sent directly to the player, interrupting the execution to prevent further progression.
     * 3. If all trucks pass validation, it updates the allowed aliens for each player's truck.
     * 4. Calls the {@code startSetCrew} method to transition to the next phase of the game.
     *
     * @throws IllegalTruckException if an error occurs during the truck validation phase,
     *         preventing the game from continuing.
     */
    public void startCheckBoard() throws IllegalTruckException {

        if (Server.getInstance().getGame(gameId).getGameStatus() == GameStatus.Building) {
            timer.shutdown();
            Game game = Server.getInstance().getGame(gameId);
            game.checkReservedTiles();
            game.setGameStatus(GameStatus.CheckBoards);
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new StateChanged(GameStatus.CheckBoards)), gameId);
        }

        for (Player player : Server.getInstance().getGame(gameId).getPlayers()) {
            if (!player.getTruck().check()) {
                Server.getInstance().sendMessage(player.getNickname(), new DirectMessage((new IllegalTruck())));
                return;
            }
        }

        for (Player player : Server.getInstance().getGame(gameId).getPlayers()) {
            player.getTruck().updateAllowedAliens();
        }

        startSetCrew();
    }

    /**
     * Transitions the game state to the "SetCrew" phase if the current game state is "CheckBoards".
     *
     * This method checks the status of the game identified by the associated game ID. If the game
     * is in the "CheckBoards" phase, it updates the game status to "SetCrew", notifies all observers
     * about the state change, and broadcasts a {@code StateChanged} event with the new status.
     *
     * If the game is not in the "CheckBoards" state, the method throws a {@code RuntimeException}
     * to indicate that the current state is not valid for setting the crew.
     *
     * Throws:
     * - {@code RuntimeException}: if the game is not in the "CheckBoards" phase.
     */
    public void startSetCrew() {
        if (Server.getInstance().getGame(gameId).getGameStatus() == GameStatus.CheckBoards) {
            Server.getInstance().getGame(gameId).setGameStatus(GameStatus.SetCrew);
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new StateChanged(GameStatus.SetCrew)), gameId);
        }else{
            throw new RuntimeException("Not in the correct state to set crew");
        }
    }

    /**
     * Handles the action of positioning a crew member for a specific player in the game.
     * This method ensures that the crew member is added only if they have not already been positioned.
     * If all players have positioned their crew members, it clears the crew positions and starts the flight phase.
     *
     * @param username the unique username of the player positioning their crew member
     * @throws InvalidActionException if the player has already declared their crew
     */
    public void crewPositioned(String username) {
        if(!crewPositioned.contains(username)) {
            crewPositioned.add(Server.getInstance().getGame(gameId).getPlayerFromNickname(username));
        }else{
            throw new InvalidActionException("You have already declared crew ");
        }
        if(crewPositioned.size() == Server.getInstance().getGame(gameId).getPlayers().size()){
            crewPositioned.clear();
            startFlight();
        }
    }


    /**
     * Handles the action when a player finishes building his ship during the building phase.
     * Updates the player's position, sorts players by their position, and notifies the concerned player of their placement.
     * If all players have finished building their ships, initiates the check board phase.
     *
     * @param username the unique username of the player who has finished building their structure
     */
    public void playerFinishedBuilding(String username) {
        Game game = Server.getInstance().getGame(gameId);
        game.getPlayerFromNickname(username).setPosition(game.getFirstPositions()[currentPosition]);
        currentPosition++;
        Server.getInstance().getGame(gameId).sortPlayersByPosition();
        int playerPlacement = Server.getInstance().getGame(gameId).getPlayers().stream().map(player -> player.getNickname()).toList().indexOf(username) + 1;
        Server.getInstance().sendMessage(username, new DirectMessage(new StringResponse("Pedina posizionata! Sei in posizione " + playerPlacement +"\n")));
        if(currentPosition == game.getPlayers().size()) {
            startCheckBoard();
        }
    }

    /**
     * Initiates the flight phase of the game.
     *
     * This method transitions the game to the "Playing" status and notifies all game observers
     * of the state change through a broadcast message containing a {@code StateChanged} event.
     * Following this, the game status is updated to "Waiting for New Card", and a notification
     * is sent to all observers instructing the leader to draw the first card.
     *
     * The method uses the game instance identified by the associated game ID for these operations
     * and relies on the server's notification mechanisms to communicate updates to all players.
     */
    public void startFlight(){
        Game game = Server.getInstance().getGame(gameId);
        game.setGameStatus(GameStatus.Playing);
        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StateChanged(GameStatus.Playing)), gameId);
        game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la prima carta\n")), gameId);
    }


    /**
     * Handles a game event by updating the game's status and notifying observers.
     * This method interacts with the game associated with the given game ID, updating its
     * status based on the new status provided by the event. It also sends notifications
     * to all observers about the state change and provides a detailed event description.
     *
     * @param event the game event containing the new status and its associated details
     *              used to update the game and notify observers
     */
    public void onGameEvent(Event event) {
        Game game = Server.getInstance().getGame(gameId);
        game.setGameStatus(event.getNewStatus());

        Message message = new BroadcastMessage(new UpdateFromCard(event.describe(gameId)));
        Server.getInstance().notifyAllObservers(message, gameId);

    }

    /**
     * Handles a game event directed towards a single client by updating the game's status
     * and sending a notification with event details only to the specified player.
     *
     * @param event the game event containing the new status and its associated details
     *              used to update the game and notify the single client
     * @param playerUsername the unique username of the player who should receive the
     *                        notification of the event
     */
    public void onGameEventString(Event event, String playerUsername) {
        Server.getInstance().getGame(gameId).setGameStatus(event.getNewStatus());

        Message message = new DirectMessage(new UpdateFromCard(event.describe(gameId)));
        Server.getInstance().sendMessage(playerUsername, message);

    }
}
