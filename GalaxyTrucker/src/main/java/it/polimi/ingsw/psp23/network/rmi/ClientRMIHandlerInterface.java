package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.protocol.request.Action;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientRMIHandlerInterface extends Remote{
    void registerClient(String username, String nameConnection, ClientCallbackInterface stub) throws RemoteException;
    void heartbeat(String username) throws RemoteException;
    void sendToAllClients(Message msg, List<String> listaUsername) throws RemoteException;
    void sendToUser(String nameConnection, Message msg) throws RemoteException;

    void sendToNickname(String username, Message msg) throws RemoteException;

    void sendAction(String username, String nameConnection, Action action) throws RemoteException;
    void setGameLevel(int level) throws RemoteException;

    GameStatus getGameStatus(int gameId) throws RemoteException;

    int getNumPlayersConnected(int gameId) throws RemoteException;
    void setPlayerUsername(String username, int gameId) throws RemoteException;
    void setNumRequestedPlayers(int num, String username) throws RemoteException;
    int getNumRequestedPlayers(int gameId) throws RemoteException;
    void startBuildingPhase(int gameId) throws RemoteException;
    int getGameLevel(int gameId) throws RemoteException;
    int getGamesSize() throws RemoteException;
    List<List<Integer>> getGamesAvailables() throws RemoteException;
    void disconnectAll(List<String> players) throws RemoteException;
}
