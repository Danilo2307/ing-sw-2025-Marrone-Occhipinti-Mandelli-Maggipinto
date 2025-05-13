package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

public record MeteorIncoming(Meteor meteor) implements Event {

    public void handle(ViewAPI viewAPI){
        viewAPI.showMeteor(meteor);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForMeteorIncoming(this, viewAPI);
    }
}
