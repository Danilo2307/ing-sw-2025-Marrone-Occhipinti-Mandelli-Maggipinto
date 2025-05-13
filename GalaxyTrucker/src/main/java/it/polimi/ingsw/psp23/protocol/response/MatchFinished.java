package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record MatchFinished(String message) implements Event {

    public void handle(TuiApplication tuiApplication){
        tuiApplication.getClient().stopListeningForServerThread();
        tuiApplication.getIOManager().print(message);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForMatchFinished(this, tuiApplication);
    }

}
