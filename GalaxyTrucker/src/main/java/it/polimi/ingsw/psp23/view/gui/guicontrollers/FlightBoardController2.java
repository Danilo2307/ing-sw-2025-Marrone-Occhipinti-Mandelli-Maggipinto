package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.protocol.request.ReleaseDeck;
import it.polimi.ingsw.psp23.protocol.request.RequestFlightBoard;
import it.polimi.ingsw.psp23.protocol.request.TakeVisibleDeck;
import it.polimi.ingsw.psp23.view.gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.image.ImageView;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import javafx.stage.Stage;

public class FlightBoardController2 {
    @FXML private Button firstDeck;
    @FXML private Button secondDeck;
    @FXML private Button thirdDeck;
    @FXML private Button fourthDeck;
    @FXML private Button backToShip;
    @FXML private javafx.scene.image.ImageView position0;
    @FXML private javafx.scene.image.ImageView position1;
    @FXML private javafx.scene.image.ImageView position2;
    @FXML private javafx.scene.image.ImageView position3;
    @FXML private javafx.scene.image.ImageView position4;
    @FXML private javafx.scene.image.ImageView position5;
    @FXML private javafx.scene.image.ImageView position6;
    @FXML private javafx.scene.image.ImageView position7;
    @FXML private javafx.scene.image.ImageView position8;
    @FXML private javafx.scene.image.ImageView position9;
    @FXML private javafx.scene.image.ImageView position10;
    @FXML private javafx.scene.image.ImageView position11;
    @FXML private javafx.scene.image.ImageView position12;
    @FXML private javafx.scene.image.ImageView position13;
    @FXML private javafx.scene.image.ImageView position14;
    @FXML private javafx.scene.image.ImageView position15;
    @FXML private javafx.scene.image.ImageView position16;
    @FXML private javafx.scene.image.ImageView position17;
    @FXML private javafx.scene.image.ImageView position18;
    @FXML private javafx.scene.image.ImageView position19;
    @FXML private javafx.scene.image.ImageView position20;
    @FXML private javafx.scene.image.ImageView position21;
    @FXML private javafx.scene.image.ImageView position22;
    @FXML private javafx.scene.image.ImageView position23;
    @FXML private ImageView card1;
    @FXML private ImageView card2;
    @FXML private ImageView card3;
    private Client client;
    private int deck = 0;
    private javafx.scene.image.ImageView[] positions;

    public void initialize() {
        positions = new javafx.scene.image.ImageView[]{
                position0, position1, position2, position3, position4, position5,
                position6, position7, position8, position9, position10, position11,
                position12, position13, position14, position15, position16, position17,
                position18, position19, position20, position21, position22, position23
        };
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    public void onFirstDeckClicked(){
        deck = 1;
        try {
            client.sendAction(new TakeVisibleDeck(1));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onSecondDeckClicked(){
        deck = 2;
        try {
            client.sendAction(new TakeVisibleDeck(2));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onThirdDeckClicked(){
        deck = 3;
        try {
            client.sendAction(new TakeVisibleDeck(3));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void onBackToShipClicked(){
        GuiApplication.getInstance().toBuildingPhase(null);
    }

    @FXML
    public void onBackToFlightBoardClicked(){
        try {
            switch (deck) {
                case 1 -> client.sendAction(new ReleaseDeck(1));
                case 2 -> client.sendAction(new ReleaseDeck(2));
                case 3 -> client.sendAction(new ReleaseDeck(3));
                default -> throw new RuntimeException("Deck non salvato");
            }
            deck = 0;
            client.sendAction(new RequestFlightBoard());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setColors(Map<Color,Integer> colors){

       /* for(int i =0; i<positions.length; i++){
            int finalI = i;
            Platform.runLater(()->{
                positions[finalI].setStyle(null);
            });

        }*/

        for(Map.Entry<Color,Integer> entry : colors.entrySet()){
            Platform.runLater(()->{
                Color color = entry.getKey();
                switch (color){
                    case Red ->  positions[entry.getValue()].setStyle("-fx-background-color: red;");
                    case Blue ->  positions[entry.getValue()].setStyle("-fx-background-color: blue;");
                    case Yellow -> positions[entry.getValue()].setStyle("-fx-background-color: yellow;");
                    case Green -> positions[entry.getValue()].setStyle("-fx-background-color: green;");
                }
            });

        }
    }

    public ImageView getCard1() {
        return card1;
    }
    public ImageView getCard2() {
        return card2;
    }
    public ImageView getCard3() {
        return card3;
    }


}
