package it.polimi.ingsw.psp23.network.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRegistry extends UnicastRemoteObject implements ClientRegistryInterface{
    /**
     * Implementazione del registry che mantiene mappa username → stub callback.
     */
    private final Map<String,ClientCallbackInterface> clients = new ConcurrentHashMap<>();

        public ClientRegistry() throws RemoteException {
            super();
        }

        @Override
        public void registerClient(String username, ClientCallbackInterface callback) throws RemoteException {
            clients.put(username, callback);
            System.out.println("ClientRegistry: registrato " + username);
        }

        @Override
        public void unregisterClient(String username) throws RemoteException {
            clients.remove(username);
            System.out.println("ClientRegistry: deregistrato " + username);
        }

        @Override
        public Collection<ClientCallbackInterface> getAllClients() throws RemoteException {
            return clients.values();
        }

        @Override
        public ClientCallbackInterface getClient(String username) throws RemoteException {
            return clients.get(username);
        }
}
