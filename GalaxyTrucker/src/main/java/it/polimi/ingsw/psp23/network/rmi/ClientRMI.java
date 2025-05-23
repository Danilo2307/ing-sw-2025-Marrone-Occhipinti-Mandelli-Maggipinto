package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.exceptions.LobbyUnavailableException;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.response.LobbyUnavailable;
import it.polimi.ingsw.psp23.view.ClientEventHandler;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class ClientRMI extends Client {
    private final ClientRMIHandlerInterface gameServer;
    private final ClientRegistryInterface clientRegistry;
    private String username;
    private final String nameConnection;
    private final Registry registry;

    /**
     * Costruisce e registra il client RMI.
     * @param host indirizzo del server RMI
     * @param port porta del registry (es. 1099)
     * @param username nickname del giocatore
     * @throws Exception in caso di errori RMI
     */
    public ClientRMI(String host, int port, String username, ClientEventHandler handler) throws Exception {
        this.username = username;

        registry = LocateRegistry.getRegistry(host, port);

        this.clientRegistry = (ClientRegistryInterface) registry.lookup("ClientRegistry");
        this.gameServer = (ClientRMIHandlerInterface) registry.lookup("GameServer");

        // 3. Esporta il callback del client
        ClientCallback callback = new ClientCallback(handler);
        // ClientCallbackInterface callbackStub = (ClientCallbackInterface) UnicastRemoteObject.exportObject(callback, 0);
        ClientCallbackInterface callbackStub = callback;

        // 4. Registra il callback nel ClientRegistry
        this.nameConnection = UUID.randomUUID().toString();
        try {
            gameServer.registerClient(username, nameConnection, callbackStub);
        }
        catch (LobbyUnavailableException e) {
            gameServer.sendToUser(nameConnection, new DirectMessage(new LobbyUnavailable()));
        }

    }

    public ClientRMIHandlerInterface getGameServer() {
        return gameServer;
    }

    public void close() throws RemoteException, NotBoundException {
        registry.unbind("GameServer");
        try {
            Server.getInstance().close();
        } catch (RuntimeException e) {

        }
    }

    @Override
    public void sendAction(Action action) throws RemoteException {
        gameServer.sendAction(username, nameConnection, action);
    }

    @Override
    public boolean isRmi() throws RemoteException {
        return true;
    }

    @Override
    public void open() throws RemoteException {
        registry.rebind("GameServer", gameServer);
//        Server.getInstance().setServerSocket("localhost", 8000);
    }
    @Override
    public void setUsername(String username) throws RemoteException {
        this.username = username;
    }

}
