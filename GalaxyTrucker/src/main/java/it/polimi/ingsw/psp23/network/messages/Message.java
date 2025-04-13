package it.polimi.ingsw.psp23.network.messages;


import java.io.Serializable;

// La seguente classe implementa Serializable in modo da permettere una corretta serializzazione.
// Non dovrebbe essere necessario fare l'override dei metodi perchè quelli di default vanno bene
public sealed abstract class Message implements Serializable permits UpdateStateMessage, SelectCannonsMessage{

    public Message() {}

}
