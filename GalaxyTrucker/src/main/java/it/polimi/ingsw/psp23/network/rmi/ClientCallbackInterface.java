package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.network.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Defines a remote interface for client-side callbacks in a distributed system using RMI.
 * The methods in this interface enable the server to communicate asynchronously with the client,
 * providing functionalities such as message delivery, error reporting, disconnection handling,
 * and heartbeat signaling.
 *
 * The implementing class must handle each of these methods appropriately for the client-side logic.
 *
 * Methods:
 * - onReceivedMessage: Called by the server to deliver a message to the client.
 * - onError: Called by the server to notify the client of an error, providing an error description.
 * - disconnectClient: Allows the server to instruct the client to disconnect.
 * - sendHeartbeat: Used to maintain a connection between the client and the server, verifying the client's active status.
 *
 * This interface extends {@link Remote}, making all methods accessible remotely via RMI.
 * All methods throw {@link RemoteException} to handle communication issues across the network.
 */
public interface ClientCallbackInterface extends Remote {
        void onReceivedMessage(Message message) throws RemoteException;
        void onError(String message) throws RemoteException;
        void disconnectClient() throws RemoteException;
        void sendHeartbeat() throws RemoteException;
}
