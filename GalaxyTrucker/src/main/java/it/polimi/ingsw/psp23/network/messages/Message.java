package it.polimi.ingsw.psp23.network.messages;


import java.io.Serializable;



/**
 * Represents an abstract base class for different types of messages used in a communication system.
 * The class implements the {@link Serializable} interface, enabling proper serialization of its subclasses.
 *
 * This class is designed as sealed, with its permitted subclasses explicitly defined:
 * {@link BroadcastMessage}, {@link DirectMessage}, {@link ActionMessage}, and {@link LevelSelectionMessage}.
 * Each subclass represents a specific category of messages, enabling distinct behavior or encapsulated data.
 *
 * The {@code Message} class also implements the {@link MessageInterface}, which enforces the use of
 * the Visitor design pattern. This design pattern enables external processing of messages by providing
 * an implementation of the {@link MessageVisitor} interface.
 *
 * Subclasses of {@code Message} are expected to override the
 * {@link #call(MessageVisitor)} and {@link #toString()} methods to provide type-specific behavior.
 */
// La seguente classe implementa Serializable in modo da permettere una corretta serializzazione.
// Non dovrebbe essere necessario fare l'override dei metodi perchè quelli di default vanno bene
public sealed abstract class Message implements Serializable, MessageInterface permits BroadcastMessage, DirectMessage, ActionMessage, LevelSelectionMessage {

    public Message() {

    }

    @Override
    public <T> T call(MessageVisitor<T> messageVisitor){
        return null;
    }

    @Override
    public String toString() {
        return null;
    }

}
