package it.polimi.ingsw.psp23.model.Game;
import java.util.Random;
import java.util.List;


/**
 * Utility is a final class that provides static helper methods for general-purpose utility 
 * operations, including random number generation and position updating logic. 
 * The class is designed to be non-instantiable and thread-safe.
 */
public final class Utility {
    // Creating a single instance (for efficiency and true randomness) of Java's Random class used to generate random numbers
    private static final Random rand = new Random();

    private Utility() {
    }

    /**
     * Simulates the rolling of two six-sided dices and returns the sum of the results.
     * This method generates two random integers between 1 (inclusive) and 6 (inclusive)
     * and adds them together to produce a value in the range of 2 to 12.
     *
     * @return an integer representing the sum of two randomly generated dice rolls,
     *         ranging from 2 to 12.
     */
    public static int roll2to12() {
        return rand.nextInt(6) + 1 + rand.nextInt(6) + 1;
    }


    /**
     * Generates a random integer in the range from 0 (inclusive) to the specified size (exclusive).
     * It is used to pick one random card from the heap.
     *
     * @param size the upper bound (exclusive) for the generated random integer
     * @return a random integer between 0 (inclusive) and size (exclusive)
     */
    public static int randomComponent(int size) {
        return rand.nextInt(0, size);
    }

    /**
     * Updates the position of a specific player in the provided list based on the specified number of positions to jump.
     * The method ensures that players do not move to positions already occupied by other players and doesn't count the position
     * when a player is already there.
     *
     * @param players the list of players participating in the game
     * @param playerIndex the index of the player in the list whose position is to be updated
     * @param positionsToJump the number of positions the player intends to move; can be positive or negative
     */
    public static void updatePosition(List<Player> players, int playerIndex, int positionsToJump) {
        Player giocatore = players.get(playerIndex);
        int playerLocation = giocatore.getPosition();
        int saltiEffettivi = positionsToJump;
        if (positionsToJump > 0) {
            for (int offset = 0; offset <= saltiEffettivi; offset++) {
                for (int k = 0; k < players.size(); k++) {
                    if (k != playerIndex && players.get(k).getPosition() == playerLocation + offset) {
                        saltiEffettivi++;
                        offset++;
                        // resetting k causes the cycle to restart in case of a collision, ensuring
                        // the current player is compared with ALL OTHER PLAYERS PRESENT
                        k=0;

                        /* TODO: bisogna attenzionare che non ci siano loop infiniti dovuto magari ad un accavallamento successivo delle pedine
                                 che potrebbe portare questo ciclo a non esaurirsi mai perchè trova sempre posizioni occupate dopo */
                    }
                }
            }
        } else {
            for (int offset = 0; offset >= saltiEffettivi; offset--) {
                for (int k = 0; k < players.size(); k++) {
                    if (k != playerIndex && players.get(k).getPosition() == playerLocation + offset) {
                        saltiEffettivi--;
                        offset--;
                        // resetting k causes the cycle to restart in case of a collision, ensuring
                        // the current player is compared with ALL OTHER PLAYERS PRESENT
                        k=0;
                        
                        /* TODO: bisogna attenzionare che non ci siano loop infiniti dovuto magari ad un accavallamento successivo delle pedine
                                 che potrebbe portare questo ciclo a non esaurirsi mai perchè trova sempre posizioni occupate dopo */
                    }
                }
            }
        }
        giocatore.setPosition(saltiEffettivi);
    }

}