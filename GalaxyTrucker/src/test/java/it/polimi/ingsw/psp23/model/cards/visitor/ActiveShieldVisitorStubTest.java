package it.polimi.ingsw.psp23.model.cards.visitor;

import org.junit.jupiter.api.Test;

public class ActiveShieldVisitorStubTest {

    @Test
    void testReturnNullStubs() {
        ActiveShieldVisitor visitor = new ActiveShieldVisitor();
        visitor.visitForAbandonedStation(null, "Fede", 0, 0);
        visitor.visitForPlanets(null, "Fede", 0, 0);
        visitor.visitForSmugglers(null, "Fede", 0, 0);
        visitor.visitForSlavers(null, "Fede", 0, 0);
        visitor.visitForOpenSpace(null, "Fede", 0, 0);
    }
}