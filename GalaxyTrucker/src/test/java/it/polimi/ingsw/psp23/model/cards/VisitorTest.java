package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.model.enumeration.Challenge;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Direction;

import java.util.ArrayList;
import java.util.List;

public class VisitorTest {
    public static void main(String[] args) {

        Visitor<Integer> visitor1 = new GetFirePowerVisitor();
        Visitor<Integer> visitor2 = new GetNumItemsVisitor();
        Visitor<Integer> visitor3 = new GetDaysVisitor();
        Visitor<List<List<Item>>> visitor4 = new GetListVisitor();
        Visitor<boolean[]> visitor5 = new GetArrayVisitor();
        VisitorParametrico<Challenge> visitorParametrico1 = new GetPenaltyVisitor();

        Item item1 = new Item(Color.Red);
        Item item2 = new Item(Color.Blue);
        Item item3 = new Item(Color.Green);

        List<Item> prizes1 = new ArrayList<Item>();
        prizes1.add(item1);
        prizes1.add(item2);
        prizes1.add(item3);

        List<Item> prizes2 = new ArrayList<>();
        prizes2.add(item3);
        prizes2.add(item2);
        prizes2.add(item1);

        List<Item> planetPrizes1 = new ArrayList<>(prizes1);
        List<Item> planetPrizes2 = new ArrayList<>(prizes2);

        List<List<Item>> pianeti = new ArrayList<>();
        pianeti.add(planetPrizes1);
        pianeti.add(planetPrizes2);

        CannonShot cannonata1 = new CannonShot(true, Direction.DOWN);
        CannonShot cannonata2 = new CannonShot(false, Direction.UP);

        List<CannonShot> cannonShotList1 = new ArrayList<>();
        cannonShotList1.add(cannonata1);
        cannonShotList1.add(cannonata2);

        Card carta1 = new Smugglers(2, 3, 3, 3, prizes1);
        Card carta2 = new Planets(2, 2, pianeti);
        Card carta3 = new CombatZone(2, 2, 3, 1, Challenge.CannonStrength, Challenge.Members, Challenge.EngineStrength, cannonShotList1);

        List<Card> deck = new ArrayList<>();
        deck.add(carta1);
        deck.add(carta2);
        deck.add(carta3);

        System.out.println("Stampa con deck e 0: " + deck.get(0).call(visitor3));
        System.out.println("Stampa con deck e 1: " + deck.get(1).call(visitor3));
        System.out.println("Stampa con deck e 2: " + deck.get(2).call(visitor3));

        System.out.println("Il livello della carta smugglers e': " + carta1.getLevel());
        System.out.println("La potenza di fuoco di smugglers e': " + carta1.call(visitor1));
        System.out.println("Il numero di merci di smugglers e': " + carta1.call(visitor2));
        System.out.println("I giorni da perdere si smugglers sono: " + carta1.call(visitor3));
        System.out.println("La lista con i premi di smugglers è composta da: " + carta1.call(visitor4));

        System.out.println("Il livello di planets è:" + carta2.getLevel());
        System.out.println("Il numero di giorni di planets è:" + carta2.call(visitor3));
        System.out.println("La lista coi pianeti di planets è:" + carta2.call(visitor4));
        System.out.print("L'array dei pianeti di planets è: ");
        for(boolean b : carta2.call(visitor5)){
            System.out.print(b + " ");
        }
        carta2.call(visitorParametrico1, 1);
        carta2.call(visitorParametrico1, 0);
        System.out.println("");
        for(boolean b : carta2.call(visitor5)){
            System.out.print(b + " ");
        }
        System.out.println();

        System.out.println("La prima penalità è: " + carta3.call(visitorParametrico1, 3));
        System.out.println("La prima penalità è: " + carta3.call(visitorParametrico1, 2));

    }
}
