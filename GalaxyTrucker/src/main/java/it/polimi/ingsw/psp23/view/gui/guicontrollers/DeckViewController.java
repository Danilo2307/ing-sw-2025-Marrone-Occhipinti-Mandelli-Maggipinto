package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.protocol.request.ReleaseDeck;
import it.polimi.ingsw.psp23.protocol.request.RequestFlightBoard;
import it.polimi.ingsw.psp23.view.gui.GuiApplication;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.rmi.RemoteException;

public class DeckViewController {
    @FXML private ImageView card1;
    @FXML private ImageView card2;
    @FXML private ImageView card3;
    private int deck = 0;





    @FXML
    public void onBackToFlightBoardClicked(){
        int deck = GuiApplication.getInstance().getDeckNumber();
        Client client = GuiApplication.getInstance().getClient();
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
