// Interfaccia observer necessaria per ricevere messaggi lato client.
package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.events.server.Event;
import it.polimi.ingsw.psp23.network.messages.Message;

public interface MessageObserver {

    public void messageReceived(Event event);

}
