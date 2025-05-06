package it.polimi.ingsw.psp23.model.cards;

public class GetFirePowerVisitor implements Visitor<Integer> {

    public Integer visitForPlanets(Planets planets){
        return null;
    }

    public Integer visitForAbandonedShip(AbandonedShip abandonedShip){
        return null;
    }

    public Integer visitForAbandonedStation(AbandonedStation abandonedStation){
        return null;
    }

    public Integer visitForCannonShot(CannonShot cannonShot){
        return null;
    }

    public Integer visitForCombatZone(CombatZone combatZone){
        return null;
    }

    public Integer visitForEpidemic(Epidemic epidemic){
        return null;
    }

    public Integer visitForMeteorSwarm(MeteorSwarm meteorSwarm){
        return null;
    }

    public Integer visitForOpenSpace(OpenSpace openSpace){
        return null;
    }

    public Integer visitForPirates(Pirates pirates){
        return pirates.getPrize();
    }

    public Integer visitForSlavers(Slavers slavers){
        return slavers.getCannonStrength();
    }

    public Integer visitForSmugglers(Smugglers smugglers) {
        return smugglers.getFirePower();
    }

    public Integer visitForStardust(Stardust stardust){
        return null;
    }

}
