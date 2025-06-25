package it.polimi.ingsw.psp23.model.cards.visitor;

import org.junit.jupiter.api.Test;

public class BuyShipVisitorStubTest {

    @Test
    void testReturnNullStubs() {
        BuyShipVisitor visitor = new BuyShipVisitor();
        visitor.visitForPlanets(null, "Fede");
        visitor.visitForAbandonedStation(null, "Fede");
        visitor.visitForPirates(null, "Fede");
        visitor.visitForSlavers(null, "Fede");
        visitor.visitForSmugglers(null, "Fede");
        visitor.visitForCombatZone(null, "Fede");
        visitor.visitForMeteorSwarm(null, "Fede");
        visitor.visitForOpenSpace(null, "Fede");
        visitor.visitForStardust(null, "Fede");
        visitor.visitForEpidemic(null, "Fede");
    }
}
