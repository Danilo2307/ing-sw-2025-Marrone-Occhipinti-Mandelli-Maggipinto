package it.polimi.ingsw.psp23.network.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface that allows the server to send asynchronous events to the client.
 * Implemented by the client and registered for server callbacks.
 */
public interface PushServiceOfClientRMI extends Remote {

    /**
     * Delivers an event to the client through a remote method call.
     * This method is invoked by the server when it needs to notify the client.
     * Note: the server thread will remain blocked until this call completes,
     * so the server should ensure it runs this in a separate thread to avoid
     * potential delays or deadlocks due to slow client handling.
     *
     * @param event the event or message to deliver to the client
     * @throws RemoteException if an RMI-related error occurs
     */
    void pushEvent(Object event) throws RemoteException;
}
