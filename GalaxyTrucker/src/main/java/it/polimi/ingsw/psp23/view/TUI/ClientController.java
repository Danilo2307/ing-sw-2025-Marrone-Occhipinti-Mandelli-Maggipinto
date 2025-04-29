package it.polimi.ingsw.psp23.view.TUI;

import java.io.ObjectOutputStream;

public class ClientController {
    private final boolean useRMI;

    private ObjectOutputStream out; // se socket
    private ServerRemoteInterface serverStub; // se RMI

    public ClientController(boolean useRMI, ObjectOutputStream out, ServerRemoteInterface stub) {
        this.useRMI = useRMI;
        this.out = out;
        this.serverStub = stub;
    }

    /* public void sendAction(Event e) {
        try {
            if (useRMI) {
                serverStub.receiveEvent(e); // metodo remoto RMI
            } else {
                EventMessage eventMessage = new EventMessage(e);
                out.writeObject(e);         // socket
                out.flush();
            }
        } catch (IOException ex) {
            System.err.println("Errore nell'invio dell'evento: " + ex.getMessage());
        }
    } */
}

