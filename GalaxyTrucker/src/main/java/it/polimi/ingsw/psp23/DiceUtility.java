package it.polimi.ingsw.psp23;
import java.util.Random;

public final class DiceUtility {
    // creo istanza unica (efficienza e vera casualità) della classe di Java Random che serve per generare numeri casuali
    private static final Random rand = new Random();

    // evito istanziazione
    private DiceUtility() {}

    public static int roll2to12() {
        return rand.nextInt(6) + 1 + rand.nextInt(6) + 1;
    }
}