// Interfaccia observer necessaria per ricevere messaggi lato client.
package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.protocol.response.Event;

public interface MessageObserver {

    public void messageReceived(Event event);

}
