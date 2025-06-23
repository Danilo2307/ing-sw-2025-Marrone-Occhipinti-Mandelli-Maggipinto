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

    public void showLobbies(List<List<Integer>> availableLobbies) {
        Platform.runLater(() -> {
            // Crea un bottone per ogni lobby
            for (int i = 0; i < availableLobbies.size(); i++) {
                List<Integer> lobby = availableLobbies.get(i);
                int id = lobby.get(0);
                int current = lobby.get(1);
                int max = lobby.get(2);
                int level = lobby.get(3);

                String label = String.format("Partita %d:::  livello: %d ||| giocatori presenti: %d ||| numero massimo: %d", id+1, level, current, max);
                Button joinButton = new Button(label);
                joinButton.setFont(Font.font("Arial Black"));
                joinButton.setPrefHeight(40);
                joinButton.setPrefWidth(1000);
                joinButton.setOnAction(ev -> {
                    // la lambda cattura il rispettivo lobbyId al momento della creazione
                    try {
                        if (!client.isRmi()) {
                            try {
                                client.sendAction(new UserDecision(id+1));   // server fa la conversione a 0-based
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {
                            RMIIdGameChosen = id+1;
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
        });
    }

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

    public void hideUserChoice() {
        usernameField.setVisible(false);
        usernameField.setManaged(false);
        done.setVisible(false);
        done.setManaged(false);
        usernameLabel.setVisible(false);
        usernameLabel.setManaged(false);
    }

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

    @FXML
    public void onDoneClicked() throws RemoteException{
        String username = usernameField.getText();
        if(client.isRmi()){
            try {
                // TODO: client.getGameServer().setPlayerUsername(username);
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

    public void showNumPlayers() {
        selectPlayersLabel.setVisible(true);
        twoPlayersButton.setVisible(true);
        threePlayersButton.setVisible(true);
        fourPlayersButton.setVisible(true);
        selectPlayersLabel.setManaged(true);
        twoPlayersButton.setManaged(true);
        threePlayersButton.setManaged(true);
        fourPlayersButton.setManaged(true);
    }

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

    @FXML
    public void onSelectTwoPlayers(javafx.event.ActionEvent actionEvent) throws RemoteException {
        client.sendAction(new RegisterNumPlayers(2));
        hideNumPlayers();
    }

    @FXML
    public void onSelectThreePlayers(javafx.event.ActionEvent actionEvent) throws RemoteException {
        client.sendAction(new RegisterNumPlayers(3));
        hideNumPlayers();
    }

    @FXML
    public void onSelectFourPlayers(javafx.event.ActionEvent actionEvent) throws RemoteException {
        client.sendAction(new RegisterNumPlayers(4));
        hideNumPlayers();
    }

    public void flushText() {
        usernameField.setText("");
    }







}

