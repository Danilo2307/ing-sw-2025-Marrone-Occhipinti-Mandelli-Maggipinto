package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.List;

public class Epidemic extends Card {
    // Alberto

    public Epidemic(int level) {
        super(level);
    }

    public void initPlay() {
        Game.getInstance().setGameStatus(GameStatus.RunningEpidemic);
        Game.getInstance().fireEvent(new Event(Game.getInstance().getGameStatus(), meteors, impactLine));
    }

    public void play(InputObject input) {

        Board board = Game.getInstance().getCurrentPlayer().getTruck();

        List<HousingUnit> housingUnits = board.getHousingUnits();

        List<Boolean> visited = new ArrayList<>();

        int i = 0;

        int length = housingUnits.size();

        // Creo una lista contenente tutti false inizialmente
        for( i = 0 ; i < length ; i++ ) {
            visited.add(false);
        }

        // Popolo la lista con true negli indici in cui ci sono cabine connesse ad altre cabine occupate
        for(i = 0 ; i < length ; i++ ) {
            int j = i + 1 ;
            if(housingUnits.get(i).getNumAstronaut() > 0 || housingUnits.get(i).getAlien() != null) {
                while (j < length) {
                    if(housingUnits.get(j).getNumAstronaut() > 0 || housingUnits.get(j).getAlien() != null) {
                        // Per usare il metodo areTilesConnected di Board mi servono le coordinate dei componenti e me le salvo
                        // in questi interi
                        int coordXi = housingUnits.get(i).getX();
                        int coordYi = housingUnits.get(i).getY();
                        int coordXj = housingUnits.get(j).getX();
                        int coordYj = housingUnits.get(j).getY();
                        if (board.areTilesConnected(coordXi, coordYi, coordXj, coordYj)) {
                            if (!visited.get(i)) {
                                visited.set(i, true);
                            }
                            if (!visited.get(j)) {
                                visited.set(j, true);
                            }
                        }
                    }
                    j++;
                }
            }
        }

        // Adesso sottraggo un membro da ogni cabina con true
        for(i =  0 ; i < length ; i++ ) {
            if(visited.get(i)) {
                housingUnits.get(i).reduceOccupants(1);

                // Per sicurezza imposto anche a false l'elemento della lista da cui ho appena sottratto
                visited.set(i, false);
            }
        }


    }

}
