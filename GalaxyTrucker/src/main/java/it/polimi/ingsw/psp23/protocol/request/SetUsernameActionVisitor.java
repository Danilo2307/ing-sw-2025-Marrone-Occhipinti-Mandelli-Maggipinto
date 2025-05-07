package it.polimi.ingsw.psp23.protocol.request;

public class SetUsernameActionVisitor implements ActionVisitorSinglePar<String>{

    @Override
    public String visitForSetUsername(SetUsername setUsername){
        return setUsername.username();
    }
}
