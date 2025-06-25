package it.polimi.ingsw.psp23.model.cards.visitor;

import org.junit.jupiter.api.Test;

public class ReduceCrewVisitorNumStubTest {

    @Test
    void testReturnNullStub() {
        ReduceCrewVisitorNum visitor = new ReduceCrewVisitorNum();
        visitor.visitForSmugglers(null, "Fede", 0, 0, 0);
    }
}
