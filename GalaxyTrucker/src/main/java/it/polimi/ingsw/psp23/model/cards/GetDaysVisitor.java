package it.polimi.ingsw.psp23.model.cards;

public class GetDaysVisitor implements Visitor {

    public Integer visitForPlanets(Planets planets){
        return planets.getDaysLost();
    }

    public Integer visitForAbandonedShip(AbandonedShip abandonedShip){
        return abandonedShip.getDays();
    }

    public Integer visitForAbandonedStation(AbandonedStation abandonedStation){
        return abandonedStation.getDays();
    }

    public Integer visitForCannonShot(CannonShot cannonShot){
        return null;
    }

    public Integer visitForCombatZone(CombatZone combatZone){
        return combatZone.getDaysLost();
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
        return pirates.getDays();
    }

    public Integer visitForSlavers(Slavers slavers){
        return slavers.getDays();
    }

    public Integer visitForSmugglers(Smugglers smugglers){
        return smugglers.getDays();
    }

    public Integer visitForStardust(Stardust stardust){
        return null;
    }

}
