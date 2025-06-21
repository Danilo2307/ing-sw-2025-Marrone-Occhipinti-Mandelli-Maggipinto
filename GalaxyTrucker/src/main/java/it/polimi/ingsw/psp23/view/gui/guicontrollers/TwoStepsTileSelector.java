package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import java.util.function.BiConsumer;

public class TwoStepsTileSelector {

    private enum  Step { FIRST, SECOND };
    private Step currentStep = Step.FIRST;
    private Coord first;
    // call back
    private final BiConsumer<Coord, Coord> onDone;

    public TwoStepsTileSelector(BiConsumer<Coord, Coord> onDone) {
        this.onDone = onDone;
    }

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

    private void reset() {
        currentStep = Step.FIRST;
        first = null;
    }


    public record Coord(int x, int y) {}
}
