package it.polimi.ingsw.psp23.model.cards;

public interface CardInterfaceCoordinate {

    public <T> T call(VisitorCoordinate<T> visitorCoordinate, String username, int i, int j);

}
