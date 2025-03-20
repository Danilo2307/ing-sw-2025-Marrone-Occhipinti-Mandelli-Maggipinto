package it.polimi.ingsw.psp23;

import it.polimi.ingsw.psp23.model.components.Component;
import java.util.Random;
import java.util.ArrayList;

public class Player {
    private final String nickname;
    private final int position ;
    private final Board truck;
    private int money;
    private boolean inGame;

    public Player(String nickname) {
        this.nickname = nickname;
        this.position = 0;
        this.truck = new Board();
        this.money = 0;
        this.inGame = true;
    }

    public boolean isInGame() {
        return inGame;
    }

    public Board getTruck() {
        return new Board(truck);
    }

    public void updateMoney(int moneyVariation) {
        money += moneyVariation;
    }

    public int getPosition() {
        return position;
    }

    public void leaveGame() {
        inGame = false;
        // il metodo sort() presente in Game provvederà a spostare il Player da players a playersNotInGame
     }

    public String getNickname() {
        return nickname;
    }

    public int getMoney() {
        return money;
    }

    //public setPosition(int finalOffset)  ATTENDO NEWS SU UPDATE_POSITION DA DANILO/ALBI

    public void buildTruck(ArrayList <Component> heap, ArrayList <Component> uncovered) {
        boolean hasFinished = false;

    // MANCA QUESTIONE COORDINAMENTO PER QUANDO IL PRIMO FINISCE E GIRA LA CLESSIDRA: GLI ALTRI PLAYER AVRANNO X TEMPO PER FINIRE
        while (!hasFinished()) {
            int pickingChoice = View.AskPickSource(); // 0 heap, 1 uncovered
            Component randomComponent;
            Random rand = new Random();

            // gestisco casi vuoti forzando pickingChoice al valore corretto (quello non vuoto)
            if (heap.isEmpty()) {
                pickingChoice = 1;
            }
            else if (uncovered.isEmpty()){
                pickingChoice = 0;
            }

            // pesco dall'heap
            if (pickingChoice == 0) {
                int index = rand.nextInt(heap.size());
                // sincronizzo su heap così che solo un Player alla volta possa rimuovere un component dal mucchio.
                synchronized (heap) {
                    randomComponent = heap.remove(index);  // rimuove dalla lista l'elemento heap(index) e lo restituisce
                }
                randomComponent.moveToHand();
            }
            // pesco dagli uncovered
            else {
                int index = rand.nextInt(uncovered.size());
                synchronized (uncovered) {
                    randomComponent = uncovered.remove(index);
                }
                randomComponent.moveToHand();
            }

            //ASK ALLA VIEW PER DECIDERE SE SALDARLO (choice=0) OPPURE RIMETTERLO NEL MUCCHIO SCOPERTO (choice=1)
            int choice = View.decideComponent();  // 0 saldo, 1 mucchio scoperto
            if (choice == 0) {
                // ricevi dalla view coordinate i,j su cui vuole saldarlo (eventualmente dopo aver chiamato su di esso vari rotate)
                truck.addComponent(randomComponent, i, j);
                randomComponent.placeOnTruck();    // setta lo stato del Component a ON_TRUCK
            }
            else {      // cambia stato component a FACE_UP
                randomComponent.discardFaceUp();
                synchronized (uncovered) {
                    uncovered.add(randomComponent);
                }
            }
        }
    }
}
