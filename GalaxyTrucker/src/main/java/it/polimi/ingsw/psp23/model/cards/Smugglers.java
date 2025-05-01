package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.exceptions.ContainerException;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.Container;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.Challenge;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Smugglers adventure card, where players compare
 * cannon strength against the smugglers and win prizes or suffer penalties.
 */
public class Smugglers extends Card {

    /** Required cannon strength to defeat the smugglers. */
    private final int firePower;
    /** Number of items the smugglers steal from a losing player. */
    private final int numItemsStolen;
    /** Number of flight days lost when claiming the reward. */
    private final int days;
    /** List of items awarded to the winning player. */
    private final List<Item> prize;
    /** Nickname of the winning player, if any. */
    private String winner = null;
    /** Nicknames of the losing players. */
    private List<String> losers = new ArrayList<>();
    private int loadedCount;
    private int losedCount;

    /**
     * Constructs a Smugglers card with specified level and parameters.
     *
     * @param level the adventure difficulty level
     * @param firePower the smugglers' firepower
     * @param numItemsStolen the number of items stolen on defeat
     * @param days the days lost when claiming the prize
     * @param prize list of items awarded to the victor
     */
    public Smugglers(int level, int firePower, int numItemsStolen, int days, List<Item> prize) {
        super(level);
        this.firePower = firePower;
        this.numItemsStolen = numItemsStolen;
        this.days = days;
        this.prize = prize;
        this.loadedCount = 0;
        this.losedCount = 0;
    }

    /** @return the smugglers' firepower threshold */
    public int getFirePower() {
        return firePower;
    }

    /** @return number of items the smugglers will steal on defeat */
    public int getNumItemsStolen() {
        return numItemsStolen;
    }

    /** @return number of flight days lost when claiming the prize */
    public int getDays() {
        return days;
    }

    /**
     * Returns a copy of the prize list for the winner.
     *
     * @return list of prize items
     */
    public List<Item> getPrize() {
        return new ArrayList<>(prize);
    }

    public void loadGoods(int i, int j){
        if(loadedCount < prize.size() && Game.getInstance().getCurrentPlayer().equals(winner)){
            if(loadedCount == 0){
                Utility.updatePosition(Game.getInstance().getPlayers(), Game.getInstance().getCurrentPlayerIndex(), -days);
            }
            Board board = Game.getInstance().getCurrentPlayer().getTruck();
            Component[][] ship = board.getShip();
            Component tile = ship[i][j];
            switch (tile) {
                case Container container -> {
                    int index = board.getContainers().indexOf(container);
                    if (index == -1) {
                        throw new CardException("Container not found in 'containers' list: error in loadGoods of Board");
                    }
                    try {
                        // loadItem controlla anche se l'item può essere caricato in quello specifico container
                        board.getContainers().get(index).loadItem(prize.get(loadedCount));
                        loadedCount ++;

                    } catch (CardException c) {
                        // Rilancio una ContainerException con maggior contesto, da gestire poi nel Controller
                        throw new CardException("Item at index cannot be loaded in container at [" + i + "][" + j + "]: " + c.getMessage());
                    }

                }
                default -> throw new CardException("Component at ["+i+"]["+j+"] is not a container");
            }
        }
        else{
            throw new CardException("Merci esaurite");
        }
    }

    public void removePreciousItem(int i, int j, int item) {
        if(losedCount < numItemsStolen && losers.contains(Game.getInstance().getCurrentPlayer())){
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
                        losedCount ++;
                    } catch (ContainerException e) {
                        throw new CardException("Cannon remove precious item in Container at Ship[" + i + "][" + j + "]:" + e.getMessage());
                    }
                }
                default -> throw new CardException("Component at [" + i + "][" + j + "] is not a container");
            }
        }
        else if(losedCount == numItemsStolen){
            throw new CardException("Le merci da perdere sono esaurite!");
        }
        else if(!losers.contains(Game.getInstance().getCurrentPlayer())){
            throw new CardException("Il player non è tra i perdenti");
        }
    }

    /**
     * Accepts a visitor for processing this card's logic.
     *
     * @param visitor the visitor handling Smugglers card
     * @return result from the visitor
     */

    public Object call(Visitor visitor) {
        return visitor.visitForSmugglers(this);
    }

    /**
     * Initializes play by firing an event with parameters for this card.
     */
    public void initPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_SMUGGLERS);
        game.fireEvent(new Event(
                game.getGameStatus(),
                firePower, numItemsStolen, days, prize
        ));
    }

    /**
     * Executes the encounter: players compare their cannon strength
     * to the smugglers' firepower in order of flight. The first
     * player to exceed the firepower wins; all who fail are recorded
     * as losers. Ties allow the smugglers to advance to the next player.
     *
     */
    public void play() {
        List<Player> players = Game.getInstance().getPlayers();
        for (Player p : players) {
            if (p.getTruck().calculateCannonStrength() > firePower) {
                winner = p.getNickname();
                break;
            } else if (p.getTruck().calculateCannonStrength() < firePower) {
                losers.add(p.getNickname());
            }
        }
        endPlay();
    }

    /**
     * Ends the Smugglers encounter and updates the game status to
     * allow the winner to load goods or losers to pick most important goods.
     */
    public void endPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.END_SMUGGLERS);
    }

}
