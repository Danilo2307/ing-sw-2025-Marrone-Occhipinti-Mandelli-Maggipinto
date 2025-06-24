package it.polimi.ingsw.psp23.protocol.request;

/**
 * Event triggered when the user wants to activate a double cannon during the action phase.
 * The client specifies the position of the cannon to activate (cx, cy) and the battery hub (bx, by)
 * from which one battery will be consumed.
 * */
public record AccessMatch(int matchChoice) implements Action {

    public void handle(String username){
        //Server.getInstance().getClients().get
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForAccessMatch(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}
