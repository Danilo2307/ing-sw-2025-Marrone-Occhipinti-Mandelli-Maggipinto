package it.polimi.ingsw.psp23.protocol.request;

public record ActivateEngine(int ex, int ey, int bx, int by) implements Action {

    public void handle(String username){

    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForActivateEngine(this, username);
    }

}

