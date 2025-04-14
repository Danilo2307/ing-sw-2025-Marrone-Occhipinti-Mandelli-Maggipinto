package it.polimi.ingsw.psp23.network.common;

import java.io.Serializable;

/**
 * Represents a keep-alive signal sent from the client to the server.
 * The server expects these messages periodically to confirm that the client is still active.
 * Failure to receive them in time may result in disconnection.
 */
public class Heartbeat implements Serializable {
}
