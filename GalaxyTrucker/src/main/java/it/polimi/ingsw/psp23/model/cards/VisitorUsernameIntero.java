package it.polimi.ingsw.psp23.model.cards;

public interface VisitorUsernameIntero<T>{

    public T visitForPlanets(Planets planets, String username, int i);

}
