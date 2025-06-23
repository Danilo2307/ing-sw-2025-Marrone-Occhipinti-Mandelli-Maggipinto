package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.protocol.request.RequestShip;
import it.polimi.ingsw.psp23.view.gui.GuiApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class OpponentShipController {
    private Client client;

    @FXML private Button player1;
    @FXML private Button player2;
    @FXML private Button player3;

    public void initialize() {
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

    public void setClient(Client client) {
        this.client = client;
    }

    private void enable(Button button) {
        button.setVisible(true);
        button.setManaged(true);
    }

    private void disable(Button button) {
        button.setVisible(false);
        button.setManaged(false);
    }

    @FXML
    public void viewShip(javafx.event.ActionEvent event) throws RemoteException {
        Button clickedButton = (Button) event.getSource();  // bottone che ha scatenato l’evento
        String username = clickedButton.getText();          // testo del bottone = username

        client.sendAction(new RequestShip(username));       // invia richiesta con username selezionato
    }


}

