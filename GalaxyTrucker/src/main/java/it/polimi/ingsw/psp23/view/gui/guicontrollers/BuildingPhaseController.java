package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.exceptions.InvalidCoordinatesException;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
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


/**
 * The BuildingPhaseController class is responsible for managing the building phase of a game.
 * It interacts with the user interface and processes various user actions related to building,
 * reserving, and managing tiles and components during the game phase.
 *
 * Class members include functionality to handle:
 * - Drag-and-drop interactions for placing/removing tiles and components
 * - Managing client-server communication for actions like reserving tiles, drawing from heaps, and more
 * - Updating UI components such as buttons and slots
 * - Handling events triggered by user interactions (e.g., rotating components, adding crew members).
 */
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
    private boolean fromReserved = false;


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
    boolean putClicked = false;

    /**
     * Enables the specified button by making it visible and allowing it to be managed by the layout.
     *
     * @param button the button to be enabled
     */
    private void enable(Button button) {
        button.setVisible(true);
        button.setManaged(true);
    }

    /**
     * Disables the specified button by setting its visibility and manageability to false.
     *
     * @param button the button to be disabled
     */
    private void disable(Button button) {
        button.setVisible(false);
        button.setManaged(false);
    }

    public ImageView getTileInHand() {
        return tileInHand;
    }

    /**
     * Handles the event when a timer ends by toggling the visibility and manageability
     * of the "put" button if it has not been clicked.
     *
     * This method ensures that the "put" button is visible and managed only if the
     * user has not already interacted with it by clicking it.
     */
    public void timerEnded() {
        if(!putClicked) {
                putBtn.setVisible(true);
                putBtn.setManaged(true);
        }
    }

    /**
     * Retrieves the current cell to be removed in the BuildingPhaseController.
     *
     * @return the StackPane representing the cell to remove
     */
    public StackPane getCellToRemove() {
        return cellToRemove;
    }

    /**
     * Sets the client instance for this controller.
     *
     * @param client the client to be associated with this controller.
     *               This client typically manages communication and
     *               interactions with the game server.
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Initializes the graphical user interface for the building phase of the game.
     *
     * Depending on the game level, this method adjusts the board image and manages visibility
     * and layout of specific elements. It sets up the grid for the player's ship with appropriate
     * drop capabilities for tiles, reserving some cells based on level-specific conditions.
     */
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

    /**
     * Handles the drag-and-drop detection when a drag event is initiated on the tile in hand.
     * Initiates a drag operation if there is an image present in the tile being interacted with.
     * Consumes the event to prevent further propagation.
     *
     * @param event the MouseEvent associated with the drag detection action
     */
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

    /**
     * Configures a StackPane cell to handle various user interactions, including mouse hover
     * effects and drag-and-drop events. This method manages the visual feedback for the cell
     * during these interactions and processes actions for dropping a tile into the cell.
     *
     * @param cell the StackPane representing the cell to configure for user interaction
     * @param row the row index of the cell in the grid
     * @param col the column index of the cell in the grid
     */
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
                dropped.setRotate(tileInHand.getRotate());
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

    /**
     * Sets up a StackPane to handle drag-and-drop functionality for reserving a tile.
     * The method configures the given StackPane with event handlers for drag over and drag drop actions.
     * It checks if the drag event contains an image and processes the drop to add the image
     * to the `slot` and send a reserve action to the client.
     *
     * @param slot the StackPane that serves as the target for drag-and-drop operations
     */
    private void setupReserveSlot(StackPane slot) {
        slot.setOnDragOver(event -> {
            if (event.getGestureSource() != slot && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        slot.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasImage() && !fromReserved) {
                ImageView dropped = new ImageView(db.getImage());
                dropped.setFitWidth(86);
                dropped.setFitHeight(86);
                slot.getChildren().clear(); // rimuovi vecchie immagini
                dropped.setRotate(tileInHand.getRotate());
                slot.getChildren().add(dropped);
                try {
                    client.sendAction(new ReserveTile());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                tileInHand.setVisible(false);
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    /**
     * Handles the action of picking up the first reserved tile (index 0) from the reserved slot.
     * This operation relies on communication to handle the action on the server side.
     *
     * @throws RemoteException if a communication-related exception occurs during the client action.
     */
    @FXML
    public void takeReserved1() throws RemoteException {
        fromReserved = true;
        if (!reserved1.getChildren().isEmpty()) {
            ImageView imageView = (ImageView) reserved1.getChildren().get(0);
            if (imageView.getImage() != null) {
                client.sendAction(new TakeReservedTile(0));
                tileInHand.setImage(imageView.getImage());
                tileInHand.setRotate(imageView.getRotate());
                tileInHand.setVisible(true);
                reserved1.getChildren().remove(imageView); // rimuovi dalla riserva
                reservedInHand = true;
            }
        }
    }

    /**
     * Handles the process of taking the reserved tile from the second reserved slot.
     * This method checks if the second reserved slot contains a tile. If a tile is present,
     * it sends an action to the client to take the reserved tile, sets the image of the
     * tile in the player's hand to the tile from the reserved slot, and makes the necessary
     * updates to the UI components.
     *
     * @throws RemoteException if there is an issue communicating with the server or performing
     *                         the required remote operation.
     */
    @FXML
    public void takeReserved2() throws RemoteException {
        fromReserved = true;
        if (!reserved2.getChildren().isEmpty()) {
            ImageView imageView = (ImageView) reserved2.getChildren().get(0);
            if (imageView.getImage() != null) {
                client.sendAction(new TakeReservedTile(1));
                tileInHand.setImage(imageView.getImage());
                tileInHand.setRotate(imageView.getRotate());
                tileInHand.setVisible(true);
                reserved2.getChildren().remove(imageView);
                reservedInHand = true;
            }
        }
    }

    public StackPane getShip(){
        return boardStack;
    }

    /**
     * Updates the central cell of the ship's grid layout based on the provided player's color.
     * This method dynamically sets up an image corresponding to the player's color in the
     * central position of the grid. The image is determined by mapping the specified color
     * to a unique identifier.
     *
     * @param playerColor the color of the player, represented as an enum value of type {@code Color}.
     *                    It determines which image is displayed in the central grid cell.
     */
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

    /**
     * Configures the view to display buttons corresponding to other players' ships.
     *
     * This method retrieves the list of other players available from the {@code GuiApplication}
     * instance and uses it to dynamically set the text and enable the buttons related
     * to those players.
     */
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

    /**
     * Handles the event triggered when the user clicks the "Draw from Heap" button.
     *
     * This method sends a {@link DrawFromHeap} action to the server through the client.
     * The action informs the server that the player wishes to draw a component
     * from the heap.
     *
     * @throws RemoteException if there is an issue with the remote communication
     *                         while attempting to send the action to the server.
     */
    @FXML
    public void onDrawHeapClicked() throws RemoteException {
        fromReserved = false;
        client.sendAction(new DrawFromHeap());
    }

    /**
     * Handles the user action of releasing a tile currently held in hand.
     *
     * If the tile in hand is not reserved, the method sends a {@code ReleaseTile} action to the server,
     * indicating the intent to discard the tile. Upon successful execution, the tile in hand is removed
     * from the UI.
     * If the tile in hand is reserved, an error message is displayed, notifying the user that reserved tiles
     * cannot be discarded.
     *
     * @throws RemoteException if there is an issue communicating with the server while sending the action
     */
    @FXML
    public void onReleaseClicked() throws RemoteException{
        if (!reservedInHand) {
            client.sendAction(new ReleaseTile());
            Platform.runLater(() -> {
                tileInHand.setVisible(false);
            });
        }
        else {
            GuiApplication.getInstance().showError("Non puoi scartare una tile prenotata!");
        }
    }

    /**
     * Handles the event triggered when the "Rotate" button is clicked.
     * This method performs a 90-degree clockwise rotation of the tile currently held in hand.
     * It retrieves the current rotation value of the tile in hand, sends a {@code RotateTile}
     * action to the server through the client, and updates the rotation of the tile in the UI
     * on the JavaFX application thread.
     *
     * @throws RemoteException if an error occurs while communicating with the server.
     */
    @FXML
    public void onRotateClicked() throws RemoteException{
        double rotation;
        if(fromReserved)
            rotation = tileInHand.getRotate();
        else
            rotation = componentInHand.getRotate();
        client.sendAction(new RotateTile());
        Platform.runLater(() -> {
            tileInHand.setRotate(rotation + 90);
        });
    }

    /**
     * Handles the event triggered when the "Put" button is clicked by the user.
     * This method performs the following actions:
     * 1. Sets the {@code putClicked} flag to true, indicating that the "Put" button
     *    has been interacted with.
     * 2. Sends a {@link Put} action to the server using the {@code client} instance.
     * 3. Disables and hides the building-related commands or button options
     *    by invoking the {@code hideBuildCommands()} method.
     *
     * The method interacts with the server-side through the {@code client.sendAction()} call
     * to notify that the player has completed their building phase and is ready
     * to proceed with the game.
     *
     * @throws RemoteException if there is an issue with the remote communication
     *                         while sending the {@link Put} action to the server.
     */
    @FXML
    public void onPutClicked() throws RemoteException{
        putClicked = true;
        client.sendAction(new Put());
        //GuiApplication.getInstance().disableDeckClick();

        // annullo tutti i bottoni della building
        hideBuildCommands();
    }

    /**
     * Hides and disables the visibility and management of UI components related to
     * build commands in the application.
     * This method adjusts multiple UI elements, including buttons, tiles, boxes,
     * and scroll panes, making them non-visible and unmanaged.
     * Additionally, for level 2 of the application, it clears and disables reserved elements.
     */
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
            reserved1.getChildren().clear();
            reserved1.setVisible(false);
            reserved1.setManaged(false);
            reserved2.getChildren().clear();
            reserved2.setVisible(false);
            reserved2.setManaged(false);
        }
    }


    /**
     * Handles the event triggered when the "Turn" button is clicked by the user.
     *
     * This method sends a {@code TurnHourglass} action to the server through the client.
     * The {@code TurnHourglass} action signifies that the player has completed their ship
     * and the timer should advance.
     *
     * @throws RemoteException if an issue occurs during remote communication
     *                         while sending the {@code TurnHourglass} action to the server.
     */
    @FXML
    public void onTurnClicked() throws RemoteException{
        client.sendAction(new TurnHourglass());
    }

    /**
     * Displays the specified tile component visually by updating the appropriate
     * UI elements. If the system is in the process of adding a crew, the method
     * does nothing.
     *
     * @param toDraw the Component object representing the tile to be displayed.
     *               The method uses this component's properties to determine
     *               the image to show and the rotation angle to apply.
     */
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

    /**
     * Displays the uncovered components in a graphical user interface by updating their visual representation
     * based on the provided list and the specified version.
     *
     * @param uncovered the list of uncovered components to be displayed, each represented by a Component object.
     *                  Each component will have an associated image and a click listener.
     * @param lastVersion an integer indicating the latest version of the uncovered components being updated.
     */
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

    /**
     * Draws an uncovered tile represented by the specified ImageView. The method identifies
     * the index of the provided ImageView in the uncoveredBox container and sends a corresponding
     * action to the client.
     *
     * @param imageView the ImageView object representing the uncovered card to be drawn
     * @throws RemoteException if a communication-related exception occurs
     */
    private void drawUncovered(ImageView imageView) throws RemoteException {
        fromReserved = false;
        int index = uncoveredBox.getChildren().indexOf(imageView);
        client.sendAction(new DrawFromFaceUp(index, lastVersion));
    }


    /**
     * Handles the action of refreshing uncovered items or data.
     * This method sends a request to the client to perform an action related to refreshing uncovered tiles.
     *
     * @throws RemoteException if there is a communication-related error during the remote method call
     */
    @FXML
    public void onUncoveredRefresh() throws RemoteException{
        client.sendAction(new RequestUncovered());
    }

    /**
     * Handles the event when the flight board button is clicked in the user interface.
     * Sends a request for the flight board details to the server through the client object.
     *
     * Throws:
     * RuntimeException - if a RemoteException occurs during the operation.
     */
    @FXML
    public void onFlightBoardClicked(){
        try {
            client.sendAction(new RequestFlightBoard());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Configures the user interface and drag-and-drop functionality for tile management in the check-phase.
     * This method interacts with the following components:
     * - Makes the bin check and ship correction indicators visible and managed.
     * - Hides and disables management of the tile held in hand.
     * It also sets up drag-and-drop functionality for tiles:
     * - Enables drag-and-drop listeners for each node in a grid (associated with a "ship").
     * - Allows tiles within the grid to be dragged to a bin check area for removal.
     *
     * Exceptions:
     * - If a RemoteException occurs when sending the action to the client, it wraps the exception
     *   in a RuntimeException.
     */
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

    /**
     * Handles the event triggered when the ship correction process is completed.
     *
     * This method sends a "Fixed" action to the client, indicating that the ship has been corrected.
     * It then updates the user interface by hiding and disabling relevant buttons and drag-and-drop
     * functionality.
     *
     * @throws RemoteException if there is a communication-related exception during the invocation of the remote method.
     */
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

    /**
     * Sends a crew action command to the server with the specified parameters.
     *
     * @param row the row coordinate where the action is to be performed
     * @param col the column coordinate where the action is to be performed
     * @param isAlien a flag indicating if the crew member is an alien (true) or not (false)
     * @param color the color associated with the possible alien (null if humans)
     */
    private void sendCrewAction(int row, int col, boolean isAlien, Color color) {
        try {
            client.sendAction(new SetCrew(row, col, isAlien, color));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Configures the UI and logic for adding a crew to the ship in the application.
     *
     * This method sets up the visibility and event handling for buttons that allow the user
     * to select and add a crew type (astronaut, purple alien, or brown alien) to specific locations
     * on the ship. The functionality is dynamically adjusted based on the current game level.
     */
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

    /**
     * Handles the event triggered when the "Finished" button is clicked.
     * It also disables and hides multiple UI buttons.
     * @throws RemoteException if there is an error during the remote method call.
     */
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

    /**
     * Handles the event triggered when the "Spy Others" button is clicked.
     *
     * @param event the ActionEvent triggered by the button click, carrying information
     *              about the source of the event (the clicked button)
     * @throws RemoteException if a remote communication error occurs during the
     *                         handling of the client request
     */
    @FXML
    public void onSpyOthersClicked(javafx.event.ActionEvent event) throws RemoteException {
        Button clickedButton = (Button) event.getSource();  // bottone che ha scatenato l’evento
        String username = clickedButton.getText();          // testo del bottone = username

        client.sendAction(new RequestShip(username));       // invia richiesta con username selezionato
    }


}
