package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.network.*;
import it.polimi.ingsw.psp23.network.client.*;
import it.polimi.ingsw.psp23.network.common.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.ServerError;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class represents the RMI implementation of the UserStubRMI interface.
 * It handles client-server communication via RMI and manages the user's state and events.
 */
public class RMIServer implements RMIServerInterface {
    private final ClientInfo user;
    private PushServiceOfClientRMI clientPushCallback;
    private final AtomicLong lastHeartbeat;
    private final ScheduledExecutorService executorService;

    /**
     * Constructs a UserStub and initializes heartbeat monitoring.
     * If the client fails to send a heartbeat in time, the server will consider it disconnected.
     *
     * @param connectionUUID the UUID of the client connection
     * @param heartbeatMs the maximum allowed time between heartbeats in milliseconds
     * @throws RemoteException if an error occurs during initialization
     */
    public RMIServer(String connectionUUID, Integer heartbeatMs) throws RemoteException {
        this.user = new ClientInfo(connectionUUID, true, this);
        this.clientPushCallback = null;
        this.lastHeartbeat = new AtomicLong(System.currentTimeMillis());
        this.executorService = Executors.newSingleThreadScheduledExecutor();

        this.executorService.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis() - this.lastHeartbeat.get() > heartbeatMs) {
                try {
                    RMIServerHandler.unexportObject("User", true);
                } catch (NotBoundException | RemoteException e) {
                    throw new ServerCriticalError("Failed to release RMI resources for disconnected client.");
                } finally {
                    Profiles.getInstance().silentPruneUser(connectionUUID, true);
                    this.executorService.shutdownNow();
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    /**
     * Returns the associated user instance.
     *
     * @return the user object
     */
    public ClientInfo getUser() {
        return user;
    }

    /**
     * Registers the client-side push service used by the server to send events back.
     * Only the first non-null registration is accepted.
     *
     * @param clientPushCallback the remote push service of the client
     * @throws RemoteException if an RMI error occurs
     */
    @Override
    public void registerClientPushCallback(PushServiceOfClientRMI clientPushCallback) throws RemoteException {
        if (this.clientPushCallback != null) return;
        if (clientPushCallback != null) {
            this.clientPushCallback = clientPushCallback;
        }
    }

    /**
     * Receives a heartbeat signal from the client and pushes back a sense signal.
     *
     * @param heartbeat the heartbeat message
     * @throws RemoteException if the client is unreachable
     */
    @Override
    public void sendHeartbeat(Heartbeat heartbeat) throws RemoteException {
        this.lastHeartbeat.set(System.currentTimeMillis());
    }

    /**
     * Pushes an event object to the client using its registered push service.
     *
     * @param object the event to be sent
     * @throws RemoteException if the client is not reachable
     */
    public void pushEvent(Object object) throws RemoteException {
        if (this.clientPushCallback == null) return;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Void> future = executorService.submit(() -> {
            clientPushCallback.pushEvent(object);
            return null;
        });

        try {
            future.get();
        } catch (InterruptedException e) {
            throw new ServerCriticalError("Unexpected thread interruption occurred in server execution.");
        } catch (ExecutionException e) {
//            if (e.getCause() instanceof RemoteException) {
//                throw (RemoteException) e.getCause();
//            }
            throw new ServerCriticalError("Unexpected execution failure during push to client.");
        }
    }

    /**
     * Attempts to set the user's username if the state allows it.
     *
     * @param username the desired username
     * @return true if the username was set successfully, false otherwise
     * @throws RemoteException if an RMI error occurs
     */
    @Override
    public Boolean setUsernameRMI(String username) throws RemoteException {
        if (this.user.getState().getStateType() != StateType.SETTINGUSERNAME) return false;

        if (!Profiles.getInstance().isUsernameTaken(username)) {
            try {
                Profiles.getInstance().setUserUsername(this.user.getConnectionUUID(), username);
                this.user.setState(new ChooseCreateJoinState(this.user));
                return true;
            } catch (ProfilesException | UserException e) {
                return false;
            }
        }

        return false;
    }

    /**
     * Attempts to create a new lobby for the user.
     *
     * @param name the lobby name
     * @param maxPlayers the max number of players
     * @return true if lobby creation succeeds, false otherwise
     * @throws RemoteException if an RMI error occurs
     */
    @Override
    public Boolean createLobbyRMI(String name, Integer maxPlayers) throws RemoteException {
        if (this.user.getState().getStateType() != StateType.CHOOSECREATEJOIN) return false;

        try {
            MatchController.getInstance().createLobbyRMI(name, maxPlayers, this.user);
            return true;
        } catch (GameLobbyException | MatchControllerException e) {
            return false;
        }
    }

    /**
     * Joins the user to a specific lobby.
     *
     * @param lobbyUUID the target lobby's UUID
     * @return true if the join was successful, false otherwise
     * @throws RemoteException if an RMI error occurs
     */
    @Override
    public Boolean joinLobbyRMI(String lobbyUUID) throws RemoteException {
        if (this.user.getState().getStateType() != StateType.CHOOSECREATEJOIN) return false;

        try {
            MatchController.getInstance().joinLobbyRMI(lobbyUUID, this.user);
            return true;
        } catch (GameLobbyException | MatchControllerException e) {
            return false;
        }
    }

    /**
     * Retrieves a list of lobbies available for the user to join.
     *
     * @return list of available lobbies or null if not available
     * @throws RemoteException if an RMI error occurs
     */
    @Override
    public List<ListOfLobbyToJoinMessage.LobbyInfo> getListOfLobbyToJoinRMI() throws RemoteException {
        if (this.user.getState().getStateType() != StateType.CHOOSECREATEJOIN) return null;

        try {
            return MatchController.getInstance().getListOfLobbyToJoinRMI(this.user);
        } catch (MatchControllerException e) {
            return null;
        }
    }

    /**
     * Retrieves current lobby information for the user.
     *
     * @return lobby info or null if not available
     * @throws RemoteException if an RMI error occurs
     */
    @Override
    public LobbyInfo getLobbyInfoRMI() throws RemoteException {
        if (this.user.getState().getStateType() == StateType.INLOBBY ||
                this.user.getState().getStateType() == StateType.INGAME) {
            try {
                return MatchController.getInstance().getLobbyInfoRMI(this.user);
            } catch (MatchControllerException e) {
                return null;
            }
        }

        return null;
    }

    /**
     * Starts the lobby session for the user.
     *
     * @return true if the lobby was started, false otherwise
     * @throws RemoteException if an RMI error occurs
     */
    @Override
    public Boolean startLobbyRMI() throws RemoteException {
        if (this.user.getState().getStateType() != StateType.INLOBBY) return false;

        try {
            MatchController.getInstance().startLobbyRMI(this);
            return true;
        } catch (MatchControllerException e) {
            return false;
        }
    }

    /**
     * Exits the current lobby session.
     *
     * @return true if the user exited the lobby, false otherwise
     * @throws RemoteException if an RMI error occurs
     */
    @Override
    public Boolean exitLobbyRMI() throws RemoteException {
        if (this.user.getState().getStateType() != StateType.INLOBBY) return false;

        try {
            MatchController.getInstance().exitLobbyRMI(this.user);
            return true;
        } catch (MatchControllerException e) {
            return false;
        }
    }
}

