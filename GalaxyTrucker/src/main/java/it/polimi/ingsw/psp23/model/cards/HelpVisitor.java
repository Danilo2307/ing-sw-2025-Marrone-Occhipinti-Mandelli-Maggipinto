package it.polimi.ingsw.psp23.model.cards;

public class HelpVisitor implements Visitor<String> {

    public String visitForPlanets(Planets planets){
        planets.help();
        return null;
    }

    public String visitForAbandonedShip(AbandonedShip abandonedShip){
        abandonedShip.help();
        return null;
    }

    public String visitForAbandonedStation(AbandonedStation abandonedStation){
        abandonedStation.help();
        return null;
    }

    public String visitForCombatZone(CombatZone combatZone){
        combatZone.help();
        return null;
    }

    public String visitForEpidemic(Epidemic epidemic){
        epidemic.help();
        return null;
    }

    public String visitForMeteorSwarm(MeteorSwarm meteorSwarm){
        meteorSwarm.help();
        return null;
    }

    public String visitForOpenSpace(OpenSpace openSpace){
        openSpace.help();
        return null;
    }

    public String visitForPirates(Pirates pirates){
        pirates.help();
        return null;
    }

    public String visitForSlavers(Slavers slavers){
        slavers.help();
        return null;
    }

    public String visitForSmugglers(Smugglers smugglers){
        smugglers.help();
        return null;
    }

    public String visitForStardust(Stardust stardust){
        stardust.help();
        return null;
    }

}
