package it.polimi.ingsw.psp23;

public class Player {
    private final String nickname;
    private int position ;
    private Board truck;
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
    }




}
