package it.polimi.ingsw.psp23.model.cards.visitor;

import org.junit.jupiter.api.Test;

public class RemovePreciousItemVisitorStubTest {

    @Test
    void testReturnNullStubs() {
        RemovePreciousItemVisitor visitor = new RemovePreciousItemVisitor();
        visitor.visitForAbandonedShip(null, "Fede", 0, 0, 0);
        visitor.visitForSlavers(null, "Fede", 0, 0, 0);
    }
}
