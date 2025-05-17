package it.polimi.ingsw.psp23.view.gui;

import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.GetEventVisitor;
import it.polimi.ingsw.psp23.network.messages.LevelSelectionMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Client;
import it.polimi.ingsw.psp23.protocol.response.HandleEventVisitor;
import it.polimi.ingsw.psp23.view.ViewAPI;
import it.polimi.ingsw.psp23.view.gui.guicontrollers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;

// carica la prima scena e inizializza tutti gli oggetti di servizio come ad esempio i controller.
// la prima scena viene caricata con l'aiuto di FxmlViewLOader, inoltre questa classe contiene il main
// da cui viene effettivamente fatta partire la gui
public class GuiApplication extends Application implements ViewAPI {

    private Client client;
    private BuildingPhaseController buildingPhaseController;
    private CardDialogController cardDialogController;
    private CheckBoardController checkBoardController;
    private FlightPhaseController flightPhaseController;
    private LobbyController lobbyController;
    private TimerController timerController;

    public GuiApplication() {
        BuildingPhaseController buildingPhaseController = new BuildingPhaseController();
        CardDialogController cardDialogController = new CardDialogController();
        CheckBoardController checkBoardController = new CheckBoardController();
        FlightPhaseController flightPhaseController = new FlightPhaseController();
        LobbyController lobbyController = new LobbyController();
        TimerController timerController = new TimerController();
    }


    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/hello-view.fxml")
        );
        Parent root = loader.load();


        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Galaxy Trucker");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


    @Override
    public void setClient(Client client) {
        this.client = client;
        this.buildingPhaseController.setClient(client);
        this.cardDialogController.setClient(client);
        this.checkBoardController.setClient(client);
        this.flightPhaseController.setClient(client);
        this.lobbyController.setClient(client);
        this.timerController.setClient(client);
    }

    @Override
    public void init() {

    }

    @Override
    public void showRequestLevel() {

    }

    @Override
    public void showRequestNumPlayers() {

    }

    @Override
    public void showAppropriateUsername(String username) {

    }

    @Override
    public void showWrongUsername() {

    }

    @Override
    public void showTile(Component requested) {

    }

    @Override
    public void showShip(Component[][] ship, int[][] validCoordinates) {

    }

    @Override
    public void showUncovered(ArrayList<Component> uncovered, int lastVersion) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void stateChanged(GameStatus newState) {

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
}
