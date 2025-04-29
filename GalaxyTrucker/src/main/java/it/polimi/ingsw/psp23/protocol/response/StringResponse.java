package it.polimi.ingsw.psp23.protocol.response;


import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

/* evento usato da server per inviare info testuali di qualsiasi genere al client (conferme, esiti operazioni)*/
public record StringResponse(String message) implements Event {

    public void handle(TuiApplication tui) {
        tui.getIOManager().print(message);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication) {
        return eventVisitor.visitForStringResponse(this, tuiApplication);
    }

}
