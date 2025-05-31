package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.socket.ClientSocket;
import it.polimi.ingsw.psp23.protocol.request.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;

public class BuildingPhaseController {
    private Client client;
    int lastVersion;

    @FXML private StackPane boardStack;
    @FXML private ImageView board;
    @FXML private GridPane ship;
    @FXML private Button releaseBtn;
    @FXML private Button rotateBtn;
    @FXML private Button putBtn;
    @FXML private Button turnBtn;
    @FXML private Button leaveBtn;
    @FXML private Button drawHeapBtn;
    @FXML private Button reserveBtn;
    @FXML private ImageView tileInHand;
    @FXML private HBox uncoveredBox;
    @FXML private ScrollPane uncoveredScrollPane;
    @FXML private Button uncoveredRefresh;
    Component componentInHand;

    public void setClient(Client client) {
        this.client = client;
    }

    public void initialize() {
        board.fitWidthProperty().bind(boardStack.widthProperty());
        board.fitHeightProperty().bind(boardStack.heightProperty());

        ship.prefWidthProperty().bind(boardStack.widthProperty());
        ship.prefHeightProperty().bind(boardStack.heightProperty());

        // codice per permettere il drag della tile (equivale a spostare dati)
        tileInHand.setOnDragDetected(event -> {
            Dragboard db = tileInHand.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(tileInHand.getImage());
            db.setContent(content);
            event.consume();
        });
    }

    public void setCentral(Color playerColor) {
        int id = 0;
        switch (playerColor) {
            case Blue -> id = 900;
            case Green -> id = 901;
            case Red -> id = 902;
            case Yellow -> id = 903;
        }

        int finalId = id;
        Platform.runLater(() -> {
            String imagePath = "/it/polimi/ingsw/psp23/images/tiles/" + finalId + ".jpg";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            ImageView imageView = new ImageView(image);
            StackPane pane = new StackPane(imageView);
            pane.setAlignment(Pos.CENTER);
            imageView.setFitHeight(94);
            imageView.setFitWidth(94);
            ship.add(pane, 3, 2);
        });

    }

    @FXML
    public void onDrawHeapClicked() throws RemoteException {
        client.sendAction(new DrawFromHeap());
    }

    @FXML
    public void onReleaseClicked() throws RemoteException{
        client.sendAction(new ReleaseTile());
        Platform.runLater(() -> {
            tileInHand.setImage(null);
        });
    }

    @FXML
    public void onRotateClicked() throws RemoteException{
        int rotation = componentInHand.getRotate();
        client.sendAction(new RotateTile());
        Platform.runLater(() -> {
            tileInHand.setRotate(rotation + 90);
        });
    }

    @FXML
    public void onPutClicked() throws RemoteException{
        client.sendAction(new Put());
    }

    @FXML
    public void onLeaveClicked() throws RemoteException{
        client.sendAction(new LeaveFlight());
    }

    @FXML
    public void onTurnClicked() throws RemoteException{
        client.sendAction(new TurnHourglass());
    }

    @FXML
    public void onReserveClicked() throws RemoteException{
        client.sendAction(new ReserveTile());
        Platform.runLater(() -> {
            tileInHand.setImage(null);
        });
    }

    public void showTile(Component toDraw) {
        componentInHand = toDraw;
        Platform.runLater(() -> {
            String imagePath = "/it/polimi/ingsw/psp23/images/tiles/" + toDraw.getId() + ".jpg";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            tileInHand.setRotate(componentInHand.getRotate());
            tileInHand.setImage(image);
            tileInHand.setVisible(true);
        });
    }

    public void showUncovered(ArrayList<Component> uncovered, int lastVersion) {
        // aggiorno attributo che indica l'ultima versione
        this.lastVersion = lastVersion;

        Platform.runLater(() -> {
            // pulisco immagini precedenti
            uncoveredBox.getChildren().clear();

            // creo image per ogni tile
            for (Component component : uncovered) {
                String imagePath = "/it/polimi/ingsw/psp23/images/tiles/" + component.getId() + ".jpg";
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(101);
                imageView.setFitHeight(97);

                // aggiungo la tile all'HBox
                imageView.setRotate(component.getRotate());
                uncoveredBox.getChildren().add(imageView);


                // associo un listener di ClickEvent ad ogni imageview creata
                imageView.setOnMouseClicked(mouseEvent -> {
                    try {
                        drawUncovered(imageView);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
            }
            // forzo aggiornamento all'inizio della lista
            uncoveredScrollPane.setHvalue(0.0);
        });
    }

    private void drawUncovered(ImageView imageView) throws RemoteException {
        int index = uncoveredBox.getChildren().indexOf(imageView);
        client.sendAction(new DrawFromFaceUp(index, lastVersion));
    }



    @FXML
    public void onUncoveredRefresh() throws RemoteException{
        client.sendAction(new RequestUncovered());
    }



}
