package it.polimi.ingsw.psp23.model.cards;

public interface CardInterfaceUsername {
    public <T> T call(VisitorUsername<T> visitorUsername, String username);
}
