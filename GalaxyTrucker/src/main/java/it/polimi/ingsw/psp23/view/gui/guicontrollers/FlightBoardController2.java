package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.protocol.request.TakeVisibleDeck;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.awt.*;
import java.rmi.RemoteException;

public class FlightBoardController2 {
    @FXML private Button firstDeck;
    @FXML private Button secondDeck;
    @FXML private Button thirdDeck;
    @FXML private Button fourthDeck;
    @FXML private Button backToShip;
    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    public void onFirstDeckClicked(){
        try {
            client.sendAction(new TakeVisibleDeck(1));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onSecondDeckClicked(){

    }

    @FXML
    public void onThirdDeckClicked(){

    }

    @FXML
    public void onFourthDeckClicked(){

    }


}
