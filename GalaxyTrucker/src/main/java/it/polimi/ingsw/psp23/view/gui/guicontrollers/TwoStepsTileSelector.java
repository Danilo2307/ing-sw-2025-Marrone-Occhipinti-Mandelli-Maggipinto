package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import java.util.function.BiConsumer;

/**
 * This class allows selecting two tiles through consecutive clicks, invoking a callback
 * after both selections are made. It maintains an internal state to track the selection process.
 */
public class TwoStepsTileSelector {

    private enum  Step { FIRST, SECOND };
    private Step currentStep = Step.FIRST;
    private Coord first;
    // call back
    private final BiConsumer<Coord, Coord> onDone;

    public TwoStepsTileSelector(BiConsumer<Coord, Coord> onDone) {
        this.onDone = onDone;
    }

    /**
     * Handles a click event on a tile by recording its coordinates and managing the two-step
     * selection process. In the first step, the method saves the coordinates of the first tile.
     * In the second step, it invokes a callback with the coordinates of both tiles and resets
     * the internal state to allow a new selection process.
     *
     * @param row the row index of the clicked tile
     * @param col the column index of the clicked tile
     */
    // metodo invocato ad ogni click su una tile
    public void handleClick(int row, int col) {
        Coord c = new Coord(row, col);

        switch (currentStep) {
            case FIRST -> {
                // salvo le coordinate del primo click
                first = c;
                currentStep = Step.SECOND;
            }
            case SECOND -> {
                // chiamata alla callback
                onDone.accept(first, c);
                reset();
            }
        }
    }

    /**
     * Resets the internal state of the selection process. Sets the current step to the initial step
     * and clears any stored data from the first selection. This allows a fresh two-step selection
     * process to begin.
     */
    private void reset() {
        currentStep = Step.FIRST;
        first = null;
    }


    public record Coord(int x, int y) {}
}
