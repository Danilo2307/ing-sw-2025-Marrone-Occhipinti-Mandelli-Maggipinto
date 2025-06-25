package it.polimi.ingsw.psp23.model.cards.visitor;

import org.junit.jupiter.api.Test;

public class GetCosmicCreditsVisitorStubTest {

    @Test
    void testReturnNullStubs() {
        GetCosmicCreditsVisitor visitor = new GetCosmicCreditsVisitor();
        visitor.visitForPlanets(null, "Fede");
        visitor.visitForAbandonedShip(null, "Fede");
        visitor.visitForAbandonedStation(null, "Fede");
        visitor.visitForSmugglers(null, "Fede");
        visitor.visitForCombatZone(null, "Fede");
        visitor.visitForMeteorSwarm(null, "Fede");
        visitor.visitForOpenSpace(null, "Fede");
        visitor.visitForEpidemic(null, "Fede");
        visitor.visitForStardust(null, "Fede");
    }}
