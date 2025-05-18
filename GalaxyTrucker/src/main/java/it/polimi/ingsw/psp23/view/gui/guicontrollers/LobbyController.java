package it.polimi.ingsw.psp23.view.gui.guicontrollers;
import it.polimi.ingsw.psp23.network.messages.LevelSelectionMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Client;
import it.polimi.ingsw.psp23.protocol.request.RegisterNumPlayers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LobbyController {
    private Client client;

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

    public void showLevelChoice() {
        selectLevel.setVisible(true);
        trialLevel.setVisible(true);
        twoLevel.setVisible(true);
        selectLevel.setManaged(true);
        trialLevel.setManaged(true);
        twoLevel.setManaged(true);
    }

    private void hideLevelChoice() {
        selectLevel.setVisible(false);
        trialLevel.setVisible(false);
        twoLevel.setVisible(false);
        selectLevel.setManaged(false);
        trialLevel.setManaged(false);
        twoLevel.setManaged(false);
    }

    public void showUserChoice() {
        usernameField.setVisible(true);
        done.setVisible(true);
        usernameField.setManaged(true);
        done.setManaged(true);
        usernameLabel.setVisible(true);
        usernameLabel.setManaged(true);
    }

    public void onTrialLevel() {
        Message message = new LevelSelectionMessage(0);
        client.sendMessage(message);

        hideLevelChoice();
        showUserChoice();
    }

    public void onTwoLevel() {
        Message message = new LevelSelectionMessage(2);
        client.sendMessage(message);

        hideLevelChoice();
        showUserChoice();
    }

    @FXML
    public void onDoneClicked(javafx.event.ActionEvent actionEvent) {
        client.setUsername(usernameField.getText());
    }

    private void hideNumPlayers() {
        selectPlayersLabel.setVisible(false);
        twoPlayersButton.setVisible(false);
        threePlayersButton.setVisible(false);
        fourPlayersButton.setVisible(false);
    }

    @FXML
    public void onSelectTwoPlayers(javafx.event.ActionEvent actionEvent) {
        client.sendAction(new RegisterNumPlayers(2));
        hideNumPlayers();
    }

    @FXML
    public void onSelectThreePlayers(javafx.event.ActionEvent actionEvent) {
        client.sendAction(new RegisterNumPlayers(3));
        hideNumPlayers();
    }

    @FXML
    public void onSelectFourPlayers(javafx.event.ActionEvent actionEvent) {
        client.sendAction(new RegisterNumPlayers(4));
        hideNumPlayers();
    }




}

