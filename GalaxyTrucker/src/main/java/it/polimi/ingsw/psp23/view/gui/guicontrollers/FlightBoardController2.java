package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.protocol.request.TakeVisibleDeck;
import it.polimi.ingsw.psp23.view.gui.GuiApplication;
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
        try {
            client.sendAction(new TakeVisibleDeck(2));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onThirdDeckClicked(){
        try {
            client.sendAction(new TakeVisibleDeck(3));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onFourthDeckClicked(){
        try {
            client.sendAction(new TakeVisibleDeck(4));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onBackToShipClicked(){
        GuiApplication.getInstance().toBuildingPhase(null);
    }


}
