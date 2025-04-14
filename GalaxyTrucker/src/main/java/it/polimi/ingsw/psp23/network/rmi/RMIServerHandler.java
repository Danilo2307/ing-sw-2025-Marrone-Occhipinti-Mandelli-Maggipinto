package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.network.*;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Utility class for managing the RMI registry and object lifecycle,
 * including creation, exporting, and cleanup of remote objects.
 */
public class RMIServerHandler {

    private static final int DEFAULT_PORT = 1099;
    private static boolean isRegistryInitialized = false;
    private static int activePort = -1;

    /**
     * Initializes an RMI registry on the given port.
     *
     * @param port port number to bind the registry
     * @throws RemoteException if the registry already exists or cannot be created
     */
    public static void createRegistry(int port) throws RemoteException {
        if (!isRegistryInitialized) {
            try {
                String hostAddress = InetAddress.getLocalHost().getHostAddress();
                System.setProperty("java.rmi.server.hostname", hostAddress);
            } catch (UnknownHostException e) {
                throw new ServerCriticalError("Unable to determine host IP. Aborting server startup.");
            }

            LocateRegistry.createRegistry(port);
            activePort = port;
            isRegistryInitialized = true;
        } else {
            throw new RemoteException("An RMI registry is already active.");
        }
    }

    /**
     * Initializes the RMI registry on the default port (1099).
     *
     * @throws RemoteException if a registry already exists or fails to start
     */
    public static void createRegistry() throws RemoteException {
        createRegistry(DEFAULT_PORT);
    }

    /**
     * Shuts down the current registry instance.
     *
     * @throws RemoteException if no registry is active or shutdown fails
     */
    public static void destroyRegistry() throws RemoteException {
        if (isRegistryInitialized) {
            Registry registry = LocateRegistry.getRegistry(activePort);
            UnicastRemoteObject.unexportObject(registry, true);
            activePort = -1;
            isRegistryInitialized = false;
        } else {
            throw new RemoteException("No active registry to destroy.");
        }
    }

    /**
     * Makes a remote object accessible via RMI by binding it to the registry.
     *
     * @param obj  the remote object to expose
     * @param name name under which the object is bound
     * @throws RemoteException if no registry is active or if export fails
     * @throws MalformedURLException if the RMI URL is invalid
     */
    public static void exportObject(Remote obj, String name) throws RemoteException, MalformedURLException {
        if (!isRegistryInitialized) {
            throw new RemoteException("Registry must be created before exporting objects.");
        }
        UnicastRemoteObject.exportObject(obj, 0);
        Naming.rebind("rmi://localhost:" + activePort + "/" + name, obj);
    }

    /**
     * Unregisters a remote object from the RMI registry.
     *
     * @param name  identifier used in the registry
     * @param force true to force unbinding even if calls are in progress
     * @return true if the object was successfully unbound
     * @throws RemoteException if there is no active registry or unbinding fails
     * @throws NotBoundException if the object name is not found in the registry
     */
    public static boolean unexportObject(String name, boolean force) throws RemoteException, NotBoundException {
        if (isRegistryInitialized) {
            Remote stub = getStub(name);
            return UnicastRemoteObject.unexportObject(stub, force);
        } else {
            throw new RemoteException("No registry is currently running.");
        }
    }

    /**
     * Retrieves a stub for the given object name from the registry.
     *
     * @param name the identifier used to bind the object
     * @return the corresponding stub, to be cast to the appropriate interface
     * @throws RemoteException if no registry is available or lookup fails
     * @throws NotBoundException if the object is not currently bound
     */
    public static Remote getStub(String name) throws RemoteException, NotBoundException {
        if (isRegistryInitialized) {
            Registry registry = LocateRegistry.getRegistry(activePort);
            return registry.lookup(name);
        } else {
            throw new RemoteException("Cannot fetch stub: no registry available.");
        }
    }
}
