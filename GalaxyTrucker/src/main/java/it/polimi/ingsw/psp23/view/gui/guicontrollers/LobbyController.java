package it.polimi.ingsw.psp23.view.gui.guicontrollers;
import it.polimi.ingsw.psp23.network.socket.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

public class LobbyController {
    @FXML
    private Button button;

    @FXML
    private TextField usernameField;

    private Client client;


    @FXML
    public void onButtonClicked(javafx.event.ActionEvent actionEvent) {
        client.setUsername(usernameField.getText());
    }

    public void setClient(Client client) {
        this.client = client;
    }
}

