package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Events.EventForCombatZone;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.Container;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.Challenge;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.List;
import java.util.ArrayList;

public class CombatZone extends Card{
    // Danilo

    private final int daysLost;
    private final int goodsLost;
    private final int membersLost;
    private final List<CannonShot> cannonShot;
    private final List<Challenge> penalties;
    private int countMember;
    private int countGood;
    private String playerMin;

    public CombatZone(int level,int daysLost, int goodsLost, int membersLost, List<Challenge> penalties, List<CannonShot> cannonshot) {
        super(level);
        this.daysLost = daysLost;
        this.goodsLost = goodsLost;
        this.membersLost = membersLost;
        this.penalties = penalties;
        this.cannonShot = cannonshot;
        this.countMember = 0;
        this.countGood = 0;
        this.playerMin = null;
    }

    public List<Challenge> getPenalties() {
        return new ArrayList<Challenge>(penalties);
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

    private Player findMinMembers(){
        List<Player> players = Game.getInstance().getPlayers();
        int tmp = players.get(0).getTruck().calculateCrew();
        Player playerTmp = players.get(0);
        for (Player p : players) {
            if (p.getTruck().calculateCrew() < tmp) {
                playerTmp = p;
                tmp = p.getTruck().calculateCrew();
            }
        }
        return playerTmp;
    }

    //prima di questo metodo il player deve chiamare activate cannon in modo da aumentare la sua potenza di fuoco
    private Player findMinCannonStrength(){
        List<Player> players = Game.getInstance().getPlayers();
        double tmp = players.get(0).getTruck().calculateCannonStrength();
        Player playerTmp = players.get(0);
        for (Player p : players) {
            if (p.getTruck().calculateCannonStrength() < tmp) {
                playerTmp = p;
                tmp = p.getTruck().calculateCannonStrength();
            }
        }
        return playerTmp;
    }

    private Player findMinEngineStrength(){
        List<Player> players = Game.getInstance().getPlayers();
        int tmp = players.get(0).getTruck().calculateEngineStrength();
        Player playerTmp = players.get(0);
        for (Player p : players) {
            if (p.getTruck().calculateEngineStrength() < tmp) {
                playerTmp = p;
                tmp = p.getTruck().calculateEngineStrength();
            }
        }
        return playerTmp;
    }

    public void reduceCrew(int i, int j, int num) {
        if(countMember < membersLost && Game.getInstance().getCurrentPlayer().equals(playerMin)){
            Board board = Game.getInstance().getCurrentPlayer().getTruck();
            Component[][] ship = board.getShip();
//        if ((!board.isValid(i, j)) || board.isFree(i,j))
//            throw new InvalidCoordinatesException("Coordinates("+i+","+j+") cannon contain a tile or don't contain one");
            Component tile = ship[i][j];
            switch (tile) {
                case HousingUnit cabin -> {
                    int index = board.getHousingUnits().indexOf(cabin);
                    if (index == -1) {
                        throw new CardException("HousingUnit not found in 'housingUnit' list: error in reduceCrew of Board");
                    } else {
                        try {
                            // controllo rimozione implementato in reduceOccupants
                            board.getHousingUnits().get(index).reduceOccupants(num);
                            countMember += num;
                        }
                        catch (IllegalArgumentException e) {
                            throw new CardException("Failed to remove "+ num + "crew members from HousingUnit at Ship["+i+"]["+j+"]" + e.getMessage());
                        }
                    }
                }
                default -> throw new CardException("Component at ["+i+"]["+j+"] is not a housing unit");
            }
        }
        else if(countMember == membersLost){
            throw new CardException("I membri da perdere sono esauriti!");
        }
        else if(!Game.getInstance().getCurrentPlayer().equals(playerMin)){
            throw new CardException("Il player che ha meno potenza motrice è" + playerMin);
        }
        else if(penalties.get(1) == Challenge.Goods){
            throw new CardException("La tua penalità è perdere le tue" + getGoodsLost() + "merci più importanti!" );
        }
    }

    public void removePreciousItem(int i, int j, int item) {
        if(countGood < goodsLost && Game.getInstance().getCurrentPlayer().equals(playerMin)) {
            Board board = Game.getInstance().getCurrentPlayer().getTruck();
            Component[][] ship = board.getShip();
            Component tile = ship[i][j];
            switch (tile) {
                case Container c -> {
                    // Trovo l'indice del container corrispondente a ship[i][j] nella lista dei container
                    // L'oggetto in ship[i][j] è lo stesso oggetto (stesso riferimento) inserito in containers, quindi indexOf funziona correttamente.
                    int index = board.getContainers().indexOf(ship[i][j]);
                    // Controllo che l'indice sia valido: se è -1, significa che ship[i][j] non è un container noto
                    if (index == -1) {
                        throw new CardException("Invalid coordinates: ship[i][j] does not contain a container.");
                    }
                    // Controllo che l'item sia tra i più preziosi attualmente a bordo
                    else if (!board.isMostPrecious(board.getContainers().get(index).getItems().get(item))) {
                        throw new CardException("Item" + board.getContainers().get(index).getItems().get(item).getColor() + " at Container[" + i + "][" + j + "] is not among the most precious: you must remove the most valuable item first.");
                    }
                    // provo a rimuovere item: se loseItem lancia eccezione, la raccolgo e la rilancio con contesto affinchè venga gestita meglio dal controller
                    try {
                        board.getContainers().get(index).loseItem(board.getContainers().get(index).getItems().get(item));
                        countGood ++;
                    } catch (ContainerException e) {
                        throw new CardException("Cannon remove precious item in Container at Ship[" + i + "][" + j + "]:" + e.getMessage());
                    }
                }
                default -> throw new CardException("Component at [" + i + "][" + j + "] is not a container");
            }
        }
        else if(countGood == goodsLost){
            throw new CardException("Le merci da perdere sono esaurite!");
        }
        else if(!Game.getInstance().getCurrentPlayer().equals(playerMin)){
            throw new CardException("Il player che ha meno potenza motrice è" + playerMin);
        }
        else if(penalties.get(1) == Challenge.Members){
            throw new CardException("La tua penalità è perdere" + getMembersLost() + "membri dell'equipaggio!" );
        }
    }


    @Override
    public Object call(Visitor visitor){
        return visitor.visitForCombatZone(this);
    }

    @Override
    public Object call(VisitorParametrico visitorParametrico, int index){
        if(index < 1 || index > 3) {
            throw new IndexOutOfBoundsException("Eccezione lanciata nel metodo setIndex presente in combatZone");
        }
        return visitorParametrico.visitForCombatZone(this, index);
    }

    public void initPlay(){
        Game.getInstance().setGameStatus(GameStatus.INIT_COMBATZONE);
        Game.getInstance().fireEvent(new EventForCombatZone(Game.getInstance().getGameStatus(), daysLost, goodsLost, membersLost, penalties, cannonShot));
    }

   public void play() {
        int impactLine;
        int pos;
        int size = Game.getInstance().getPlayers().size();
        List<Player> players = Game.getInstance().getPlayers();
        int tmp = -1;
        Player playerTmp;
        //inizio prima sfida
        if (penalties.get(0) == Challenge.Members) {
            playerTmp = findMinMembers();
            Utility.updatePosition(players, players.indexOf(playerTmp), -daysLost);
        } else if (penalties.get(0) == Challenge.CannonStrength) {
            playerTmp = findMinCannonStrength();
            Utility.updatePosition(players, players.indexOf(playerTmp), -daysLost);
        } else {
            throw new CardException("Eccezione in combatzone ");
        }

        //inizio seconda sfida
        playerMin = findMinEngineStrength().getNickname();

        //inizio terza sfida
        if (penalties.get(2) == Challenge.Members) {
            playerTmp = findMinMembers();
            int i = 0;
            for (CannonShot c : cannonShot) {
                impactLine = Utility.roll2to12();
                playerTmp.getTruck().handleCannonShot(c, impactLine);
                i++;
            }


        } else if (penalties.get(2) == Challenge.CannonStrength) {
            playerTmp = findMinCannonStrength();
            int i = 0;
            for (CannonShot c : cannonShot) {
                impactLine = Utility.roll2to12();
                playerTmp.getTruck().handleCannonShot(c, impactLine);
                i++;
            }
        } else {
            throw new CardException("Eccezione in combatzone");
        }
        endPlay();
    }

    public void endPlay(){
        Game.getInstance().setGameStatus(GameStatus.END_COMBATZONE);
    }
}
