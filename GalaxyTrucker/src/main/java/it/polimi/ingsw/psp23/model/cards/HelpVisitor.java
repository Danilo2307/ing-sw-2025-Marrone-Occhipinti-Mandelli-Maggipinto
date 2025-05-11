package it.polimi.ingsw.psp23.model.cards;

public class HelpVisitor implements Visitor<String> {

    public String visitForPlanets(Planets planets){

        return planets.help();
    }

    public String visitForAbandonedShip(AbandonedShip abandonedShip){

        return abandonedShip.help();
    }

    public String visitForAbandonedStation(AbandonedStation abandonedStation){

        return abandonedStation.help();
    }

    public String visitForCombatZone(CombatZone combatZone){

        return combatZone.help();
    }

    public String visitForEpidemic(Epidemic epidemic){

        return epidemic.help();
    }

    public String visitForMeteorSwarm(MeteorSwarm meteorSwarm){

        return meteorSwarm.help();
    }

    public String visitForOpenSpace(OpenSpace openSpace){

        return openSpace.help();
    }

    public String visitForPirates(Pirates pirates){

        return pirates.help();
    }

    public String visitForSlavers(Slavers slavers){

        return slavers.help();
    }

    public String visitForSmugglers(Smugglers smugglers){

        return smugglers.help();
    }

    public String visitForStardust(Stardust stardust){

        return stardust.help();
    }

}
