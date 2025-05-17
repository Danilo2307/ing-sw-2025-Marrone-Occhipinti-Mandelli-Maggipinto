package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.network.socket.Client;
import it.polimi.ingsw.psp23.protocol.request.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class BuildingPhaseController {
    private Client client;

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

    public void setClient(Client client) {
        this.client = client;
    }

    public void initialize() {
        board.fitWidthProperty().bind(boardStack.widthProperty());
        board.fitHeightProperty().bind(boardStack.heightProperty());

        ship.prefWidthProperty().bind(boardStack.widthProperty());
        ship.prefHeightProperty().bind(boardStack.heightProperty());
    }

    @FXML
    public void onDrawHeapClicked() {
        client.sendAction(new DrawFromHeap());
    }

    @FXML
    public void onReleaseClicked() {
        client.sendAction(new ReleaseTile());
    }

    @FXML
    public void onRotateClicked() {
        client.sendAction(new RotateTile());
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
    }


}
