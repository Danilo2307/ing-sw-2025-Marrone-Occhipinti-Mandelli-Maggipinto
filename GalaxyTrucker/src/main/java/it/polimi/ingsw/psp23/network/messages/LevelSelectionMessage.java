package it.polimi.ingsw.psp23.network.messages;

public final class LevelSelectionMessage extends Message{
    int level;

    public LevelSelectionMessage(int level) {
        this.level = level;
    }

    public String toString(){
        return String.valueOf(level);
    }
}
