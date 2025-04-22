package it.polimi.ingsw.psp23.network;

import it.polimi.ingsw.psp23.logger.FlightRecorder;
import it.polimi.ingsw.psp23.network.rmi.ProfilesRMI;
import it.polimi.ingsw.psp23.network.rmi.RMIServer;
import it.polimi.ingsw.psp23.network.rmi.RMIServerHandler;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton that manages all connected user sessions.
 * When a client connects, a new Session is registered here.
 */
public class Profiles implements ProfilesRMI {
    private static Profiles instance;
    private final HashSet<ClientInfo> sessions;
    private final Object sessionsLock;
    private final Integer heartbeatInterval;
    private static final Integer DEFAULT_HEARTBEAT_MS = 6500;

    private Profiles() {
        this.sessions = new HashSet<>();
        this.sessionsLock = new Object();
        this.heartbeatInterval = DEFAULT_HEARTBEAT_MS;
    }

    public static Profiles getInstance() {
        if (instance == null) {
            instance = new Profiles();
        }
        return instance;
    }

    /**
     * Retrieve a Session by its connection UUID.
     * @param connectionUUID unique session identifier.
     * @return the matching ClientInfo.
     * @throws DirectoryException if no session matches.
     */
    public ClientInfo getSessionById(String connectionUUID) throws DirectoryException {
        for (ClientInfo s : sessions) {
            if (s.getConnectionUUID().equals(connectionUUID)) {
                return s;
            }
        }
        throw new DirectoryException("No session with the given connection UUID found.", DirectoryException.Reason.NOT_FOUND);
    }

    /**
     * Retrieve a Session by username, if set.
     * @param username the user's chosen name.
     * @return the matching ClientInfo.
     * @throws DirectoryException if no session with that username exists.
     */
    public ClientInfo getSessionByUsername(String username) throws DirectoryException {
        for (ClientInfo s : sessions) {
            if (username.equals(s.getUsername())) {
                return s;
            }
        }
        throw new DirectoryException("No session with the given username found.", DirectoryException.Reason.NOT_FOUND);
    }

    /**
     * Retrieve a list of Sessions from a list of usernames.
     * @param usernames list of usernames to look up.
     * @return list of ClientInfos, or empty if any lookup fails.
     */
    public List<ClientInfo> getSessionsByUsernameList(List<String> usernames) {
        List<ClientInfo> result = new ArrayList<>();
        try {
            for (String user : usernames) {
                result.add(getSessionByUsername(user));
            }
        } catch (DirectoryException e) {
            // Impossible situation: return empty to indicate failure
            return new ArrayList<>();
        }
        return result;
    }

    /**
     * Check whether a username is already in use.
     * @param username candidate name.
     * @return true if taken, false otherwise.
     */
    public Boolean isUsernameTaken(String username) {
        for (ClientInfo s : sessions) {
            if (s.getUsername() != null && s.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Register a new TCP session with the given UUID.
     * @param connectionUUID unique session identifier.
     */
    public void createTCPSession(String connectionUUID) {
        ClientInfo s = new ClientInfo(connectionUUID, false, null);
        synchronized (sessionsLock) {
            sessions.add(s);
        }
    }

    /**
     * Register a new RMI session, generating a UUID internally.
     * @throws RemoteException
     * @throws MalformedURLException
     */
    @Override
    public void createUserRMI() throws RemoteException, MalformedURLException {
        String connectionUUID = UUID.randomUUID().toString();
        RMIServer stub = new RMIServer(connectionUUID, heartbeatInterval);
        synchronized (sessionsLock) {
            sessions.add(stub.getUser());
        }
        RMIServerHandler.exportObject(stub, "Session");
    }

    /**
     * Assign a username to an existing session.
     * @param connectionUUID session identifier.
     * @param username name to assign.
     * @throws DirectoryException if session not found.
     * @throws ClientInfoException if the username is invalid.
     */
    public void setUserUsername(String connectionUUID, String username) throws DirectoryException, ClientInfoException {
        ClientInfo s = getSessionById(connectionUUID);
        synchronized (sessionsLock) {
            s.setUsername(username);
        }
    }

    /**
     * Silently remove a session: close connection, exit game/lobby, unregister.
     * @param connectionUUID session identifier.
     * @param isRMI true if connected via RMI, false for TCP.
     */
    public void silentPruneUser(String connectionUUID, Boolean isRMI) {
        FlightRecorder.info("Pruning session " + connectionUUID);

        if (!isRMI) {
            try {
                TCPServer.getInstance().silentClose(connectionUUID);
            } catch (ServerException e) {
                throw new CriticalServerError("TCP network error.");
            }
        }

        try {
            if (isRMI) GameController.getInstance().exitMatchRMI(getSessionById(connectionUUID));
            else GameController.getInstance().exitMatch(getSessionById(connectionUUID));
        } catch (DirectoryException | MatchControllerException ignored) {
            // Either already removed or not in a match
        }

        try {
            if (isRMI) GameController.getInstance().exitLobbyRMI(getSessionById(connectionUUID));
            else GameController.getInstance().exitLobby(getSessionById(connectionUUID));
        } catch (DirectoryException | MatchControllerException ignored) {
            // Either already removed or not in a lobby
        }

        synchronized (sessionsLock) {
            sessions.removeIf(s -> s.getConnectionUUID().equals(connectionUUID));
        }
    }
}
