package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.view.ClientEventHandler;
import it.polimi.ingsw.psp23.view.ViewAPI;

import javax.swing.text.View;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientRMI {
    private final ClientRMIHandlerInterface gameServer;
    private final ClientRegistryInterface clientRegistry;
    private final String username;

    /**
     * Costruisce e registra il client RMI.
     * @param host indirizzo del server RMI
     * @param port porta del registry (es. 1099)
     * @param username nickname del giocatore
     * @throws Exception in caso di errori RMI
     */
    public ClientRMI(String host, int port, String username, ClientEventHandler handler) throws Exception {
        this.username = username;

        Registry registry = LocateRegistry.getRegistry(host, port);

        this.clientRegistry = (ClientRegistryInterface) registry.lookup("ClientRegistry");
        this.gameServer = (ClientRMIHandler) registry.lookup("GameServer");

        // 3. Esporta il callback del client
        ClientCallback callback = new ClientCallback(handler);
        ClientCallbackInterface callbackStub = (ClientCallbackInterface) UnicastRemoteObject.exportObject(callback, 0);

        // 4. Registra il callback nel ClientRegistry
        clientRegistry.registerClient(username, callbackStub);
    }

}
