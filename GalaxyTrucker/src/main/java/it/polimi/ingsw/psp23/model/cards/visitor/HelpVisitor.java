package it.polimi.ingsw.psp23.model.cards.visitor;

import it.polimi.ingsw.psp23.model.cards.*;

public class HelpVisitor implements VisitorUsername<String> {

    public String visitForPlanets(Planets planets, String username) {
        return planets.help(username);
    }

    public String visitForAbandonedShip(AbandonedShip abandonedShip, String username){
        return abandonedShip.help(username);
    }

    public String visitForAbandonedStation(AbandonedStation abandonedStation, String username){
        return abandonedStation.help(username);
    }

    public String visitForCombatZone(CombatZone combatZone, String username){
        return combatZone.help(username);
    }

    public String visitForEpidemic(Epidemic epidemic, String username){
        return epidemic.help(username);
    }

    public String visitForMeteorSwarm(MeteorSwarm meteorSwarm, String username){
        return meteorSwarm.help(username);
    }

    public String visitForOpenSpace(OpenSpace openSpace, String username){
        return openSpace.help(username);
    }

    public String visitForPirates(Pirates pirates, String username){
        return pirates.help(username);
    }

    public String visitForSlavers(Slavers slavers, String username){
        return slavers.help(username);
    }

    public String visitForSmugglers(Smugglers smugglers, String username){
        return smugglers.help(username);
    }

    public String visitForStardust(Stardust stardust, String username){
        return stardust.help(username);
    }

}
