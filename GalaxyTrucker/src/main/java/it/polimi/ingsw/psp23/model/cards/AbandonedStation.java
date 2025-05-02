package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.exceptions.ContainerException;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Events.EventForAbandonedShip;
import it.polimi.ingsw.psp23.model.Events.EventForAbandonedStation;
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
    private String isSold = null;
    private int indexItem = 0;

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
        Game.getInstance().setGameStatus(GameStatus.INIT_ABANDONEDSTATION);
        Game.getInstance().fireEvent(new EventForAbandonedStation(Game.getInstance().getGameStatus(), days, numMembers, prize ));
    }

    public void play() {

        List<Player> players = Game.getInstance().getPlayers();
        for (Player player : players) {
            if(isSold == null){
                break;
            }
            if (player.getTruck().calculateCrew() >= numMembers && isSold.equals(player.getNickname()) ){
                    Utility.updatePosition(players, players.indexOf(player), -days);
                    break;
            }
        }
    }


    public void endPlay() {
        Game.getInstance().setGameStatus(GameStatus.END_ABANDONEDSTATION);
    }

    public void loadItem(int x, int y) {
        if(indexItem < prize.size() && Game.getInstance().getGameStatus().equals(GameStatus.END_ABANDONEDSTATION)) {
            List<Item> items = new ArrayList<>();
            items.add(prize.get(indexItem));
            Game.getInstance().getPlayerFromNickname(isSold).getTruck().loadGoods(items, x, y);
            indexItem++;
        }else{
            throw new RuntimeException("Le merci sono terminate");
        }
    }

    public String getIsSold() {
        return isSold;
    }

    public void setIsSold(String nickname) {
        this.isSold = nickname;
    }

    //TODO: i metodi set e get isSold e loaditem andranno implementati con i visitor (loaditem seguirà l'ordine stampato delle merci)

  /*  public void inputValidity(InputObject inputObject){
        Player player = Game.getInstance().getCurrentPlayer();

        if (inputObject.getContainers().size() != prize.size()) {
            throw new RuntimeException("Input non valido");
        }

        if(Game.getInstance().getCurrentPlayer().getTruck().calculateCrew() < numMembers){
            throw new RuntimeException("Non hai abbastanza membri dell'equipaggio");
        }

        int i = 0;

        int counter;

        for(Integer[] pos : inputObject.getContainers()){
            counter = 0;

           for(Integer[] pos2 : inputObject.getContainers()){
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
*/

}
