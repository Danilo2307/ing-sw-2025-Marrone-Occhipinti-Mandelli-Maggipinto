package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record MeteorIncoming(Meteor meteor) implements Event {

    public void handle(TuiApplication tuiApplication){

    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForMeteorIncoming(this, tuiApplication);
    }
}
