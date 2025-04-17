package it.polimi.ingsw.psp23.network;

import it.polimi.ingsw.psp23.network.rmi.*;
import it.polimi.ingsw.psp23.network.state.State;
import it.polimi.ingsw.psp23.network.state.WaitingForUsernameState;

/**
 * Holds minimal information about a connected client.
 * Includes its connection ID, current state, and communication type.
 */
public class ClientInfo {
    private final String connectionUUID;
    private final boolean isRMI;
    private String username;
    private State state;
    private RMIServer serverRMI;

    /**
     * Creates a new ClientInfo entry.
     *
     * @param connectionUUID the unique connection ID
     * @param isRMI whether the client is connected via RMI
     * @param serverRMI the ServerRMI instance if RMI is used, null otherwise
     */
    public ClientInfo(String connectionUUID, boolean isRMI, RMIServer serverRMI) {
        this.connectionUUID = connectionUUID;
        this.isRMI = isRMI;

        if (isRMI) {
            this.serverRMI = serverRMI;
            this.state = null;
        } else {
            // TODO: Handle TCP/IP or other protocols in future
        }
        this.username = null;
        this.state = new WaitingForUsernameState(this);
    }

    /**
     * Gets the connection UUID.
     *
     * @return the UUID string
     */
    public String getConnectionUUID() {
        return connectionUUID;
    }

    /**
     * Gets the user's current state.
     * Valid only for RMI connections.
     *
     * @return the current state
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the user's current state.
     * Valid only for RMI connections.
     *
     * @param state the new state to assign
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Gets the ServerRMI reference.
     *
     * @return the ServerRMI if RMI is enabled, null otherwise
     */
    public RMIServer getServerRMI() {
        return serverRMI;
    }

    /**
     * Sets the username of the User.
     * @param username The username to set.
     * @throws UserException If the username is invalid.
     */
    public void setUsername(String username) throws UserException {
        // Checks for the validity of the username
        Pattern pattern = Pattern.compile(TextValidator.usernameValidator);
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            throw new UserException("Username is invalid.", UserException.Reason.INVALID_USERNAME);
        }

        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

}