package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.exceptions.ContainerException;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.Container;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.List;

public class AbandonedStation extends Card {

    private final int days;
    private final int numMembers;
    private final List<Item> prize;

    public AbandonedStation(int level, int days, int numMembers, List<Item> prize) {
        super(level);
        this.days = days;
        this.numMembers = numMembers;
        this.prize = prize;
    }

    public int getDays() {
        return days;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public List<Item> getPrize() {
        return new ArrayList<>(prize);
    }

    @Override
    public Object call(Visitor visitor) {
        return visitor.visitForAbandonedStation(this);
    }


    public void initPlay() {
        Game.getInstance().setGameStatus(GameStatus.BooleanRequest);
        Game.getInstance().fireEvent(new Event(Game.getInstance().getGameStatus(), days, numMembers, prize ));
    }

    public void play(InputObject inputObject) {
        Player player = Game.getInstance().getCurrentPlayer();

            if (inputObject.getDecision()) {
                int i = 0;

                inputValidity(inputObject);

                for (Item item : prize) {

                    //SUPPONGO CHE NELL'INPUT LE POSIZIONI DEI CONTAINER SIANO MESSI IN ORDINE RISPETTO ALLE MERCI DA DISTRIBUIRE

                    int coordX = inputObject.getLista().get(i)[0];

                    int coordY = inputObject.getLista().get(i)[1];

                    Component analizedComponent = player.getTruck().getTile(coordX, coordY);

                    Container container = player.getTruck().getContainers().get(player.getTruck().getContainers().indexOf(analizedComponent));

                    container.loadItem(item); //lancia un eccezione in caso di carico non valido per quel container o di container pieno

                    i++;
                }
                Utility.updatePosition(Game.getInstance().getPlayers(), Game.getInstance().getPlayers().indexOf(player), -days);
            }
    }

    public void inputValidity(InputObject inputObject){
        Player player = Game.getInstance().getCurrentPlayer();

        if (inputObject.getLista().size() != prize.size()) {
            throw new RuntimeException("Input non valido");
        }

        if(Game.getInstance().getCurrentPlayer().getTruck().calculateCrew() < numMembers){
            throw new RuntimeException("Non hai abbastanza membri dell'equipaggio");
        }

        int i = 0;

        int counter;

        for(Integer[] pos : inputObject.getLista()){
            counter = 0;

           for(Integer[] pos2 : inputObject.getLista()){
                if(pos2[0] == pos[0] && pos2[1] == pos[1]){
                    counter++;
                }
           }

            Component analizedComponent = player.getTruck().getTile(pos[0], pos[1]);

            if(!(player.getTruck().getContainers().contains(analizedComponent))){
                throw new ContainerException("Input errato, non hai selezionato solo Container");
            }

           Container container = player.getTruck().getContainers().get(player.getTruck().getContainers().indexOf(analizedComponent));

            if(counter > (container.getSize() - container.getItems().size())){
                throw new ContainerException("Uno dei tuoi container non ha abbastanza spazio");
            }

            if(!container.canLoadItem(prize.get(i))){
                throw new ContainerException("Almeno una merce non è compatibile con il container selezionato");
            }



            i++;

        }

    }


}
