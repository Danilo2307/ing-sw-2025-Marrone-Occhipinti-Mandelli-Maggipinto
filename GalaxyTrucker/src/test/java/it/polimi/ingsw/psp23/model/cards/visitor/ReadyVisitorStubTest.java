package it.polimi.ingsw.psp23.model.cards.visitor;

import org.junit.jupiter.api.Test;

public class ReadyVisitorStubTest {

    @Test
    void testReturnNullStubs() {
        ReadyVisitor visitor = new ReadyVisitor();
        visitor.visitForPlanets(null, "Fede");
        visitor.visitForAbandonedShip(null, "Fede");
        visitor.visitForAbandonedStation(null, "Fede");
        visitor.visitForEpidemic(null, "Fede");
        visitor.visitForStardust(null, "Fede");
    }
}
