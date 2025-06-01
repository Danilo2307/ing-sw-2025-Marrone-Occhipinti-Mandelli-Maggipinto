package it.polimi.ingsw.psp23.view.gui.guicontrollers;
import it.polimi.ingsw.psp23.exceptions.PlayerExistsException;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.LevelSelectionMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.ClientSocket;
import it.polimi.ingsw.psp23.protocol.request.RegisterNumPlayers;
import it.polimi.ingsw.psp23.protocol.response.AppropriateUsername;
import it.polimi.ingsw.psp23.protocol.response.RequestNumPlayers;
import it.polimi.ingsw.psp23.protocol.response.WrongUsername;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.rmi.Remote;
import java.rmi.RemoteException;

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
    public void onDoneClicked(javafx.event.ActionEvent actionEvent) throws RemoteException{
        String username = usernameField.getText();
        if(client.isRmi()){
            try {
                client.getGameServer().setPlayerUsername(username);
                client.getGameServer().sendToUser(client.getNameConnection(), new DirectMessage(new AppropriateUsername(username, client.getGameServer().getGameLevel())));
                if(client.getGameServer().getNumPlayersConnected() == 1){
                    client.getGameServer().sendToUser(client.getNameConnection(), new DirectMessage(new RequestNumPlayers()));
                }
            }
            catch(PlayerExistsException e){
                client.getGameServer().sendToUser(client.getNameConnection(), new DirectMessage(new WrongUsername()));
            }
        }
        client.setUsername(username);
        try{
            if(client.isRmi()) {
                if (client.getGameServer().getNumPlayersConnected() != 1 && client.getGameServer().getNumPlayersConnected() == client.getGameServer().getNumRequestedPlayers()) {
                    client.getGameServer().startBuildingPhase();
                }
            }
        }catch (RemoteException e) {
            e.printStackTrace();
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

