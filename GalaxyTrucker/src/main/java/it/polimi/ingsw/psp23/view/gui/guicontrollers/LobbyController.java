package it.polimi.ingsw.psp23.view.gui.guicontrollers;
import it.polimi.ingsw.psp23.exceptions.PlayerExistsException;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.LevelSelectionMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.ClientSocket;
import it.polimi.ingsw.psp23.protocol.request.RegisterNumPlayers;
import it.polimi.ingsw.psp23.protocol.request.UserDecision;
import it.polimi.ingsw.psp23.protocol.response.AppropriateUsername;
import it.polimi.ingsw.psp23.protocol.response.RequestNumPlayers;
import it.polimi.ingsw.psp23.protocol.response.WrongUsername;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


/**
 * The LobbyController class manages the user interface and interaction logic for the game's lobby.
 * It facilitates game creation, lobby selection, username input, level selection, and number of players
 * selection, providing communication with the client-side game logic.
 */
public class LobbyController {
    private Client client;

    // scelta partita
    @FXML private Button createGameBtn;
    @FXML private Label lobbyLabel;
    @FXML private VBox lobbiesContainer;
    private boolean creator = false;
    private int RMIIdGameChosen;

    // username
    @FXML private Button done;
    @FXML private TextField usernameField;
    @FXML private Label usernameLabel;

    // selezione livello
    @FXML private Label selectLevel;
    @FXML private Button trialLevel;
    @FXML private Button twoLevel;

    // selezione numero giocatori
    @FXML private Label selectPlayersLabel;
    @FXML private Button twoPlayersButton;
    @FXML private Button threePlayersButton;
    @FXML private Button fourPlayersButton;


    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Displays the available lobbies to the user and provides options to join one of them.
     * Updates the UI dynamically by creating buttons for each available lobby with
     * relevant information about the lobby.
     *
     * @param availableLobbies a list of lobbies where each lobby is represented
     *                         as a list of integers. The list contains the following elements:
     *                         - index 0: the ID of the lobby
     *                         - index 1: the current number of players in the lobby
     *                         - index 2: the maximum number of players allowed in the lobby
     *                         - index 3: the level of the lobby
     */
    public void showLobbies(List<List<Integer>> availableLobbies) {
        Platform.runLater(() -> {
            if (!availableLobbies.isEmpty()) {
                lobbyLabel.setText("Lobby disponibili");
                // Crea un bottone per ogni lobby
                for (int i = 0; i < availableLobbies.size(); i++) {
                    List<Integer> lobby = availableLobbies.get(i);
                    int id = lobby.get(0);
                    int current = lobby.get(1);
                    int max = lobby.get(2);
                    int level = lobby.get(3);

                    String label = String.format("Partita %d:::  livello: %d ||| giocatori presenti: %d ||| numero massimo: %d", id + 1, level, current, max);
                    Button joinButton = new Button(label);
                    joinButton.setFont(Font.font("Arial Black"));
                    joinButton.setPrefHeight(40);
                    joinButton.setPrefWidth(1000);
                    joinButton.setOnAction(ev -> {
                        // la lambda cattura il rispettivo lobbyId al momento della creazione
                        try {
                            if (!client.isRmi()) {
                                try {
                                    client.sendAction(new UserDecision(id + 1));   // server fa la conversione a 0-based
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                RMIIdGameChosen = id + 1;
                            }
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        hideLobbiesView();
                        showUserChoice();  // il server avvia login+join
                        client.setId(id);  // setto 0-based come nel server
                    });

                    lobbiesContainer.getChildren().add(joinButton);
                }
            }
        });
    }

    /**
     * Hides the UI elements related to the lobbies view. This includes
     * making specific buttons and containers invisible and unmanaged within
     * the layout. Clears any existing lobbies from the container.
     *
     * This method ensures that the view updates are performed on the JavaFX
     * Application Thread using the {@code Platform.runLater} mechanism.
     */
    private void hideLobbiesView() {
        Platform.runLater(() -> {
            createGameBtn.setVisible(false);
            createGameBtn.setManaged(false);
            lobbiesContainer.getChildren().clear();
            lobbiesContainer.setVisible(false);
            lobbiesContainer.setManaged(false);
            lobbyLabel.setVisible(false);
        });
    }

    /**
     * Handles the creation of a new game. This method is triggered when the user initiates
     * a request to create a game from the lobby view. Depending on the client's connection type (RMI or not),
     * it sends the appropriate actions or updates RMI-specific data.
     *
     * @throws RemoteException if there is a failure in remote communication.
     */
    @FXML
    private void createGame() throws RemoteException {
        if (!client.isRmi()) {
            client.sendAction(new UserDecision(0));
        }
        else {
            RMIIdGameChosen = 0;
        }

        creator = true;
        hideLobbiesView();
        showLevelChoice();  // reindirizza al flow di creazione
    }

    /**
     * Displays the UI elements for selecting a level.
     */
    public void showLevelChoice() {
        selectLevel.setVisible(true);
        trialLevel.setVisible(true);
        twoLevel.setVisible(true);
        selectLevel.setManaged(true);
        trialLevel.setManaged(true);
        twoLevel.setManaged(true);
    }

    /**
     * Hides the level selection UI elements.
     */
    private void hideLevelChoice() {
        selectLevel.setVisible(false);
        trialLevel.setVisible(false);
        twoLevel.setVisible(false);
        selectLevel.setManaged(false);
        trialLevel.setManaged(false);
        twoLevel.setManaged(false);
    }

    /**
     * Displays the choice interface for the user to input their username.
     */
    public void showUserChoice() {
        usernameField.setVisible(true);
        done.setVisible(true);
        usernameField.setManaged(true);
        done.setManaged(true);
        usernameLabel.setVisible(true);
        usernameLabel.setManaged(true);
    }

    /**
     * Hides the UI elements associated with the user choice interface.
     */
    public void hideUserChoice() {
        usernameField.setVisible(false);
        usernameField.setManaged(false);
        done.setVisible(false);
        done.setManaged(false);
        usernameLabel.setVisible(false);
        usernameLabel.setManaged(false);
    }

    /**
     * Handles the action triggered when the trial level option is selected.
     * This method determines the type of connection used by the client (RMI or not)
     * and configures the game level accordingly.
     */
    @FXML
    public void onTrialLevel() {
        try{
            if(!client.isRmi()) {
                Message message = new LevelSelectionMessage(0);
                client.sendMessage(message);
            }
            else{
                client.getGameServer().setGameLevel(0);
            }
        }catch (RemoteException e){
            e.printStackTrace();
        }
        hideLevelChoice();
        showUserChoice();
    }

    /**
     * Handles the selection of the second level in the game setup process.
     *
     * After processing the level selection, this method updates the user interface:
     * - Hides the level selection view using {@code hideLevelChoice}.
     * - Displays the user choice view using {@code showUserChoice}.
     */
    @FXML
    public void onTwoLevel() {
        try{
            if(!client.isRmi()) {
                Message message = new LevelSelectionMessage(2);
                client.sendMessage(message);
            }
            else{
                client.getGameServer().setGameLevel(2);
            }
        }catch (RemoteException e){
            e.printStackTrace();
        }

        hideLevelChoice();
        showUserChoice();
    }

    /**
     * Handles the action triggered when the "Done" button is clicked in the
     * lobby view. This method facilitates username assignment and initiates
     * game-related actions depending on the type of client connection (RMI or not).
     * If the current client is the game creator, this method also displays the
     * options for selecting the number of players.
     *
     * @throws RemoteException if an error occurs during remote communication.
     */
    @FXML
    public void onDoneClicked() throws RemoteException{
        String username = usernameField.getText();
        if(client.isRmi()){
            try {
                if (RMIIdGameChosen == 0) {
                    RMIIdGameChosen = client.getGameServer().getGamesSize();
                    client.setId(RMIIdGameChosen - 1);
                }
                client.getGameServer().setPlayerUsername(username, RMIIdGameChosen-1);
                client.getGameServer().sendToUser(client.getNameConnection(), new DirectMessage(new AppropriateUsername(username, client.getGameServer().getGameLevel(client.getId()))));

            }
            catch(PlayerExistsException e){
                client.getGameServer().sendToUser(client.getNameConnection(), new DirectMessage(new WrongUsername()));
            }
        }
        // questo comando invia anche azione
        client.setUsername(username);
        try{
            if(client.isRmi()) {
                if (client.getGameServer().getNumPlayersConnected(client.getId()) != 1 && client.getGameServer().getNumPlayersConnected(client.getId()) == client.getGameServer().getNumRequestedPlayers(client.getId())) {
                    client.getGameServer().startBuildingPhase(client.getId());
                }
            }
        }catch (RemoteException e) {
            e.printStackTrace();
        }

        if (creator) {
            showNumPlayers();
        }
    }

    /**
     * Displays the UI elements for selecting the number of players.
     */
    private void showNumPlayers() {
        selectPlayersLabel.setVisible(true);
        twoPlayersButton.setVisible(true);
        threePlayersButton.setVisible(true);
        fourPlayersButton.setVisible(true);
        selectPlayersLabel.setManaged(true);
        twoPlayersButton.setManaged(true);
        threePlayersButton.setManaged(true);
        fourPlayersButton.setManaged(true);
    }

    /**
     * Hides the user interface elements related to selecting the number of players.
     * This includes labels and buttons for two, three, and four players.
     */
    private void hideNumPlayers() {
        selectPlayersLabel.setVisible(false);
        twoPlayersButton.setVisible(false);
        threePlayersButton.setVisible(false);
        fourPlayersButton.setVisible(false);
        selectPlayersLabel.setManaged(false);
        twoPlayersButton.setManaged(false);
        threePlayersButton.setManaged(false);
        fourPlayersButton.setManaged(false);
    }

    /**
     * Handles the selection of two players during the game setup process.
     * This method sends an action to the server registering the number of players as two.
     * It also updates the user interface by hiding the elements related to the
     * player number selection.
     *
     * @param actionEvent the event triggered when the user selects two players
     * @throws RemoteException if an error occurs during remote communication
     */
    @FXML
    public void onSelectTwoPlayers(javafx.event.ActionEvent actionEvent) throws RemoteException {
        client.sendAction(new RegisterNumPlayers(2));
        hideNumPlayers();
    }

    /**
     * Handles the selection of three players for the game. This method is triggered
     * when the three players option is selected in the UI. It sends the action to
     * register the number of players as three to the client and hides the player
     * selection UI afterward.
     *
     * @param actionEvent the ActionEvent that triggered this method, typically associated
     *                    with the selection of the three players option in the UI
     * @throws RemoteException if there is a failure in remote communication
     */
    @FXML
    public void onSelectThreePlayers(javafx.event.ActionEvent actionEvent) throws RemoteException {
        client.sendAction(new RegisterNumPlayers(3));
        hideNumPlayers();
    }

    /**
     * Handles the selection of four players during the game setup process.
     * This method performs the following actions:
     * - Sends an action to the client indicating that four players have been selected.
     * - Hides the UI components related to the number of players selection.
     *
     * @param actionEvent the event triggered when the four players option is selected
     * @throws RemoteException if an error occurs during remote communication
     */
    @FXML
    public void onSelectFourPlayers(javafx.event.ActionEvent actionEvent) throws RemoteException {
        client.sendAction(new RegisterNumPlayers(4));
        hideNumPlayers();
    }

    /**
     * Clears text
     */
    public void flushText() {
        usernameField.setText("");
    }



}

