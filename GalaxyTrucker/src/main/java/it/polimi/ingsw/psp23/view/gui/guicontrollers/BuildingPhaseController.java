package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.socket.Client;
import it.polimi.ingsw.psp23.protocol.request.*;
import javafx.fxml.FXML;
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
            if (tileInHand.getImage() == null) return;

            Dragboard db = tileInHand.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(tileInHand.getImage());

            // Inserisci la rotazione come stringa
            content.putString(String.valueOf(tileInHand.getRotate()));

            db.setContent(content);
            event.consume();});
    }

    @FXML
    public void onDrawHeapClicked() {
        client.sendAction(new DrawFromHeap());
    }

    @FXML
    public void onReleaseClicked() {
        client.sendAction(new ReleaseTile());
        tileInHand.setImage(null);
    }

    @FXML
    public void onRotateClicked() {
        client.sendAction(new RotateTile());
        tileInHand.setRotate(tileInHand.getRotate() + 90);
    }

    @FXML
    public void onPutClicked() {
        client.sendAction(new Put());
    }

    @FXML
    public void onLeaveClicked() {
        client.sendAction(new LeaveFlight());
    }

    @FXML
    public void onTurnClicked() {
        client.sendAction(new TurnHourglass());
    }

    @FXML
    public void onReserveClicked() {
        client.sendAction(new ReserveTile());
        tileInHand.setImage(null);
    }

    public void showTile(Component toDraw) {
        String imagePath = "it/polimi/ingsw/psp23/images/tiles/" + toDraw.getId() + ".jpg";
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        tileInHand.setImage(image);
        tileInHand.setVisible(true);
    }

    public void showUncovered(ArrayList<Component> uncovered, int lastVersion) {
        // aggiorno attributo che indica l'ultima versione
        this.lastVersion = lastVersion;

        // pulisco immagini precedenti
        uncoveredBox.getChildren().clear();

        // creo image per ogni tile
        for (Component component : uncovered) {
            String imagePath = "it/polimi/ingsw/psp23/images/tiles/" + component.getId() + ".jpg";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            ImageView imageView = new ImageView(image);

            // aggiungo la tile all'HBox
            uncoveredBox.getChildren().add(imageView);
            // associo un listener di ClickEvent ad ogni imageview creata
            imageView.setOnMouseClicked(mouseEvent -> drawUncovered(imageView));
        }
        // forzo aggiornamento all'inizio della lista
        uncoveredScrollPane.setHvalue(0.0);
    }

    private void drawUncovered(ImageView imageView) {
        int index = uncoveredBox.getChildren().indexOf(imageView);
        client.sendAction(new DrawFromFaceUp(index, lastVersion));
    }

    // codice che fissa la tile dopo il drop e la leva dalla mano
    @FXML
    private void handleTileDrop(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasImage()) {
            // Target: StackPane dove si è fatto il drop
            StackPane target = (StackPane) event.getGestureTarget();

            // Copia l'immagine
            ImageView dropped = new ImageView(db.getImage());
            dropped.setFitWidth(60);
            dropped.setPreserveRatio(true);

            // Recupera la rotazione passata come stringa e applicala
            double rotation = Double.parseDouble(db.getString());
            dropped.setRotate(rotation);

            // Mostra l'immagine nella cella
            target.getChildren().clear();
            target.getChildren().add(dropped);

            // Rimuovi immagine originale dal tile
            tileInHand.setImage(null);

            success = true;

            // invio azione di saldatura
            client.sendAction(new AddTile(GridPane.getRowIndex(target),GridPane.getColumnIndex(target)));
        }

        event.setDropCompleted(success);
        event.consume();
    }



}
