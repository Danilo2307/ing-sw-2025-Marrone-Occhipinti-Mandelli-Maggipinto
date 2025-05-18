package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.network.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallbackInterface extends Remote {
        void onReceivedMessage(String msg) throws RemoteException;
        void onError(String message) throws RemoteException;
}
