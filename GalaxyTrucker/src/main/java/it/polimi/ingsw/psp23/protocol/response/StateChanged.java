package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.TUI.TuiState;


public record StateChanged(GameStatus newState) implements Event {

    public void handle(TuiApplication tuiApplication) {
        tuiApplication.getIOManager().print("Stato modificato a: " + newState + "\n");
        switch (newState){
            case Building -> tuiApplication.setState(TuiState.BUILDING);
            case CheckBoards -> tuiApplication.setState(TuiState.CHECK);
            case SetCrew -> tuiApplication.setState(TuiState.ADDCREW);
            case Playing -> tuiApplication.setState(TuiState.PLAY);
        }
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForStateChanged(this, tuiApplication);
    }

}
