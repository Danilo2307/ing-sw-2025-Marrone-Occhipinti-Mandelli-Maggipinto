package it.polimi.ingsw.psp23.network.messages;

public final class SelectCannonsMessage extends Message{
    public SelectCannonsMessage(String text) {
        super(text);
    }

    public String toString() {
        return text;
    }
}
