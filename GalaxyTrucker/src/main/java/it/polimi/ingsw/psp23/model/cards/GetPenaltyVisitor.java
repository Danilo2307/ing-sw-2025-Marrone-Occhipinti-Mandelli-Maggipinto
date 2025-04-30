package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.model.enumeration.Challenge;

public class GetPenaltyVisitor implements VisitorParametrico{

    public Void visitForPlanets(Planets planets, int index, String player) {
        return null;
    }

    public Challenge visitForCombatZone(CombatZone combatZone, int index) {
        if(index == 1){
            return combatZone.getFirstPenalty();
        }
        else if(index == 2){
            return combatZone.getSecondPenalty();
        }
        else{
            return combatZone.getThirdPenalty();
        }
    }

}
