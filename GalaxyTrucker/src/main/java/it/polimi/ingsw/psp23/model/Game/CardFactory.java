package it.polimi.ingsw.psp23.model.Game;

import it.polimi.ingsw.psp23.model.cards.Card;

import java.util.ArrayList;

// Utility Class che istanzia tutte le carte
public class CardFactory {

    // costruttore private e vuoto, così impedisco istanziazione: le carte vengono create una sola volta
    private CardFactory() {};

    public static ArrayList<Card> generateLevel1Cards() {
        ArrayList<Card> cardsOne = new ArrayList<>();



        return cardsOne;
    }

    public static ArrayList<Card> generateLevel2Cards() {
        ArrayList<Card> cardsTwo = new ArrayList<>();




        return cardsTwo;
    }
}
