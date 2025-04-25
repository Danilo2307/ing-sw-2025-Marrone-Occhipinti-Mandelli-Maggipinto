// Interfaccia observer necessaria per ricevere messaggi lato client.
package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.network.messages.Message;

public interface MessageObserver {

    public void messageReceived(Message message);

}
