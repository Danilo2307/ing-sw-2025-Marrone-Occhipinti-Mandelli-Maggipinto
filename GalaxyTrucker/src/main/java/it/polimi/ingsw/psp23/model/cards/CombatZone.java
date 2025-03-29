package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.model.enumeration.Challenge;
import java.util.List;
import java.util.ArrayList;

public class CombatZone extends Card {
    // Danilo

    Challenge penalty1;
    Challenge penalty2;
    Challenge penalty3;
    private final int daysLost;
    private final int goodsLost;
    private final int membersLost;
    private final List<CannonShot> cannonShot;

    //la maggior parte dei for in questo codice servono a trovare un membro che abbia uno dei parametri minimo
    //quindi si potrebbe anche pensare di definire dei metodi per farlo

    public CombatZone(int level,int daysLost, int goodsLost, int membersLost,Challenge penalty1,Challenge penalty2, Challenge penalty3, List<CannonShot> cannonshot) {
        super(level);
        this.daysLost = daysLost;
        this.goodsLost = goodsLost;
        this.membersLost = membersLost;
        this.penalty1 = penalty1;
        this.penalty2 = penalty2;
        this.penalty3 = penalty3;
        this.cannonShot = cannonshot;
    }

    public Challenge getFirstPenalty() {
        return penalty1;
    }

    public Challenge getSecondPenalty() {
        return penalty2;
    }

    public Challenge getThirdPenalty() {
        return penalty3;
    }

    public int getDaysLost() {
        return daysLost;
    }

    public int getGoodsLost() {
        return goodsLost;
    }

    public int getMembersLost() {
        return membersLost;
    }

    public List<CannonShot> getCannonShot() {
        return new ArrayList<CannonShot>(cannonShot);
    }
}
