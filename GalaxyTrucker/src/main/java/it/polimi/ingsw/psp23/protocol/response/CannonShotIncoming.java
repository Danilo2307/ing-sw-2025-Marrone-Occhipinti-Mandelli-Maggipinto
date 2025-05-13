package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;


public record CannonShotIncoming(int coord, CannonShot cannonShot) implements Event {

    public void handle(ViewAPI viewAPI){
        viewAPI.showCannonShot(coord, cannonShot);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForCannonShotIncoming(this, viewAPI);
    }

}
