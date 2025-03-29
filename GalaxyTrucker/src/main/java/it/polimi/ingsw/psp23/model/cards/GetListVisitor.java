package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Item;

import java.util.List;

// qui uso la classe List in maniera raw, ovvero senza dire che tipi contiene perchè in alcuni casi devono essere ad
// esempio Item e in altri cannonShot
public class GetListVisitor implements Visitor<List> {

    public List<List<Item>> visitForPlanets(Planets planets){
        return planets.getPlanetGoods();
    }

    public List visitForAbandonedShip(AbandonedShip abandonedShip){
        return null;
    }

    public List<Item> visitForAbandonedStation(AbandonedStation abandonedStation){
        return abandonedStation.getPrize();
    }

    public List visitForCannonShot(CannonShot cannonShot){
        return null;
    }

    public List<CannonShot> visitForCombatZone(CombatZone combatZone){
        return combatZone.getCannonShot();
    }

    public List visitForEpidemic(Epidemic epidemic){
        return null;
    }

    public List<Meteor> visitForMeteorSwarm(MeteorSwarm meteorSwarm){
        return meteorSwarm.getMeteors();
    }

    public List visitForOpenSpace(OpenSpace openSpace){
        return null;
    }

    public List<CannonShot> visitForPirates(Pirates pirates){
        return pirates.getCannonShot();
    }

    public List visitForSlavers(Slavers slavers){
        return null;
    }

    public List visitForSmugglers(Smugglers smugglers){
        return null;
    }

    public List visitForStardust(Stardust stardust){
        return null;
    }
}
