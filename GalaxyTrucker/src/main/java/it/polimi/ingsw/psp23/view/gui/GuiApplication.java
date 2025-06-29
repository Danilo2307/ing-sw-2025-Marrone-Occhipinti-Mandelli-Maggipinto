package it.polimi.ingsw.psp23.view.gui;

import it.polimi.ingsw.psp23.model.helpers.CannonShot;
import it.polimi.ingsw.psp23.model.helpers.Meteor;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.protocol.response.LobbyAvailable;
import it.polimi.ingsw.psp23.view.ViewAPI;
import it.polimi.ingsw.psp23.view.gui.guicontrollers.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.CountDownLatch;


/**
 * GuiApplication is the graphical user interface entry point and main controller for the client application.
 * It manages the application lifecycle, transitions between game phases, and interaction
 * with the client and server through the ViewAPI interface. The class enables users to interact with
 * the game via a GUI using JavaFX.
 * GuiApplication extends JavaFX's Application and implements ViewAPI to handle game server updates effectively.
 */
public class GuiApplication extends Application implements ViewAPI {
    private static final CountDownLatch latch = new CountDownLatch(1);
    private Client client;
    private  BuildingPhaseController buildingPhaseController;
    private  FlightPhaseController flightPhaseController;
    private FlightBoardController2 flightBoardController2;
    private FlightBoardController0 flightBoardController0;
    private LobbyController lobbyController;
    private DeckViewController deckViewController;
    private OpponentShipController opponentShipController;
    private EndGameController endGameController;
    private Stage stage;
    private static GuiApplication instance;
    private Color playerColor;
    private int level;
    private StackPane buildedShip = null;
    private GridPane buildedGrid = null;
    private Scene buildingPhaseScene = null;
    private Scene flightBoardScene = null;
    private Scene flightPhaseScene = null;
    private GameStatus gameStatus;
    String myNickname;
    ArrayList<String> usernames = new ArrayList<>();
    Parent rootLobby;
    boolean isHourGlassTurned = false;

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public static void awaitStart() throws InterruptedException {
        latch.await(); // aspetta finché start() non ha finito
    }

    public Client getClient() {
        return client;
    }

    public int getLevel() {
        return level;
    }

    /**
     * @return the singleton {@code GuiApplication} instance
     */
    public static GuiApplication getInstance() {
        return instance;
    }

    public GuiApplication() {
        instance = this;
    }


    /**
     * Initializes and displays the primary stage of the application. This method is responsible for loading
     * the initial FXML file, setting up the respective controller, and preparing the GUI environment for the game.
     * Once the setup is complete, it notifies the application by decrementing the latch count to allow further execution.
     *
     * @param stage the primary stage for the application, provided by the JavaFX runtime
     * @throws Exception if there is an issue loading the FXML file or initializing the stage
     */
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/lobby-view.fxml")
        );

        rootLobby = loader.load();

        this.lobbyController = loader.getController();
        this.stage = stage;
        latch.countDown();
    }

    public void showLobby(){
        Platform.runLater(() -> {
            lobbyController.setClient(client);
            stage.setResizable(false);
            Scene scene = new Scene(rootLobby,1152,768);
            stage.setTitle("Galaxy Trucker");
            stage.setScene(scene);
            stage.show();
        });
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }


    /**
     * Configures the socket connection on the client side and starts the server listening thread.
     * This method invokes the {@code avvia()} method of the {@code Client} class, ensuring that
     * the client is ready to receive and process server events.
     */
    @Override
    public void setup() {
        client.avvia();
    }


    /**
     * Sets up the RMI (Remote Method Invocation) connection for the client by retrieving the list of
     * available games from the game server and sending this information encapsulated in a
     * {@link DirectMessage} to the specified user connection.
     *
     * @param nameConnection the name of the connection to which the {@link DirectMessage} containing
     *                       the available lobbies information is sent.
     * @throws RemoteException if there is a communication-related exception during the RMI operation.
     */
    @Override
    public void setupRMI(String nameConnection) throws RemoteException{

        List<List<Integer>> matchesAvailable = client.getGameServer().getGamesAvailables();
        client.getGameServer().sendToUser(nameConnection, new DirectMessage(new LobbyAvailable(matchesAvailable)));
    }

    /**
     * Prepares and transitions the application's user interface to the building phase.
     * If the building phase scene has not been initialized, it loads the FXML file,
     * sets up the corresponding controller and scene, and displays it. If the scene
     * is already initialized, it simply sets the existing scene as active.
     * Additionally, it configures the scene based on the player's color by updating
     * the central cabin.
     *
     * @param playerColor the {@link Color} representing the player's assigned color,
     *                    used to customize and configure the building phase layout.
     */
    public void toBuildingPhase(Color playerColor) {
        if(buildingPhaseScene == null) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/build-view.fxml")
            );
            try {
                Parent root = loader.load();
                this.buildingPhaseController = loader.getController();
                buildingPhaseController.setClient(client);
                buildingPhaseController.setupViewOtherShipsBtn();
                Scene scene = new Scene(root, 1152, 768);
                buildingPhaseScene = scene;
                buildedShip = buildingPhaseController.getShip();
                buildedGrid = buildingPhaseController.getBuildedGrid();
                Platform.runLater(() -> {stage.setScene(scene);});
                buildingPhaseController.setCentral(playerColor);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            stage.setScene(buildingPhaseScene);
        }

    }

    @Override
    public void showRequestLevel() {
        //lobbyController.showLevelChoice();
    }

    @Override
    public void showRequestNumPlayers() {
        //lobbyController.showNumPlayers();
    }

    /**
     * This method hides the user choice elements in the lobby interface and stores the
     * user's level for future use.
     *
     * @param username the username to be displayed or assigned
     * @param level the level associated with the user
     */
    @Override
    public void showAppropriateUsername(String username, int level) {
        myNickname = username;
        lobbyController.hideUserChoice();
        this.level = level;
    }

    /**
     * Displays an error message signaling that the entered username is invalid.
     * This method uses the JavaFX `Platform.runLater` functionality to ensure
     * that the user interface updates occur on the JavaFX Application Thread.
     * It clears the username input field in the lobby, then shows an alert dialog
     * of type `ERROR` to inform the user to provide a different username.
     */
    @Override
    public void showWrongUsername() {
        Platform.runLater(() -> {
            lobbyController.flushText();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Username non valido");
            alert.setHeaderText(null);
            alert.setContentText("Inseriscine un altro");
            alert.showAndWait();
        });
    }

    /**
     * Displays the tile based on the given component.
     * If the requested component represents the central cabin (IDs >= 900),
     * the player's color is updated accordingly, and the building phase setup begins.
     * Otherwise, forwards the request to the building phase controller to handle the display.
     *
     * @param requested the component representing the tile to be shown,
     *                  identified by its unique ID.
     */
    @Override
    public void showTile(Component requested) {
        // se è la cabina centrale salvo il colore e la aggiungo all'inizio di buildingphase
        if (requested.getId() >= 900) {
            switch (requested.getId()) {
                case 900 ->
                        playerColor = Color.Blue;
                case 901 ->
                        playerColor = Color.Green;
                case 902 ->
                        playerColor = Color.Red;
                case 903 ->
                        playerColor = Color.Yellow;
            }
        }
        else {
            buildingPhaseController.showTile(requested);
        }
    }

    /**
     * Displays the ship configuration for a specific user during the game. Depending on whether
     * the owner of the ship matches the user's nickname, the method initializes and renders
     * appropriate scenes and controllers to show the ship details.
     *
     * @param ship a 2-dimensional array of {@code Component} objects representing the ship's current configuration
     * @param owner the nickname of the ship's owner whose ship configuration is being displayed
     */
    @Override
    public void showShip(Component[][] ship, String owner) {
        if (!owner.equals(myNickname)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/opponent-ship-view.fxml"));
            try {
                Parent root = loader.load();
                this.opponentShipController = loader.getController();
                Scene scene = new Scene(root, 1152, 768);

                Platform.runLater(() -> {
                    opponentShipController.setLabel(owner);
                    opponentShipController.renderShip(ship, level);
                    stage.setScene(scene);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            if (flightPhaseScene != null) {
                Platform.runLater(() -> {
                    flightPhaseController.updateShip(ship);
                });
            }

        }

    }

    /**
     * This method forwards the uncovered components and version information to the
     * building phase controller for handling and display.
     *
     * @param uncovered the list of {@code Component} objects that have been uncovered
     *                  and need to be displayed
     * @param lastVersion an integer representing the latest version of the uncovered components
     */
    @Override
    public void showUncovered(ArrayList<Component> uncovered, int lastVersion) {
        buildingPhaseController.showUncovered(uncovered, lastVersion);
    }

    /**
     * Displays an error message in a blocking alert dialog.
     * The error message is shown on the JavaFX Application Thread to ensure proper UI rendering.
     *
     * @param error the error message to be displayed in the dialog
     */
    @Override
    public void showError(String error) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(error);
            // pop-up che blocca esecuzione finchè l'utente non chiude la finestra
            alert.showAndWait();
        });
    }

    /**
     * Displays an informational message in a blocking alert dialog using the JavaFX platform.
     * The method ensures that the alert dialog is created and displayed on the JavaFX Application Thread.
     *
     * @param message the informational message to be displayed in the dialog
     */
    @Override
    public void showMessage(String message) {
        Platform.runLater(()  -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(message);
            // pop-up che blocca esecuzione finchè l'utente non chiude la finestra
            alert.showAndWait();
        });
    }

    /**
     * Handles the transitions between game states by updating the current {@code gameStatus}
     * and invoking the corresponding UI updates for each state.
     *
     * @param newState the {@link GameStatus} representing the new state of the game
     */
    @Override
    public void stateChanged(GameStatus newState) {
        gameStatus = newState;
        Platform.runLater(() -> {
            switch(newState) {
                case GameStatus.Building -> toBuildingPhase(playerColor);
                case GameStatus.CheckBoards -> buildingPhaseController.hideBuildCommands();
                case GameStatus.SetCrew -> buildingPhaseController.toAddCrew();
                case GameStatus.Playing -> toFlightPhase();
            }
        });
    }

    /**
     * Displays a message indicating that a cannon shot is incoming, along with details
     * about the size and direction of the shot, and its associated coordinate.
     *
     * @param coord the coordinate related to the cannon shot
     * @param cannonShot the {@link CannonShot} object representing the fired shot,
     *                   including its size and direction
     */
    @Override
    public void showCannonShot(int coord, CannonShot cannonShot) {
        showMessage("Sta arrivando una cannonata " + cannonShot.isBig() + " dalla direzione " + cannonShot.getDirection() + coord);
    }


    /**
     * This method delegates the processing to the building phase controller, ensuring that the illegal truck
     * logic or behavior is appropriately handled within the current game phase.
     */
    @Override
    public void showIllegalTruck() {
        buildingPhaseController.toCheck();
    }


    @Override
    public void showTimeExpired() {
        if(!isHourGlassTurned)
            isHourGlassTurned = true;
        else {
            Platform.runLater(() -> {
                buildingPhaseController.hideBuildCommands();
                buildingPhaseController.timerEnded();
            });
        }
        Platform.runLater(() -> {
            showMessage("Tempo scaduto");
        });
    }



    /**
     * Ends the current match and displays a message to the user.
     * This method delegates the task of showing the message to the {@code showMessage} method.
     *
     * @param message the message to be displayed when the player quits from the flight phase
     */
    @Override
    public void endMatch(String message) {
        showMessage(message);
    }

    /**
     * Stop the current match and show the game over scene on the view.
     *
     * @param message the message to be displayed to indicate the end of the match
     */
    @Override
    public void stopMatch(String message) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameover.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 1152, 768);
        Platform.runLater(() -> {stage.setScene(scene);});
    }

    /**
     * Updates the game interface with the provided message during the flight phase.
     * The method ensures that the user interface is updated on the JavaFX Application Thread.
     *
     * @param message the message to be displayed on the card in the flight phase
     */
    @Override
    public void showCardUpdate(String message) {
        Platform.runLater(() -> {
            flightPhaseController.getTextLabel().setText(message);
        });
    }


    /**
     * Displays the deck screen in the application. This method initializes the deck view,
     * loads the corresponding FXML file, and sets up the images for the cards specified by their IDs.
     * The method ensures that the user interface updates occur on the JavaFX Application Thread.
     *
     * @param idCards the list of integers representing the IDs of the cards to be displayed
     * @param description the description to be displayed along with the cards
     */
    @Override
    public void showDeck(ArrayList<Integer> idCards, String description) {
        ImageView card1;
        ImageView card2;
        ImageView card3;
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/show-deck.fxml")
        );
        try {
            Parent root = loader.load();
            this.deckViewController = loader.getController();
            Platform.runLater(() -> {
                Scene scene = new Scene(root, 1152, 768);
                stage.setScene(scene);
            });
            String imagePath1 = "/it/polimi/ingsw/psp23/images/cards/" + idCards.get(0) + ".jpg";
            String imagePath2 = "/it/polimi/ingsw/psp23/images/cards/" + idCards.get(1) + ".jpg";
            String imagePath3 = "/it/polimi/ingsw/psp23/images/cards/" + idCards.get(2) + ".jpg";
            card1 = deckViewController.getCard1();
            card2 = deckViewController.getCard2();
            card3 = deckViewController.getCard3();
            Platform.runLater(() -> {
                card1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath1))));
                card2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath2))));
                card3.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath3))));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays a new card and triggers the corresponding actions in the flight-phase controller based on the card's ID.
     *
     * @param id the unique identifier of the card to be displayed
     * @param description a description of the card being displayed
     */
    @Override
    public void showNewCard(int id, String description) {
        Platform.runLater(() -> {
            flightPhaseController.getTextLabel().setText("");
            flightPhaseController.disableAllButtons();
            flightPhaseController.setCardImage(id);

            switch (id) {
                case 105, 212  -> {
                    flightPhaseController.startdustCommands();
                }
                case 101, 109, 110, 111, 201, 202, 203-> {
                    flightPhaseController.openSpaceCommands();
                }
                case 103, 115, 208, 209 -> {
                    flightPhaseController.abandonedshipCommands();
                }
                case 117, 213  -> {
                    flightPhaseController.slaversCommands();
                }
                case 107, 119, 120, 216, 217, 218 -> {
                    flightPhaseController.meteorSwarmCommands();
                }
                case 219 -> {
                    flightPhaseController.epidemicCommands();
                }
                case 118, 215 -> {
                    flightPhaseController.piratesCommands();
                }
                case 102, 112, 113, 114, 204, 205, 206, 207 -> {
                    flightPhaseController.planetsCommands(id);
                }
                case 104, 116, 210, 211 -> {
                    flightPhaseController.abandonedStationCommands();
                }
                case 106, 214 -> {
                    flightPhaseController.smugglersCommands();
                }
                case 108, 220 -> {
                    flightPhaseController.combatZoneCommands();
                }
            }
        });
    }

    /**
     * Makes adjustments to the game interface when a tile placement is deemed incorrect.
     * This method is executed on the JavaFX Application Thread using the `Platform.runLater` mechanism.
     * It ensures that the tile currently held is made visible and that the cell
     * designated for tile removal is cleared of its children nodes.
     */
    @Override
    public void incorrectTile(){
        Platform.runLater(() -> {
            buildingPhaseController.getTileInHand().setVisible(true);
            buildingPhaseController.getCellToRemove().getChildren().clear();
        });

    }

    /**
     * Displays the flight board interface by setting up the appropriate scene
     * depending on the current game level. It updates the board with the provided
     * player positions and ensures the correct UI is loaded.
     *
     * @param positions a map containing the player positions, where the key represents
     *                  the player color and the value represents their respective position.
     */
    @Override
    public void showFlightBoard(Map<Color,Integer> positions){
        FXMLLoader loader;
        if(flightBoardScene == null) {
            if (this.level == 0) {
                loader = new FXMLLoader(
                        getClass().getResource("/fxml/flight-board-view-0.fxml")
                );
                try {
                    Parent root = loader.load();
                    this.flightBoardController0 = loader.getController();
                    flightBoardController0.setClient(client);
                    flightBoardController0.setColors(positions);
                    Scene scene = new Scene(root, 1152, 768);
                    flightBoardScene = scene;
                    Platform.runLater(() -> {
                        stage.setScene(scene);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                loader = new FXMLLoader(
                        getClass().getResource("/fxml/flight-board-view-2.fxml")
                );
                try {
                    Parent root = loader.load();
                    this.flightBoardController2 = loader.getController();
                    flightBoardController2.setClient(client);
                    flightBoardController2.setColors(positions);
                    Scene scene = new Scene(root, 1152, 768);
                    flightBoardScene = scene;
                    Platform.runLater(() -> {
                        stage.setScene(scene);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            if(level == 2) {
                flightBoardController2.setColors(positions);
                Platform.runLater(() -> {
                    stage.setScene(flightBoardScene);
                });
            }else{
                flightBoardController0.setColors(positions);
                Platform.runLater(() -> {
                    stage.setScene(flightBoardScene);
                });
            }
        }
    }

    public int getDeckNumber(){
        return flightBoardController2.getDeckNumber();
    }

    public void disableDeckClick(){
        flightBoardController2.disableDeckClick();
    }

    /**
     * Transitions the application to the "flight phase" view by loading the associated FXML file
     * and initializing its controller. If the scene has already been created, it switches directly to it.
     */
    public void toFlightPhase(){
        if(flightPhaseController == null) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/flight-view.fxml")
            );
            try {
                Parent root = loader.load();
                this.flightPhaseController = loader.getController();
                flightPhaseController.setClient(client);
                Scene scene = new Scene(root, 1152, 768);
                flightPhaseController.enable(flightPhaseController.getDrawBtn());
                flightPhaseController.setShip(buildedShip, buildedGrid);
                flightPhaseController.installClickHandlers();
                flightPhaseController.setupViewOtherShipsBtn();
                flightPhaseController.getTextLabel().setText("Aspettando che il Leader peschi la prima carta  ...");
                flightPhaseScene = scene;
                Platform.runLater(() -> {
                    stage.setScene(scene);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Platform.runLater(() -> {
                stage.setScene(flightPhaseScene);
            });
        }
    }

    /**
     * Retrieves a list of usernames excluding the current user's nickname.
     *
     * @return an ArrayList of usernames in the game excluding the current user's nickname
     */
    public ArrayList<String> getOtherUsers() {
        ArrayList<String> copy = new ArrayList<>(usernames); // copia difensiva
        copy.remove(myNickname); // rimuove il proprio nickname
        return copy;
    }

    /**
     * Transitions the game state back to the ship phase based on the current game status.
     * If the game status is in the building phase, it transitions to the building phase.
     * Otherwise, it transitions to the flight phase.
     * This method ensures the UI transitions are correctly handled based
     * on the current context.
     */
    public void backToShip(){
        if(gameStatus == GameStatus.Building)
            toBuildingPhase(null);
        else
            toFlightPhase();
    }

    /**
     * Displays the list of available lobbies by forwarding the provided information
     * to the lobby controller.
     *
     * @param availableLobbies a list containing details of available lobbies, where
     *                         each inner list represents specific information regarding a lobby
     */
    @Override
    public void showAvailableLobbies(List<List<Integer>> availableLobbies){
        lobbyController.showLobbies(availableLobbies);
    }

    /**
     * Displays the ranking screen at the end of the game.
     *
     * @param ranking a list of player rankings where each entry represents a player,
     *                with the key being the player's name (String) and the value
     *                being their score (Integer)
     */
    @Override
    public void showRanking(List<AbstractMap.SimpleEntry<String,Integer>> ranking) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/end_game.fxml")
        );
        try {
            Parent root = loader.load();
            this.endGameController = loader.getController();
            endGameController.printInfo(ranking, stage);
            Scene scene = new Scene(root, 1152, 768);
            Platform.runLater(() -> {
                stage.setScene(scene);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the list of player names into the usernames list.
     *
     * @param players the list of player names to be saved
     */
    @Override
    public void savePlayersNames(List<String> players) {
        usernames = new ArrayList<>(players);
    }

    public String getMyNickname() {
        return myNickname;
    }

}