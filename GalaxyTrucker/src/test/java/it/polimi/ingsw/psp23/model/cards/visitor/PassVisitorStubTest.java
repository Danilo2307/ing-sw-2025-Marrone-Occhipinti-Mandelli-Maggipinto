package it.polimi.ingsw.psp23.model.cards.visitor;

import org.junit.jupiter.api.Test;

public class PassVisitorStubTest {

    @Test
    void testReturnNullStubs() {
        PassVisitor visitor = new PassVisitor();
        visitor.visitForCombatZone(null, "Fede");
        visitor.visitForMeteorSwarm(null, "Fede");
        visitor.visitForOpenSpace(null, "Fede");
        visitor.visitForEpidemic(null, "Fede");
        visitor.visitForStardust(null, "Fede");
    }
}
