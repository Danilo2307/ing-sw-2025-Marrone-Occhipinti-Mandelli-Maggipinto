package it.polimi.ingsw.psp23.model.cards.visitor;

import org.junit.jupiter.api.Test;

public class RemoveBatteriesVisitorStubTest {

    @Test
    void testReturnNullStubs() {
        RemoveBatteriesVisitor visitor = new RemoveBatteriesVisitor();
        visitor.visitForAbandonedShip(null, "Fede", 0, 0, 0);
        visitor.visitForSlavers(null, "Fede", 0, 0, 0);
    }
}

