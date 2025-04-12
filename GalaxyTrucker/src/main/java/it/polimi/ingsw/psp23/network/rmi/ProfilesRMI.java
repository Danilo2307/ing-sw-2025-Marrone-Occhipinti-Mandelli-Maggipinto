package it.polimi.ingsw.psp23.network.rmi;

import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface that allows RMI clients to access methods of the Profiles class.
 */
public interface ProfilesRMI extends Remote {

    /**
     * Remotely creates a new user via RMI communication.
     *
     * @throws RemoteException if a remote communication error occurs
     * @throws MalformedURLException if there is a problem with the RMI registry URL
     */
    void createUserRMI() throws RemoteException, MalformedURLException;
}
