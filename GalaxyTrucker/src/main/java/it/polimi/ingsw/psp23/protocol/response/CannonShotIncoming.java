package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;


public record CannonShotIncoming(int coord, CannonShot cannonShot) implements Event {

    public void handle(TuiApplication tuiApplication){
        tuiApplication.getIOManager().print("In arrivo un " + cannonShot + " a " + coord);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForCannonShotIncoming(this, tuiApplication);
    }

}
