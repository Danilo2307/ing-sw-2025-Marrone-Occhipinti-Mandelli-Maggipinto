package it.polimi.ingsw.psp23.model.cards;

// interfaccia in cui sono presenti i metodi di cui faremo override nelle varie carte specifiche
public interface CardInterface {

    // I defined this method as "call" because it calls the method visit in the "Visitor" interface
    public <T> T call(Visitor<T> visitor);

    public <T> T call(VisitorParametrico<T> visitorParametrico);

}
