package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.exceptions.GameException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.request.HandleActionVisitor;
import it.polimi.ingsw.psp23.protocol.response.ErrorResponse;
import it.polimi.ingsw.psp23.protocol.response.SelectLevel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRMIHandler extends UnicastRemoteObject implements ClientRMIHandlerInterface {
    private final ClientRegistryInterface registry;

    public ClientRMIHandler(ClientRegistryInterface registry) throws RemoteException{
        super();
        this.registry = registry;
    }

    @Override
    public void registerClient(String username, String nameConnection, ClientCallbackInterface callback) throws RemoteException{

        List<String> usersConnected = UsersConnected.getInstance().getClients();

        synchronized (usersConnected) {
            UsersConnected.getInstance().addClient(nameConnection);
            System.out.println("Client connected: " + nameConnection);

            if(UsersConnected.getInstance().getClients().size() == 1){

                //socketHandler.sendMessage(new DirectMessage(new SelectLevel()));
                Message message = (new DirectMessage(new SelectLevel()));

                callback.onReceivedMessage(message.toString());

                System.out.println(message.toString());

                Game.getInstance(Integer.parseInt(message.toString()));

                System.out.println("arrivato a questo punto");

            }
        }

        registry.registerClient(username, callback);



    }

    @Override
    public void heartbeat(String username) throws RemoteException{}


    /**
     * Invia un messaggio a TUTTI i client RMI registrati.
     */
    @Override
    public void sendToAllClients(Message msg) throws RemoteException {
        for (ClientCallbackInterface cb : registry.getAllClients()) {
            try {
                cb.onReceivedMessage(msg.toString());
            } catch (RemoteException e) {
                // il client probabilmente è offline: deregistralo
                System.err.println("Client non risponde, rimuovo dallo stub list.");
            }
        }
    }

    /**
     * Invia un messaggio RMI a un singolo client identificato da username.
     * Se il client non è registrato o non risponde, viene loggato e rimosso.
     */
    @Override
    public void sendToUser(String username, Message msg) throws RemoteException {
        ClientCallbackInterface callback = registry.getClient(username);
        if (callback == null) {
            System.err.println("sendToUser: nessun client registrato con username \"" + username + "\"");
            return;
        }
        try {
            callback.onReceivedMessage(msg.toString());
        } catch (RemoteException e) {
            // Se il client non risponde, rimuovilo dalla lista
            System.err.println("sendToUser: impossibile inviare a \"" + username + "\": " + e.getMessage());
        }
    }

    @Override
    public void sendAction(String username, Action action) throws RemoteException{
        try {
            action.call(new HandleActionVisitor(), username);
        }
        /// TODO: raccolgo eccezioni lanciate dalla call
        // Catch all game-related exceptions triggered by invalid player actions.
        // These are not recoverable errors but rule violations (e.g., wrong component state or illegal move).
        // The server sends an error message back to the client to notify them, without stopping the game flow.
        catch(GameException e) {
            DirectMessage dm = new DirectMessage(new ErrorResponse(e.getMessage()));
            sendToUser(username, dm);
        }
    }

}
