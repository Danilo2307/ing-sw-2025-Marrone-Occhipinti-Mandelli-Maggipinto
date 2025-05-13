// Questo evento verrà mandato dal server ai client e conterranno nel parametro message la stringa di ritorno del metodo
// describe degli eventi del model che notificano aggiornamenti delle carte

package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

public record UpdateFromCard(String message) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showCardUpdate(message);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForUpdateFromCard(this, viewAPI);
    }
}
