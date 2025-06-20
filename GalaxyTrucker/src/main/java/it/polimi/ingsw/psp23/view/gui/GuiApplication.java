package it.polimi.ingsw.psp23.view.gui;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.GetEventVisitor;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.rmi.ClientRMI;
import it.polimi.ingsw.psp23.network.socket.ClientSocket;
import it.polimi.ingsw.psp23.protocol.response.HandleEventVisitor;
import it.polimi.ingsw.psp23.protocol.response.SelectLevel;
import it.polimi.ingsw.psp23.view.ViewAPI;
import it.polimi.ingsw.psp23.view.gui.guicontrollers.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import javafx.scene.control.Button;


// carica la prima scena e inizializza tutti gli oggetti di servizio come ad esempio i controller.
// la prima scena viene caricata con l'aiuto di FxmlViewLOader, inoltre questa classe contiene il main
// da cui viene effettivamente fatta partire la gui
public class GuiApplication extends Application implements ViewAPI {
    private static final CountDownLatch latch = new CountDownLatch(1);
    private Client client;
    private  BuildingPhaseController buildingPhaseController;
    private  FlightPhaseController flightPhaseController;
    private FlightBoardController2 flightBoardController2;
    private FlightBoardController0 flightBoardController0;
    private  LobbyController lobbyController;
    private DeckViewController deckViewController;
    private  TimerController timerController;
    private Stage stage;
    private static GuiApplication instance;
    private Color playerColor;
    private int level;
    private StackPane buildedShip = null;
    private Scene buildingPhaseScene = null;
    private Scene flightBoardScene = null;
    private Scene flightPhaseScene = null;
    private GameStatus gameStatus;
    String myNickname;
    ArrayList<String> usernames = new ArrayList<>();


    public static void awaitStart() throws InterruptedException {
        latch.await(); // aspetta finché start() non ha finito
    }

    public Client getClient() {
        return client;
    }

    public int getLevel() {
        return level;
    }

    public static GuiApplication getInstance() {
        return instance;
    }

    public GuiApplication() {
        instance = this;
    }


    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/lobby-view.fxml")
        );

        Parent root = loader.load();

        this.lobbyController = loader.getController();
        lobbyController.setClient(client);

        stage.setResizable(false);
        Scene scene = new Scene(root,1152,768);
        stage.setTitle("Galaxy Trucker");
        stage.setScene(scene);
        stage.show();
        this.stage = stage;
        latch.countDown();
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
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
    public void setupRMI(String nameConnection) throws RemoteException{
        if(client.getGameServer().getNumPlayersConnected(client.getId()) == 1) {

            Message msg = (new DirectMessage(new SelectLevel()));
            client.getGameServer().sendToUser(nameConnection, msg);
        }
        else{
            lobbyController.showUserChoice();
        }
    }

    public void toBuildingPhase(Color playerColor) {
        if(buildingPhaseScene == null) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/build-view.fxml")
            );
            try {
                Parent root = loader.load();
                this.buildingPhaseController = loader.getController();
                buildingPhaseController.setClient(client);
                Scene scene = new Scene(root, 1152, 768);
                buildingPhaseScene = scene;
                buildedShip = buildingPhaseController.getShip();
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
        lobbyController.showLevelChoice();
    }

    @Override
    public void showRequestNumPlayers() {
        lobbyController.showNumPlayers();
    }

    @Override
    public void showAppropriateUsername(String username, int level) {
        myNickname = username;
        lobbyController.hideUserChoice();
        this.level = level;
    }

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

    @Override
    public void showShip(Component[][] ship, int[][] validCoordinates) {

    }

    @Override
    public void showUncovered(ArrayList<Component> uncovered, int lastVersion) {
        buildingPhaseController.showUncovered(uncovered, lastVersion);
    }

    @Override
    public void showError(String error) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(error);
            // pop-up che blocca esecuzione finchè l'utente non chiude la finestra
            alert.showAndWait();
        });
    }

    @Override
    public void showMessage(String message) {
        Platform.runLater(()  -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(message);
            // pop-up che blocca esecuzione finchè l'utente non chiude la finestra
            alert.showAndWait();
        });
    }

    @Override
    public void stateChanged(GameStatus newState) {
        gameStatus = newState;
        Platform.runLater(() -> {
            switch(newState) {
                case GameStatus.Building -> toBuildingPhase(playerColor);
                case GameStatus.SetCrew -> buildingPhaseController.toAddCrew();
                case GameStatus.Playing -> toFlightPhase();
            }
        });

    }

    @Override
    public void showTurn(String turn) {

    }

    @Override
    public void showStart() {

    }

    @Override
    public void showIllegalTruck() {
        buildingPhaseController.toCheck();
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
                    Scene scene = new Scene(root, 1152, 768);
                    String imagePath1 = "/it/polimi/ingsw/psp23/images/cards/" + idCards.get(0) + ".jpg";
                    String imagePath2 = "/it/polimi/ingsw/psp23/images/cards/" + idCards.get(1) + ".jpg";
                    String imagePath3 = "/it/polimi/ingsw/psp23/images/cards/" + idCards.get(2) + ".jpg";
                    card1 = deckViewController.getCard1();
                    card2 = deckViewController.getCard2();
                    card3 = deckViewController.getCard3();
                    card1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath1))));
                    card2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath2))));
                    card3.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath3))));
                Platform.runLater(() -> {
                    stage.setScene(scene);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void showNewCard(int id, String description) {

        flightPhaseController.getButton1().setVisible(false);
        flightPhaseController.getButton2().setVisible(false);
        flightPhaseController.getButton3().setVisible(false);
        flightPhaseController.getButton4().setVisible(false);
        flightPhaseController.getButton5().setVisible(false);
        flightPhaseController.getButton6().setVisible(false);

        Platform.runLater(() -> {
            flightPhaseController.getTextLabel().setText("");
            flightPhaseController.getButton1().setVisible(false);
            flightPhaseController.getButton1().setManaged(false);
            flightPhaseController.setCardImage(id);
        });

        switch(gameStatus) {
            case GameStatus.INIT_ABANDONEDSHIP -> {
                flightPhaseController.getButton1().setText("Conquista Nave");
                flightPhaseController.getButton1().setManaged(true);
                flightPhaseController.getButton1().setVisible(true);
            }
            case GameStatus.INIT_PLANETS -> {
                flightPhaseController.getButton1().setText("Atterra Pianeta 1");
                flightPhaseController.getButton2().setText("Atterra Pianeta 2");
                flightPhaseController.getButton3().setText("Atterra Pianeta 3");
                flightPhaseController.getButton4().setText("Atterra Pianeta 4");
                flightPhaseController.getButton5().setText("Atterra Pianeta 5");
                switch(id){
                    case 102 ->{
                        flightPhaseController.getButton1().setManaged(true);
                        flightPhaseController.getButton1().setVisible(true);
                        flightPhaseController.getButton2().setManaged(true);
                        flightPhaseController.getButton2().setVisible(true);
                        flightPhaseController.getButton3().setManaged(true);
                        flightPhaseController.getButton3().setVisible(true);
                        flightPhaseController.getButton4().setManaged(true);
                        flightPhaseController.getButton4().setVisible(true);
                    }
                    case 112 ->{

                    }
                    case 113 ->{

                    }
                    case 114 ->{

                    }
                    case 204 ->{

                    }
                    case 205 ->{

                    }
                    case 206 ->{

                    }
                    case 207 ->{

                    }
                }
            }
            case GameStatus.Playing -> {

            }
        }
    }

    @Override
    public void incorrectTile(){
        Platform.runLater(() -> {
            buildingPhaseController.getTileInHand().setVisible(true);
            buildingPhaseController.getCellToRemove().getChildren().clear();
        });

    }

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
                Button button1 = flightPhaseController.getButton1();
                button1.setVisible(true);
                button1.setManaged(true);
                button1.setText("Pesca carta");
                button1.setOnAction(e -> {
                    flightPhaseController.drawCard();
                });
                flightPhaseController.setShip(buildedShip);
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

    public ArrayList<String> getOtherUsers() {
        ArrayList<String> copy = new ArrayList<>(usernames); // copia difensiva
        copy.remove(myNickname); // rimuove il proprio nickname
        return copy;
    }

    public void backToShip(){
        if(gameStatus == GameStatus.Building)
            toBuildingPhase(null);
        else if(gameStatus == GameStatus.Playing)
            toFlightPhase();
        else
            toBuildingPhase(null);
    }

    @Override
    public void showAvailableLobbies(List<List<Integer>> availableLobbies){
        // TODO: Implementare!!!!
    }


}

