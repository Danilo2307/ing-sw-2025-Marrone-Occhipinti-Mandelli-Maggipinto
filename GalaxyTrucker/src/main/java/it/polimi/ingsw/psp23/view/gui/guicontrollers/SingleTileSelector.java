package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import java.util.function.Consumer;

public class SingleTileSelector {

    private final Consumer<Coord> onDone;

    public SingleTileSelector(Consumer<Coord> onDone) {
        this.onDone = onDone;
    }

    // chiamato quando utente clicca su una cella
    public void handleClick(int row, int col) {
        onDone.accept(new Coord(row, col));
    }


    public record Coord(int x, int y) {}
}
