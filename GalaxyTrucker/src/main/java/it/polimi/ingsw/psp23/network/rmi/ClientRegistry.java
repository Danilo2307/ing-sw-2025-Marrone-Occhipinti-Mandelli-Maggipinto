package it.polimi.ingsw.psp23.network.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;

public class ClientRegistry extends UnicastRemoteObject implements ClientRegistryInterface{
    /**
     * Implementazione del registry che mantiene mappa username → stub callback.
     */
    private final Map<String,ClientCallbackInterface> clients = new ConcurrentHashMap<>();
    private final Map<String,String> players = new ConcurrentHashMap<>();

        public ClientRegistry() throws RemoteException {
            super();
        }

        @Override
        public void registerClient(String nameConnection, ClientCallbackInterface callback) throws RemoteException {
            clients.put(nameConnection, callback);
            System.out.println("ClientRegistry: registrato " + nameConnection);
        }

        @Override
        public void unregisterClient(String nameConnection) throws RemoteException {
            clients.remove(nameConnection);
            System.out.println("ClientRegistry: deregistrato " + nameConnection);
        }

        @Override
        public Collection<ClientCallbackInterface> getAllClients() throws RemoteException {
            return clients.values();
        }

        @Override
        public void addPlayer(String username, String nameConnection) throws RemoteException{
            players.put(nameConnection, username);
        }

        @Override
        public Collection<String> getAllPlayers() throws RemoteException{
            return players.values();
        }

        @Override
        public ClientCallbackInterface getClient(String nameConnection) throws RemoteException {
            return clients.get(nameConnection);
        }

        @Override
        public String getPlayerConnectionFromNickname(String nickname) throws RemoteException{
            return  players.entrySet()
                    .stream()
                    .filter(e -> Objects.equals(e.getValue(), nickname))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public String getPlayerNicknameFromConnection(String connection) throws RemoteException{
            return  players.get(connection);
        }
}
