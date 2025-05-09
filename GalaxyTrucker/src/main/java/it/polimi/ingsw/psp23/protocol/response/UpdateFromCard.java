// Questo evento verrà mandato dal server ai client e conterranno nel parametro message la stringa di ritorno del metodo
// describe degli eventi del model che notificano aggiornamenti delle carte

package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record UpdateFromCard(String message) implements Event {

    public void handle(TuiApplication tui) {
        tui.getIOManager().print(message);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForUpdateFromCard(this, tuiApplication);
    }
}
