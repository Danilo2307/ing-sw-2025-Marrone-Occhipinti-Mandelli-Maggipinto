package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.protocol.request.ReleaseDeck;
import it.polimi.ingsw.psp23.protocol.request.RequestFlightBoard;
import it.polimi.ingsw.psp23.protocol.request.TakeVisibleDeck;
import it.polimi.ingsw.psp23.view.gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import it.polimi.ingsw.psp23.model.enumeration.Color;
import javafx.stage.Stage;

public class FlightBoardController2 {
    @FXML private Button firstDeck;
    @FXML private Button secondDeck;
    @FXML private Button thirdDeck;
    @FXML private Button fourthDeck;
    @FXML private Button backToShip;
    @FXML private ImageView position0;
    @FXML private ImageView position1;
    @FXML private ImageView position2;
    @FXML private ImageView position3;
    @FXML private ImageView position4;
    @FXML private ImageView position5;
    @FXML private ImageView position6;
    @FXML private ImageView position7;
    @FXML private ImageView position8;
    @FXML private ImageView position9;
    @FXML private ImageView position10;
    @FXML private ImageView position11;
    @FXML private ImageView position12;
    @FXML private ImageView position13;
    @FXML private ImageView position14;
    @FXML private ImageView position15;
    @FXML private ImageView position16;
    @FXML private ImageView position17;
    @FXML private ImageView position18;
    @FXML private ImageView position19;
    @FXML private ImageView position20;
    @FXML private ImageView position21;
    @FXML private ImageView position22;
    @FXML private ImageView position23;
    private Client client;
    private int deck = 0;
    private ImageView[] positions;

    public void inizializzaPosizioni() {
        positions = new ImageView[]{
                position0, position1, position2, position3, position4, position5,
                position6, position7, position8, position9, position10, position11,
                position12, position13, position14, position15, position16, position17,
                position18, position19, position20, position21, position22, position23
        };
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getDeckNumber(){
        return this.deck;
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


    public void setColors(Map<Color,Integer> colors){

       for(int i =0; i<positions.length; i++){
            int finalI = i;
            Platform.runLater(()->{
                positions[finalI].setImage(null);
            });

        }

        for(Map.Entry<Color,Integer> entry : colors.entrySet()){
            Platform.runLater(()->{
                Color color = entry.getKey();
                System.out.println("Qui ci arrivo con almeno un colore " + entry.getValue() + " " + entry.getKey());
                String imagePath = "/it/polimi/ingsw/psp23/images/cards/" + 101 + ".jpg";
                javafx.scene.image.Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                switch (color){
                    case Red ->  positions[entry.getValue()].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/utility/red.jpg"))));
                    case Blue ->  positions[entry.getValue()].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/utility/blue.jpg"))));
                    case Yellow -> positions[entry.getValue()].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/utility/yellow.jpg"))));
                    case Green -> positions[entry.getValue()].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/utility/green.jpg"))));
                }
            });

        }
    }


}
