package it.polimi.ingsw.psp23.model.cards;

public class Card implements CardInterface, CardInterfaceParametrica, CardInterfaceUsername, CardInterfaceCoordinateNum, CardInterfaceCoordinate, CardInterfaceUsernameIntero {
    private int level;
    private boolean turned = false; // quando è false noi vediamo il retro della carta
    private String description;
    private final int id;

    Card(int level, int id) {
        this.level = level;
        this.id = id;
    }

    // we are using a getter because we want to know the level of the card later(for example when we have to create the small decks)
    public int getLevel() {
        return level;
    }

    public boolean isTurned() {
        return turned;
    }

    @Override
    public String toString() {
        throw new RuntimeException("Not Override of To string");
    }

    public int getId() {
        return id;
    }


    public void setTurned(boolean updatedFlag) {
        turned = updatedFlag;
    }

    // call sarebbeero i metodi presi dall'implementazione di CardInterface, qui scrivo due metodi "inutili"
    // per poi fare override nelle varie sottoclassi
    @Override
    public <T> T call(Visitor<T> visitor){
        return null;
    }

    @Override
    public <T> T call(VisitorParametrico<T> visitorParametrico, int index){
        return null;
    }

    @Override
    public <T> T call(VisitorUsername<T> visitorUsername, String username){
        return null;
    }

    @Override
    public <T> T call(VisitorCoordinateNum<T> visitorCoordinateNum, String username, int i, int j, int num){
        return null;
    }

    @Override
    public <T> T call(VisitorCoordinate<T> visitorCoordinate, String username, int i, int j){
        return null;
    }

    @Override
    public <T> T call(VisitorUsernameIntero<T> visitorUsernameIntero, String username, int i){
        return null;
    }
}