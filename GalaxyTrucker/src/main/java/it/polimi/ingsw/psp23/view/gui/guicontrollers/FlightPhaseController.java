package it.polimi.ingsw.psp23.view.gui.guicontrollers;
import it.polimi.ingsw.psp23.protocol.request.DrawCard;
import it.polimi.ingsw.psp23.protocol.request.Land;
import javafx.scene.control.Button;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.socket.ClientSocket;
import it.polimi.ingsw.psp23.protocol.request.LeaveFlight;
import it.polimi.ingsw.psp23.protocol.request.RequestFlightBoard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public void planetsCommands(int id) {
        button1.setText("Atterra Pianeta 1");
        button2.setText("Atterra Pianeta 2");
        button3.setText("Atterra Pianeta 3");
        button4.setText("Atterra Pianeta 4");
        button5.setText("Atterra Pianeta 5");

        switch (id) {
            // 4 pianeti
            case 112, 206 -> {
                button1.setManaged(true);
                button1.setVisible(true);
                button1.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(0));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                button2.setManaged(true);
                button2.setVisible(true);
                button2.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(1));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                button3.setManaged(true);
                button3.setVisible(true);
                button3.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(2));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                button4.setManaged(true);
                button4.setVisible(true);
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
                button1.setManaged(true);
                button1.setVisible(true);
                button1.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(0));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                button2.setManaged(true);
                button2.setVisible(true);
                button2.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(1));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                button3.setManaged(true);
                button3.setVisible(true);
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
                button1.setManaged(true);
                button1.setVisible(true);
                button1.setOnAction(e -> {
                    try {
                        client.sendAction(new Land(0));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                button2.setManaged(true);
                button2.setVisible(true);
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
