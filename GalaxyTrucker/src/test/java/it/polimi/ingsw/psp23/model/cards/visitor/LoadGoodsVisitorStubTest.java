package it.polimi.ingsw.psp23.model.cards.visitor;

import org.junit.jupiter.api.Test;

public class LoadGoodsVisitorStubTest {

    @Test
    void testReturnNullStubs() {
        LoadGoodsVisitor visitor = new LoadGoodsVisitor();
        visitor.visitForCombatZone(null, "Fede", 0, 0);
        visitor.visitForMeteorSwarm(null, "Fede", 0, 0);
        visitor.visitForPirates(null, "Fede", 0, 0);
        visitor.visitForSlavers(null, "Fede", 0, 0);
        visitor.visitForOpenSpace(null, "Fede", 0, 0);
    }
}
