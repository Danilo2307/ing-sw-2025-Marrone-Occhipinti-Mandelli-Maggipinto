package it.polimi.ingsw.psp23.model.cards;

public interface CardInterface<T> {

    // I defined this method as "call" because it calls the method visit in the "Visitor" interface
    public T call(Visitor visitor);

}
