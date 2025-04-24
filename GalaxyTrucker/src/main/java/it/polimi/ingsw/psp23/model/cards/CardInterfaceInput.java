package it.polimi.ingsw.psp23.model.cards;

public interface CardInterfaceInput {
    public <T> T call(VisitorInput<T> visitorInput, InputObject input);
}
