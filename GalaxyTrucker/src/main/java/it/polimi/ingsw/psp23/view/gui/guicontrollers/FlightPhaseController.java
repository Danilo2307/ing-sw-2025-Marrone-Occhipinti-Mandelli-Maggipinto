package it.polimi.ingsw.psp23.view.gui.guicontrollers;
import it.polimi.ingsw.psp23.protocol.request.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.socket.ClientSocket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.rmi.RemoteException;
import java.util.Objects;

public class FlightPhaseController {
    private Client client;
    @FXML Button button1;
    @FXML Button button2;
    @FXML Button button3;
    @FXML Button button4;
    @FXML Button button5;
    @FXML Button button6;
    @FXML ImageView card;
    @FXML Label textLabel;
    @FXML StackPane ship;
    @FXML GridPane shipGrid;

    private TwoStepsTileSelector tileSelector = null;


    public void setClient(Client client) {
        this.client = client;
    }

    public Button getButton1() {
        return button1;
    }
    public Button getButton2() {
        return button2;
    }
    public Button getButton3() {
        return button3;
    }
    public Button getButton4() {
        return button4;
    }
    public Button getButton5() {
        return button5;
    }
    public Button getButton6() {
        return button6;
    }
    public Label getTextLabel(){
        return textLabel;
    }

    public void setShip(StackPane ship){
        this.ship.getChildren().clear();
        this.ship.getChildren().add(ship);
    }

    public void installClickHandlers() {
        for (Node tile: shipGrid.getChildren()) {
            tile.setOnMouseClicked(event -> {
                // prendo le coordinate del click
                Integer r = GridPane.getRowIndex(tile);
                Integer c = GridPane.getColumnIndex(tile);
                // se null allora è 0
                int row = r == null ? 0 : r;
                int col = c == null ? 0 : c;

                // se non è stata selezionata alcuna modalità --> info
                if (tileSelector == null) {
                    try {
                        client.sendAction(new RequestTileInfo(row, col));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    tileSelector.handleClick(row, col);
                }
            });
        }
    }

    public void setCardImage(int id){
        String imagePath = "/it/polimi/ingsw/psp23/images/cards/" + id + ".jpg";
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        card.setImage(image);
    }

    @FXML
    public void onLeaveClicked() throws RemoteException {
        client.sendAction(new LeaveFlight());
    }

    @FXML
    public void onFlightBoardClicked(){
        try {
            client.sendAction(new RequestFlightBoard());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void drawCard(){
        try {
            client.sendAction(new DrawCard());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void enable(Button button){
        button.setVisible(true);
        button.setManaged(true);
    }

    public void disable(Button button){
        button.setVisible(false);
        button.setManaged(false);
    }

    public void disableAllButtons() {
        disable(button1);
        disable(button2);
        disable(button3);
        disable(button4);
        disable(button5);
        disable(button6);
    }

    public void openSpaceCommands() {
        Platform.runLater(() -> {
            button1.setText("Attiva motore");
            button2.setText("Pronto");
            enable(button1);
            enable(button2);

            button1.setOnAction(event -> {
                // creo il selector
                tileSelector = new TwoStepsTileSelector( (engine, battery) -> {
                    // callback dopo i 2 click
                    int ex = engine.x();
                    int ey = engine.y();
                    int bx = battery.x();
                    int by = battery.y();
                    try {
                        // invio azione e resetto il selettore
                        client.sendAction(new ActivateEngine(ex, ey, bx, by));
                        tileSelector = null;
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
            });

            button2.setOnAction(e -> {
                try {
                    client.sendAction(new Ready());
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            });

        });
    }

    public void planetsCommands(int id) {
        button1.setText("Atterra Pianeta 1");
        button2.setText("Atterra Pianeta 2");
        button3.setText("Atterra Pianeta 3");
        button4.setText("Atterra Pianeta 4");
        button5.setText("Atterra Pianeta 5");

        switch (id) {
            // 4 pianeti
            case 112, 206 -> {
                enable(button1);
                button1.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(0));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                enable(button2);
                button2.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(1));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                enable(button3);
                button3.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(2));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                enable(button4);
                button4.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(3));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }

            // 3 pianeti
            case 102, 114, 204, 207 -> {
                enable(button1);
                button1.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(0));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                enable(button2);
                button2.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(1));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                enable(button3);
                button3.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(2));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }

            // 2 pianeti
            case 113, 205 -> {
                enable(button1);
                button1.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(0));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                enable(button2);
                button2.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(1));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        }
    }
}
