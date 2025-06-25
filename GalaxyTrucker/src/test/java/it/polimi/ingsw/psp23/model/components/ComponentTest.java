package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.exceptions.ComponentStateException;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComponentTest {

    @Test
    public void testRotate() {
        Component c = new Component(Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, 1);
        c.rotate();
        assertEquals(c.getRight(), Side.EMPTY);
        assertEquals(c.getLeft(), Side.SINGLE_CONNECTOR);
        assertEquals(c.getUp(), Side.UNIVERSAL_CONNECTOR);
        assertEquals(c.getDown(), Side.DOUBLE_CONNECTOR);
        assertEquals(90, c.getRotate());
    }

    @Test
    public void testThrows() {
        Component c = new Component(Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, 1);

        // not in hand --> exception
        assertThrows(ComponentStateException.class, () -> c.reserve());
        assertThrows(ComponentStateException.class, () -> c.discardFaceUp());
        assertThrows(ComponentStateException.class, () -> c.placeOnTruck());
    }
}
