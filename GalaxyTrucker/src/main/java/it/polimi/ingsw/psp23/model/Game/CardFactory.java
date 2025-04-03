package it.polimi.ingsw.psp23.model.Game;

import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.model.cards.*;
import it.polimi.ingsw.psp23.model.cards.Planets;
import it.polimi.ingsw.psp23.model.enumeration.Challenge;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Direction;


import java.util.ArrayList;
import java.util.List;

// Utility Class che istanzia tutte le carte
public class CardFactory {

    // costruttore private e vuoto, così impedisco istanziazione: le carte vengono create una sola volta
    private CardFactory() {};

    public static ArrayList<Card> generateLevel1Cards() {
        ArrayList<Card> cardsOne = new ArrayList<>();

        // openspace
        cardsOne.add(new OpenSpace(1));
        cardsOne.add(new OpenSpace(1));
        cardsOne.add(new OpenSpace(1));
        cardsOne.add(new OpenSpace(1));

        // planets: List.of crea liste immutabili (in linea con il loro scopo)
        cardsOne.add(new Planets(1,3, List.of(List.of(new Item(Color.Red), new Item(Color.Green), new Item(Color.Blue), new Item(Color.Blue), new Item(Color.Blue)), List.of(new Item(Color.Red), new Item(Color.Yellow), new Item(Color.Blue)), List.of(new Item(Color.Red), new Item(Color.Blue), new Item(Color.Blue), new Item(Color.Blue)), List.of(new Item(Color.Red), new Item(Color.Green)))));
        cardsOne.add(new Planets(1,2, List.of(List.of(new Item(Color.Red), new Item(Color.Red)), List.of(new Item(Color.Red), new Item(Color.Blue), new Item(Color.Blue)), List.of(new Item(Color.Yellow)))));
        cardsOne.add(new Planets(1,3, List.of(List.of(new Item(Color.Yellow), new Item(Color.Green), new Item(Color.Blue), new Item(Color.Blue)), List.of(new Item(Color.Yellow), new  Item(Color.Yellow)))));
        cardsOne.add(new Planets(1,1, List.of(List.of(new Item(Color.Green), new Item(Color.Green)), List.of(new Item(Color.Yellow)), List.of(new Item(Color.Blue), new Item(Color.Blue), new Item(Color.Blue)))));

        // Abandoned ship
        cardsOne.add(new AbandonedShip(1,1,3,2));
        cardsOne.add(new AbandonedShip(1,1,4,3));

        // Abandoned Station
        cardsOne.add(new AbandonedStation(1,1,5,List.of(new Item(Color.Yellow), new Item(Color.Green))));
        cardsOne.add(new AbandonedStation(1,1,6,List.of(new Item(Color.Red), new Item(Color.Red))));

        // Stardust
        cardsOne.add(new Stardust(1));

        // Slavers
        cardsOne.add(new Slavers(1, 6, 3, 5,1));

        // Smugglers
        cardsOne.add(new Smugglers(1, 4, 2, 1, List.of(new Item(Color.Yellow), new Item(Color.Green), new Item(Color.Blue))));

        // Pirates
        cardsOne.add(new Pirates(1, 4, 1, 5, List.of(new CannonShot(false, Direction.UP), new CannonShot(true, Direction.UP), new CannonShot(false, Direction.UP))));

        // Meteor Swarm: ordine creazione lista meteoriti: dall'alto in basso e da sx a dx
        cardsOne.add(new MeteorSwarm(1, List.of(new Meteor(true, Direction.UP), new Meteor(false, Direction.LEFT), new Meteor(false, Direction.RIGHT))));
        cardsOne.add(new MeteorSwarm(1, List.of(new Meteor(false, Direction.UP), new Meteor(false, Direction.UP), new Meteor(false, Direction.LEFT), new Meteor(false, Direction.RIGHT), new Meteor(false, Direction.DOWN))));
        cardsOne.add(new MeteorSwarm(1, List.of(new Meteor(true, Direction.UP), new Meteor(false, Direction.UP), new Meteor(true, Direction.UP))));

        // warzone
        cardsOne.add(new CombatZone(1,3,0,2, Challenge.Members, Challenge.EngineStrength, Challenge.CannonStrength, List.of(new CannonShot(false, Direction.DOWN), new CannonShot(true, Direction.DOWN))));

        return cardsOne;
    }

    public static ArrayList<Card> generateLevel2Cards() {
        ArrayList<Card> cardsTwo = new ArrayList<>();

        // openspace
        cardsTwo.add(new OpenSpace(2));
        cardsTwo.add(new OpenSpace(2));
        cardsTwo.add(new OpenSpace(2));

        // planets
        cardsTwo.add(new Planets(2,4, List.of(List.of(new Item(Color.Red), new Item(Color.Red), new Item(Color.Red), new Item(Color.Yellow)), List.of(new Item(Color.Red),new Item(Color.Red), new Item(Color.Green), new Item(Color.Green)), List.of(new Item(Color.Red), new Item(Color.Blue), new Item(Color.Blue), new Item(Color.Blue), new Item(Color.Blue)))));
        cardsTwo.add(new Planets(2,3, List.of(List.of(new Item(Color.Red), new Item(Color.Red)), List.of(new Item(Color.Green), new Item(Color.Green), new Item(Color.Green),new Item(Color.Green)))));
        cardsTwo.add(new Planets(2,2, List.of(List.of(new Item(Color.Red), new Item(Color.Yellow)), List.of(new Item(Color.Yellow), new Item(Color.Green), new Item(Color.Blue)), List.of(new Item(Color.Green), new Item(Color.Green)), List.of(new Item(Color.Yellow)))));
        cardsTwo.add(new Planets(2,3, List.of(List.of(new Item(Color.Green), new Item(Color.Green), new Item(Color.Green), new Item(Color.Green)), List.of(new Item(Color.Yellow), new Item(Color.Yellow)), List.of(new Item(Color.Blue), new Item(Color.Blue), new Item(Color.Blue), new Item(Color.Blue)))));

        // abandoned ship
        cardsTwo.add(new AbandonedShip(2, 1, 6, 4));
        cardsTwo.add(new AbandonedShip(2, 2, 8, 5));

        // abandoned station
        cardsTwo.add(new AbandonedStation(2, 1, 7, List.of(new Item(Color.Red), new Item(Color.Yellow))));
        cardsTwo.add(new AbandonedStation(2, 2, 8, List.of(new Item(Color.Yellow), new Item(Color.Yellow), new Item(Color.Green))));

        // stardust
        cardsTwo.add(new Stardust(2));

        // slavers
        cardsTwo.add(new Slavers(2, 7, 4, 8, 2));

        // smugglers
        cardsTwo.add(new Smugglers(2, 8, 3, 1, List.of(new Item(Color.Red), new Item(Color.Yellow), new Item(Color.Yellow))));

        // pirates
        cardsTwo.add(new Pirates(2, 7, 2, 6, List.of(new CannonShot(true, Direction.UP), new CannonShot(false, Direction.UP), new CannonShot(true, Direction.UP))));

        // Meteor Swarm
        cardsTwo.add(new MeteorSwarm(2, List.of(new Meteor(false, Direction.UP), new Meteor(false, Direction.UP), new Meteor(true, Direction.LEFT), new Meteor(false, Direction.LEFT), new Meteor(false, Direction.LEFT))));
        cardsTwo.add(new MeteorSwarm(2, List.of(new Meteor(true, Direction.UP), new Meteor(true, Direction.UP), new Meteor(false, Direction.DOWN), new Meteor(false, Direction.DOWN))));
        cardsTwo.add(new MeteorSwarm(2, List.of(new Meteor(false, Direction.UP), new Meteor(false, Direction.UP), new Meteor(true, Direction.RIGHT), new Meteor(false, Direction.RIGHT), new Meteor(false, Direction.RIGHT))));

        // epidemic
        cardsTwo.add(new Epidemic(2));

        // combatzone
        cardsTwo.add(new CombatZone(2,4,3,0, Challenge.CannonStrength, Challenge.EngineStrength, Challenge.Members, List.of(new CannonShot(false, Direction.UP), new CannonShot(false, Direction.LEFT), new CannonShot(false, Direction.RIGHT), new CannonShot(true, Direction.DOWN))));

        return cardsTwo;
    }
}
