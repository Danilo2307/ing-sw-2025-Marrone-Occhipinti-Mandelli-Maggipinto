package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import java.util.function.Consumer;

/**
 * The SingleTileSelector class is responsible for handling the selection of a single tile
 * within a grid. It is initialized with a consumer function that processes
 * the selected tile's coordinates and triggers additional actions when a tile is clicked.
 */
public class SingleTileSelector {

    private final Consumer<Coord> onDone;

    public SingleTileSelector(Consumer<Coord> onDone) {
        this.onDone = onDone;
    }

    /**
     * Handles the event when the user clicks on a cell in the grid.
     * It processes the cell's row and column coordinates by invoking
     * the provided consumer function.
     *
     * @param row the row index of the clicked cell
     * @param col the column index of the clicked cell
     */
    // chiamato quando utente clicca su una cella
    public void handleClick(int row, int col) {
        onDone.accept(new Coord(row, col));
    }


    public record Coord(int x, int y) {}
}
