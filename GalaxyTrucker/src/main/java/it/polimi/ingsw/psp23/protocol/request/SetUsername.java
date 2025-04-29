package it.polimi.ingsw.psp23.protocol.request;

public record SetUsername(String username) implements Action {

    public void handle(String username){

    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForSetUsername(this, username);
    }

}
