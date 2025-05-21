package it.polimi.ingsw.psp23.view.gui;

import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.GetEventVisitor;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.rmi.ClientRMI;
import it.polimi.ingsw.psp23.network.socket.ClientSocket;
import it.polimi.ingsw.psp23.protocol.response.HandleEventVisitor;
import it.polimi.ingsw.psp23.view.ViewAPI;
import it.polimi.ingsw.psp23.view.gui.guicontrollers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.util.ArrayList;

// carica la prima scena e inizializza tutti gli oggetti di servizio come ad esempio i controller.
// la prima scena viene caricata con l'aiuto di FxmlViewLOader, inoltre questa classe contiene il main
// da cui viene effettivamente fatta partire la gui
public class GuiApplication extends Application implements ViewAPI {

    private ClientSocket client;
    private final BuildingPhaseController buildingPhaseController;
    private final CardDialogController cardDialogController;
    private final CheckBoardController checkBoardController;
    private final FlightPhaseController flightPhaseController;
    private final LobbyController lobbyController;
    private final TimerController timerController;
    private Stage stage;
    private StageManager stageManager;
    private static GuiApplication instance;

    public static GuiApplication getInstance() {
        return instance;
    }

    public GuiApplication() {
        this.buildingPhaseController = new BuildingPhaseController();
        this.cardDialogController = new CardDialogController();
        this.checkBoardController = new CheckBoardController();
        this.flightPhaseController = new FlightPhaseController();
        this.lobbyController = new LobbyController();
        this.timerController = new TimerController();
        instance = this;
    }


    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/lobby-view.fxml")
        );
        Parent root = loader.load();

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Galaxy Trucker");
        stage.setScene(scene);
        stage.show();
        this.stage = stage;
        stageManager = new StageManager(stage);
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void setClient(ClientSocket client) {
        this.client = client;
        this.buildingPhaseController.setClient(client);
        this.cardDialogController.setClient(client);
        this.checkBoardController.setClient(client);
        this.flightPhaseController.setClient(client);
        this.lobbyController.setClient(client);
        this.timerController.setClient(client);
    }

    @Override
    public void setClient(ClientRMI clientRMI){

    }

    @Override
    public void setup() {
        Socket socket = client.getSocket();
        try {
            socket.setSoTimeout(1000);
            Message messaggio = client.readMessage();
            messaggio.call(new GetEventVisitor()).call(new HandleEventVisitor(), this);
            client.avvia();
            socket.setSoTimeout(0);
        } catch (SocketTimeoutException ste) {
            try {
                socket.setSoTimeout(0);
                client.avvia();
                lobbyController.showUserChoice();
            }
            catch (SocketException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showRequestLevel() {
        lobbyController.showLevelChoice();
    }

    @Override
    public void showRequestNumPlayers() {
        lobbyController.showNumPlayers();
    }

    @Override
    public void showAppropriateUsername(String username) {
        lobbyController.hideUserChoice();
    }

    @Override
    public void showWrongUsername() {
        lobbyController.flushText();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Username non valido");
        alert.setHeaderText(null);
        alert.setContentText("Inseriscine un altro");
        alert.showAndWait();
    }

    @Override
    public void showTile(Component requested) {
        buildingPhaseController.showTile(requested);
    }

    @Override
    public void showShip(Component[][] ship, int[][] validCoordinates) {

    }

    @Override
    public void showUncovered(ArrayList<Component> uncovered, int lastVersion) {
        buildingPhaseController.showUncovered(uncovered, lastVersion);
    }

    @Override
    public void showError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(error);
        // pop-up che blocca esecuzione finchè l'utente non chiude la finestra
        alert.showAndWait();
    }

    @Override
    public void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);

    }

    @Override
    public void stateChanged(GameStatus newState) {
        switch(newState) {
            case GameStatus.Building -> stageManager.toBuildingPhase();
        }
    }

    @Override
    public void showTurn(String turn) {

    }

    @Override
    public void showStart() {

    }

    @Override
    public void showIllegalTruck() {

    }

    @Override
    public void showPlayerLanded(String username, int planet) {

    }

    @Override
    public void showTimeExpired() {

    }

    @Override
    public void showEndTurn(String username) {

    }

    @Override
    public void showEnd() {

    }

    @Override
    public void endMatch(String message) {

    }

    @Override
    public void showCardUpdate(String message) {

    }

    @Override
    public void showMeteor(Meteor meteor) {

    }

    @Override
    public void showCannonShot(int coord, CannonShot cannonShot) {

    }

    @Override
    public void setupRMI() throws RemoteException {

    }
}
