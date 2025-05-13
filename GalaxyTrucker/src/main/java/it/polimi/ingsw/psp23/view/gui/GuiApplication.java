package it.polimi.ingsw.psp23.view.gui;

import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.socket.Client;
import it.polimi.ingsw.psp23.view.ViewAPI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
// carica la prima scena e inizializza tutti gli oggetti di servizio come ad esempio i controller.
// la prima scena viene caricata con l'aiuto di FxmlViewLOader, inoltre questa classe contiene il main
// da cui viene effettivamente fatta partire la gui
public class GuiApplication extends Application implements ViewAPI {
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
    public void init() {

    }

    @Override
    public void setClient(Client client) {

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
