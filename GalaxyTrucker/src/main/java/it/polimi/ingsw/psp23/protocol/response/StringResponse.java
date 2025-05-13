package it.polimi.ingsw.psp23.protocol.response;


import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

/* evento usato da server per inviare info testuali di qualsiasi genere al client (conferme, esiti operazioni)*/
public record StringResponse(String message) implements Event {

    /// TODO: alcuni StringResponse non sono stampe di messaggi per la GUI (getVisibleDeck), ma
    /// dovremo dividerli in nuovi eventi. Lato TUI rimarrà uguale, ma lato GUI dovrà mostrare le carte, non il toString() di esse.
    public void handle(ViewAPI viewAPI) {
        viewAPI.showMessage(message);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI) {
        return eventVisitor.visitForStringResponse(this, viewAPI);
    }

    @Override
    public String toString() {
        return message;
    }

}
