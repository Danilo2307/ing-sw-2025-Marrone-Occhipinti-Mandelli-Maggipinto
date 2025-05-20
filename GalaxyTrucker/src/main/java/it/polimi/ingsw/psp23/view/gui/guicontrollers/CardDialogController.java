package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.network.socket.ClientSocket;

public class CardDialogController {
    private ClientSocket client;

    public void setClient(ClientSocket client) {
        this.client = client;
    }
}
