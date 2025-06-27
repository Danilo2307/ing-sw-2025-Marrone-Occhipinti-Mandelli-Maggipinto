package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.exceptions.InvalidCoordinatesException;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.socket.ClientSocket;
import it.polimi.ingsw.psp23.protocol.request.*;
import it.polimi.ingsw.psp23.view.gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuildingPhaseController {
    private Client client;
    int lastVersion;

    // building
    @FXML private StackPane boardStack;
    @FXML private ImageView board;
    @FXML private GridPane ship;

    // build
    @FXML private Button releaseBtn;
    @FXML private Button rotateBtn;
    @FXML private Button putBtn;
    @FXML private Button turnBtn;
    @FXML private Button leaveBtn;
    @FXML private Button drawHeapBtn;

    // view other ships
    @FXML private Button player1;
    @FXML private Button player2;
    @FXML private Button player3;

    // build
    @FXML private ImageView tileInHand;
    @FXML private HBox uncoveredBox;
    @FXML private ScrollPane uncoveredScrollPane;
    @FXML private Button uncoveredRefresh;
    @FXML private StackPane reserved1;
    @FXML private StackPane reserved2;

    // helpers
    boolean reservedInHand = false;
    Component componentInHand;
    private StackPane cellToRemove = null;


    // check
    @FXML private ImageView binCheck;
    @FXML private Button shipCorrected;

    // add crew
    @FXML private Button astronautBtn;
    @FXML private Button purpleAlienBtn;
    @FXML private Button brownAlienBtn;
    @FXML private Button finishedBtn;
    int selectedCrewType;
    boolean inAddCrew = false;


    private void enable(Button button) {
        button.setVisible(true);
        button.setManaged(true);
    }

    private void disable(Button button) {
        button.setVisible(false);
        button.setManaged(false);
    }

    public ImageView getTileInHand() {
        return tileInHand;
    }

    public StackPane getCellToRemove() {
        return cellToRemove;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void initialize() {

        if(GuiApplication.getInstance().getLevel() == 0){
            board.setImage( new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/cardboard/cardboard-1.jpg"))));
            turnBtn.setVisible(false);
            turnBtn.setManaged(false);
        }else{
            board.setImage( new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/cardboard/cardboard-1b.jpg"))));
        }

        board.fitWidthProperty().bind(boardStack.widthProperty());
        board.fitHeightProperty().bind(boardStack.heightProperty());

        ship.prefWidthProperty().bind(boardStack.widthProperty());
        ship.prefHeightProperty().bind(boardStack.heightProperty());

        //inizializzo la gridpane con degli stackpane che conterranno le tiles
        int rows = 5;
        int cols = 7;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(!(i== 2 && j== 3) && !(i==0 && (j == 5 || j == 6))) {
                    StackPane cell = new StackPane();
                    cell.setPrefSize(94, 94);

                    ship.add(cell, j, i);
                    setupCellForDrop(cell, i, j);
                }
            }
        }

        if (GuiApplication.getInstance().getLevel() == 2) {
            setupReserveSlot(reserved1);
            setupReserveSlot(reserved2);
        }

        // disabilito tutti i pulsanti che serviranno per le fasi di check e addcrew
        binCheck.setVisible(false);
        binCheck.setManaged(false);
        shipCorrected.setVisible(false);
        shipCorrected.setManaged(false);
        purpleAlienBtn.setVisible(false);
        purpleAlienBtn.setManaged(false);
        brownAlienBtn.setVisible(false);
        brownAlienBtn.setManaged(false);
        astronautBtn.setVisible(false);
        astronautBtn.setManaged(false);
        finishedBtn.setVisible(false);
        finishedBtn.setManaged(false);
    }

    @FXML
    private void handleDragDetected(MouseEvent event) {
        if (tileInHand.getImage() == null) {
            event.consume(); // blocco il drag se non c'è immagine
            return;
        }
        Dragboard db = tileInHand.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage(tileInHand.getImage());
        db.setContent(content);
        event.consume();
    }

    private void setupCellForDrop(StackPane cell, int row, int col) {

        cell.setOnMouseEntered(e -> {
            cell.getStyleClass().removeAll("cell-default");
            cell.getStyleClass().add("cell-hover");
        });

        cell.setOnMouseExited(e -> {
            cell.getStyleClass().removeAll("cell-hover", "cell-highlight");
            cell.getStyleClass().add("cell-default");
        });

        cell.setOnDragEntered(event -> {
            if (event.getDragboard().hasImage()) {
                cell.getStyleClass().removeAll("cell-hover", "cell-default");
                cell.getStyleClass().add("cell-highlight");
            }
        });

        cell.setOnDragExited(event -> {
            cell.getStyleClass().removeAll("cell-highlight");
            cell.getStyleClass().add("cell-default");
        });
        cell.setOnDragOver(event -> {
            if (event.getGestureSource() != cell && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        cell.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = true;

            if (db.hasImage()) {
                cellToRemove = cell;
                ImageView dropped = new ImageView(db.getImage());
                dropped.setFitWidth(94);
                dropped.setFitHeight(94);
                dropped.setRotate(componentInHand.getRotate());
                cell.getChildren().add(dropped);
                tileInHand.setVisible(false); // svuota la mano

                try {
                    client.sendAction(new AddTile(row,col));
                    if (reservedInHand) {
                        reservedInHand = false;
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public void setupReserveSlot(StackPane slot) {
        slot.setOnDragOver(event -> {
            if (event.getGestureSource() != slot && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        slot.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasImage()) {
                ImageView dropped = new ImageView(db.getImage());
                dropped.setFitWidth(86);
                dropped.setFitHeight(86);
                slot.getChildren().clear(); // rimuovi vecchie immagini
                slot.getChildren().add(dropped);
                try {
                    client.sendAction(new ReserveTile());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                tileInHand.setImage(null);
                tileInHand.setVisible(false);
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    @FXML
    public void takeReserved1() throws RemoteException {
        if (!reserved1.getChildren().isEmpty()) {
            ImageView imageView = (ImageView) reserved1.getChildren().get(0);
            if (imageView.getImage() != null) {
                client.sendAction(new TakeReservedTile(0));
                tileInHand.setImage(imageView.getImage());
                tileInHand.setVisible(true);
                reserved1.getChildren().remove(imageView); // rimuovi dalla riserva
                reservedInHand = true;
            }
        }
    }

    @FXML
    public void takeReserved2() throws RemoteException {
        if (!reserved2.getChildren().isEmpty()) {
            ImageView imageView = (ImageView) reserved2.getChildren().get(0);
            if (imageView.getImage() != null) {
                client.sendAction(new TakeReservedTile(1));
                tileInHand.setImage(imageView.getImage());
                tileInHand.setVisible(true);
                reserved2.getChildren().remove(imageView);
                reservedInHand = true;
            }
        }
    }

    public StackPane getShip(){
        return boardStack;
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

    public void setupViewOtherShipsBtn() {
        ArrayList<String> otherPlayers = GuiApplication.getInstance().getOtherUsers();

        player1.setText(otherPlayers.get(0));
        enable(player1);

        if (otherPlayers.size() == 2 || otherPlayers.size() == 3) {
            player2.setText(otherPlayers.get(1));
            enable(player2);
        }
        if (otherPlayers.size() == 3) {
            player3.setText(otherPlayers.get(2));
            enable(player3);
        }
    }

    @FXML
    public void onDrawHeapClicked() throws RemoteException {
        client.sendAction(new DrawFromHeap());
    }

    @FXML
    public void onReleaseClicked() throws RemoteException{
        if (!reservedInHand) {
            client.sendAction(new ReleaseTile());
            Platform.runLater(() -> {
                tileInHand.setImage(null);
            });
        }
        else {
            GuiApplication.getInstance().showError("Non puoi scartare una tile prenotata!");
        }
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
        //GuiApplication.getInstance().disableDeckClick();

        // annullo tutti i bottoni della building
        hideBuildCommands();
    }

    public void hideBuildCommands() {
        releaseBtn.setVisible(false);
        releaseBtn.setManaged(false);
        rotateBtn.setVisible(false);
        rotateBtn.setManaged(false);
        putBtn.setVisible(false);
        putBtn.setManaged(false);
        turnBtn.setVisible(false);
        turnBtn.setManaged(false);
        drawHeapBtn.setVisible(false);
        drawHeapBtn.setManaged(false);
        tileInHand.setVisible(false);
        tileInHand.setManaged(false);
        uncoveredBox.getChildren().clear();
        uncoveredBox.setManaged(false);
        uncoveredBox.setVisible(false);
        uncoveredScrollPane.setVisible(false);
        uncoveredScrollPane.setManaged(false);
        uncoveredRefresh.setVisible(false);
        uncoveredRefresh.setManaged(false);

        if (GuiApplication.getInstance().getLevel() == 2) {
            reserved1.setVisible(false);
            reserved1.setManaged(false);
            reserved2.setVisible(false);
            reserved2.setManaged(false);
        }
    }

    @FXML
    public void onLeaveClicked() throws RemoteException{
        client.sendAction(new LeaveFlight());
    }

    @FXML
    public void onTurnClicked() throws RemoteException{
        client.sendAction(new TurnHourglass());
    }

    public void showTile(Component toDraw) {
        if (inAddCrew)
            return;

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

    @FXML
    public void onFlightBoardClicked(){
        try {
            client.sendAction(new RequestFlightBoard());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void toCheck() {
        Platform.runLater(() -> {
            binCheck.setVisible(true);
            binCheck.setManaged(true);
            shipCorrected.setVisible(true);
            shipCorrected.setManaged(true);
            tileInHand.setImage(null);
            tileInHand.setVisible(false);
            tileInHand.setManaged(false);

            // ATTIVO DRAG&DROP per rimuovere tiles
            // Per ogni nodo nella griglia, aggiungo listener di drag
            for (Node node : ship.getChildren()) {
                node.setOnDragDropped(null);
                int row = GridPane.getRowIndex(node) != null ? GridPane.getRowIndex(node) : 0;
                int col = GridPane.getColumnIndex(node) != null ? GridPane.getColumnIndex(node) : 0;

                List<Node> children = ((Parent) node).getChildrenUnmodifiable();

                if (!children.isEmpty()) {
                    ImageView imageView = (ImageView) children.get(0); // sicuro per contratto

                    final int r = row;
                    final int c = col;

                    imageView.setOnDragDetected(event -> {
                        Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
                        ClipboardContent content = new ClipboardContent();
                        content.putImage(imageView.getImage());
                        content.putString(r + "," + c);
                        db.setContent(content);
                        event.consume();
                    });
                }
            }

            // Drag target: binCheck
            binCheck.setOnDragOver(event -> {
                if (event.getGestureSource() != binCheck && event.getDragboard().hasImage()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            binCheck.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasImage() && db.hasString()) {
                    String[] parts = db.getString().split(",");
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);

                    // Rimuovi l'immagine dalla griglia
                    ship.getChildren().removeIf(node ->
                            GridPane.getRowIndex(node) == row &&
                                    GridPane.getColumnIndex(node) == col
                    );

                    try {
                        client.sendAction(new RemoveTile(row, col));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });

        });
    }

    @FXML
    public void onShipCorrected() throws RemoteException{
        client.sendAction(new Fixed());
        // tolgo bottoni check
        binCheck.setVisible(false);
        binCheck.setOnDragDropped(null);
        binCheck.setOnDragOver(null);
        binCheck.setManaged(false);
        shipCorrected.setVisible(false);
        shipCorrected.setManaged(false);
        // disabilito drag&drop
        for (Node node : ship.getChildren()) {
            node.setOnDragDetected(null);
        }
    }

    private void sendCrewAction(int row, int col, boolean isAlien, Color color) {
        try {
            client.sendAction(new SetCrew(row, col, isAlien, color));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void toAddCrew() {
        inAddCrew = true;
        Platform.runLater(() -> {
            astronautBtn.setVisible(true);
            astronautBtn.setManaged(true);
            finishedBtn.setVisible(true);
            finishedBtn.setManaged(true);

            if (GuiApplication.getInstance().getLevel() == 2) {
                purpleAlienBtn.setVisible(true);
                purpleAlienBtn.setManaged(true);
                brownAlienBtn.setVisible(true);
                brownAlienBtn.setManaged(true);
            }

            // assegno a ogni click un 'id' per salvare l'informazione della crew selezionata
            astronautBtn.setOnAction(e -> selectedCrewType = 1);
            purpleAlienBtn.setOnAction(e -> selectedCrewType = 2);
            brownAlienBtn.setOnAction(e -> selectedCrewType = 3);

            // creo i listener su ogni tile
            for (Node tile : ship.getChildren()) {
                // il click sulla tile triggera l'esecuzione di questo "metodo"
                tile.setOnMouseClicked(mouseEvent -> {
                    int row = GridPane.getRowIndex(tile) != null ? GridPane.getRowIndex(tile) : 0;
                    int col = GridPane.getColumnIndex(tile) != null ? GridPane.getColumnIndex(tile) : 0;
                    switch(selectedCrewType) {
                        case 1 -> sendCrewAction(row, col, false, null);
                        case 2 -> sendCrewAction(row, col, true, Color.Purple);
                        case 3 -> sendCrewAction(row, col, true, Color.Brown);
                    }
                    // ripristino valore
                    selectedCrewType = 0;
                });
            }
        });
    }

    @FXML
    public void onFinishedClicked() throws RemoteException {
        client.sendAction(new Finished());

        // disabilito tutti i pulsanti
        purpleAlienBtn.setVisible(false);
        purpleAlienBtn.setManaged(false);
        brownAlienBtn.setVisible(false);
        brownAlienBtn.setManaged(false);
        astronautBtn.setVisible(false);
        astronautBtn.setManaged(false);
        finishedBtn.setVisible(false);
        finishedBtn.setManaged(false);
    }

    public GridPane getBuildedGrid() {
        return ship;
    }

    @FXML
    public void onSpyOthersClicked(javafx.event.ActionEvent event) throws RemoteException {
        Button clickedButton = (Button) event.getSource();  // bottone che ha scatenato l’evento
        String username = clickedButton.getText();          // testo del bottone = username

        client.sendAction(new RequestShip(username));       // invia richiesta con username selezionato
    }


}
