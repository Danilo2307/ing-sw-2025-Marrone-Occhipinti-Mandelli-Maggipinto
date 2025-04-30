package it.polimi.ingsw.psp23.model.cards;

public interface CardInterfaceParametrica {

    public <T> T call(VisitorParametrico<T> visitorParametrico, int index);

}
