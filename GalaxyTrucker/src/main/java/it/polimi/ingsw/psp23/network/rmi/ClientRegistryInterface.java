package it.polimi.ingsw.psp23.network.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;

public interface ClientRegistryInterface extends Remote {
    /** Registra un callback per lo username indicato */
    void registerClient(String username, ClientCallbackInterface callback) throws RemoteException;

    /** Rimuove il callback per lo username indicato */
    void unregisterClient(String username) throws RemoteException;

    /** Restituisce tutti i callback registrati */
    Collection<ClientCallbackInterface> getAllClients() throws RemoteException;

    void addPlayer(String username, String nameConnection) throws RemoteException;

    Collection<String> getAllPlayers() throws RemoteException;

    /** Restituisce il callback per un singolo username (o null) */
    ClientCallbackInterface getClient(String username) throws RemoteException;

    String getPlayerConnectionFromNickname(String nickname) throws RemoteException;

    String getPlayerNicknameFromConnection(String connection) throws RemoteException;
}
