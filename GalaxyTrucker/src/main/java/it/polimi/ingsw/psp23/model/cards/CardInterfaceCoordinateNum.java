package it.polimi.ingsw.psp23.model.cards;

public interface CardInterfaceCoordinateNum {

    public <T> T call(VisitorCoordinateNum<T> visitorCoordinateNum, String username, int i, int j, int num);

}
