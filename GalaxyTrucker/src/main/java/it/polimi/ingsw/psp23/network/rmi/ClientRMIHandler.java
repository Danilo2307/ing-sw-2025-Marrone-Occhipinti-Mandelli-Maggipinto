package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.exceptions.GameException;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.request.HandleActionVisitor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRMIHandler extends UnicastRemoteObject implements ClientRMIHandlerInterface {
    private final Map<String, ClientCallbackInterface> clients = new ConcurrentHashMap<>();

    protected ClientRMIHandler() throws RemoteException{
        super();
    }

    @Override
    public void registerClient(String username, ClientCallbackInterface callback) throws RemoteException{
        clients.put(username, callback);
    }

    @Override
    public void heartbeat(String username) throws RemoteException{}


    /**
     * Invia un messaggio a TUTTI i client RMI registrati.
     */
    @Override
    public void sendToAllClients(Message msg) throws RemoteException {
        for (Iterator<Map.Entry<String, ClientCallbackInterface>> it = clients.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, ClientCallbackInterface> entry = it.next();
            String user = entry.getKey();
            ClientCallbackInterface cb = entry.getValue();
            try {
                cb.onReceivedMessage(msg.toString());
            } catch (RemoteException e) {
                // il client probabilmente è offline: deregistralo
                System.err.println("Client " + user + " non risponde, rimuovo dallo stub list.");
                it.remove();
            }
        }
    }

    /**
     * Invia un messaggio RMI a un singolo client identificato da username.
     * Se il client non è registrato o non risponde, viene loggato e rimosso.
     */
    @Override
    public void sendToUser(String username, Message msg) throws RemoteException {
        ClientCallbackInterface callback = clients.get(username);
        if (callback == null) {
            System.err.println("sendToUser: nessun client registrato con username \"" + username + "\"");
            return;
        }
        try {
            callback.onReceivedMessage(msg.toString());
        } catch (RemoteException e) {
            // Se il client non risponde, rimuovilo dalla lista
            clients.remove(username);
            System.err.println("sendToUser: impossibile inviare a \"" + username + "\": " + e.getMessage());
        }
    }

}
