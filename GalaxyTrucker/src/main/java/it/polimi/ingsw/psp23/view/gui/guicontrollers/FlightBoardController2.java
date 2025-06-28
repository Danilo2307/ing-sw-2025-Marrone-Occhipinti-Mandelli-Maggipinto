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


/**
 * The FlightBoardController2 class is responsible for managing the flight board view and its interactions in the UI.
 * It handles user input via various buttons and updates the displayed elements according to the current game state.
 * The class is associated with various ImageView components representing positions on the board,
 * and buttons for interacting with different deck options or returning to the ship.
 */
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

    public void initialize() {
        positions = new ImageView[]{
                position0, position1, position2, position3, position4, position5,
                position6, position7, position8, position9, position10, position11,
                position12, position13, position14, position15, position16, position17,
                position18, position19, position20, position21, position22, position23
        };
        firstDeck.setOnAction(actionEvent -> onFirstDeckClicked());
        secondDeck.setOnAction(actionEvent -> onSecondDeckClicked());
        thirdDeck.setOnAction(actionEvent -> onThirdDeckClicked());
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getDeckNumber(){
        return this.deck;
    }

    /**
     * This method updates the current deck selection to the first deck
     * and sends an action to the server to process the selection
     * of the first visible deck. If a RemoteException occurs during the
     * action dispatch, it throws a RuntimeException to handle the error.
     *
     * The method specifically sends a {@link TakeVisibleDeck} action
     * with an index of 1, which corresponds to the first deck.
     *
     * Throws:
     * - RuntimeException: If a RemoteException occurs while sending the action to the client.
     */
    @FXML
    public void onFirstDeckClicked(){
        deck = 1;
        try {
            client.sendAction(new TakeVisibleDeck(1));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is triggered when the second deck is clicked.
     * It sets the current deck to the second deck and sends an action to the server to process this selection.
     * If a remote communication issue occurs while sending the action, a runtime exception is thrown.
     *
     * The action sent is encapsulated in a TakeVisibleDeck object, specifying the second deck index.
     *
     * Exception:
     * - Throws RuntimeException if a RemoteException occurs during the action transmission.
     */
    @FXML
    public void onSecondDeckClicked(){
        deck = 2;
        try {
            client.sendAction(new TakeVisibleDeck(2));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the action when the third deck is clicked by the user on the flight board interface.
     *
     * This method sets the currently selected deck to the third deck. It then attempts to send a
     * corresponding action, encapsulated in a {@link TakeVisibleDeck} object, to the server via the
     * {@code client.sendAction} method.
     *
     * If a {@link RemoteException} occurs during the action transmission, it wraps and rethrows it
     * as a {@link RuntimeException}.
     *
     * The third deck is identified by its index value of 3.
     */
    @FXML
    public void onThirdDeckClicked(){
        deck = 3;
        try {
            client.sendAction(new TakeVisibleDeck(3));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Handles the event when the "Back to Ship" option is clicked in the user interface.
     * It invokes the {@code backToShip} method to perform the corresponding navigation or state change.
     */
    @FXML
    public void onBackToShipClicked(){
        GuiApplication.getInstance().backToShip();
    }

    /**
     * Disables the click events for the deck buttons in the flight board interface.
     */
    public void disableDeckClick(){
        firstDeck.setOnAction(event -> {});
        secondDeck.setOnAction(event -> {});
        thirdDeck.setOnAction(event -> {} );
    }


    /**
     * Updates the images displayed on specific positions of the flight board
     * based on the given map of colors and their associated positions.
     * Each color is mapped to a UI representation, which is displayed
     * at the specified position on the board.
     *
     * @param colors a map where the key is a {@code Color}, representing the color to display,
     *               and the value is an {@code Integer} indicating the position on the board
     *               where the color image should be set
     */
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
                String imagePath = "/it/polimi/ingsw/psp23/images/cards/" + 101 + ".jpg";
                javafx.scene.image.Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                int newPosition = entry.getValue();
                if (newPosition < 0) {
                    newPosition = 24 + newPosition;
                }
                switch (color){
                    case Red ->  positions[newPosition % 24].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/utility/red.jpg"))));
                    case Blue ->  positions[newPosition % 24].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/utility/blue.jpg"))));
                    case Yellow -> positions[newPosition % 24].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/utility/yellow.jpg"))));
                    case Green -> positions[newPosition % 24].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/utility/green.jpg"))));
                }
            });
        }

    }


}
