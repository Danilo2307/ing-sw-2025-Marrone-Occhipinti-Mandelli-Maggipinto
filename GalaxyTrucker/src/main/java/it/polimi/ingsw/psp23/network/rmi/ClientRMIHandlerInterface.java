package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.protocol.request.Action;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRMIHandlerInterface extends Remote{
    void registerClient(String username, ClientCallbackInterface stub) throws RemoteException;
    void sendAction(Action action, String username) throws RemoteException;
    void heartbeat(String username) throws RemoteException;
}
