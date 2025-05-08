package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.exceptions.ContainerException;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import java.util.ArrayList;
import java.util.List;

public final class Container extends Component {
    // Danilo
    private final int size;
    private final Color colorContainer;
    private final ArrayList<Item> goods;   // il riferimento è immutabile, ma posso fare add/remove

    public Container(Side up, Side down, Side left, Side right, int size, Color colorContainer, ArrayList<Item> goods) {
        super(up,down,left,right);
        this.size = size;
        this.colorContainer = colorContainer;
        this.goods = goods;
    }

    // ritorno una copia di goods al fine di evitare side-effects esterni. Principio Encapsulation OOP.
    public List<Item> getItems(){
        return new ArrayList<>(goods);
    }

    // disaccoppio logica controllo inserimento dall'inserimento. Principio single responsibility OOP.
    public boolean canLoadItem(Item item) {
        if (goods.size() >= size) return false;

        return (colorContainer == Color.Red && (item.getColor() == Color.Red || item.getColor() == Color.Green)) ||
                (colorContainer == Color.Blue && item.getColor() != Color.Red);
    }

    public void loadItem(Item item){
        if (canLoadItem(item)) {
            goods.add(item);
        }
        else
            // eccezione che verrà raccolta nel model e gestita nel controller con un try-catch (se scegliamo di dare 2^ chance al player)
            throw new ContainerException("Cannot load item: either the container is full or the color is not allowed.");
    }

    public void loseItem(Item item){
        if (goods.isEmpty())
            throw new ContainerException("Cannot remove: container is empty.");
        if (!goods.remove(item))
            throw new ContainerException("Cannot remove: item not found in container.");
    }

    // NOTA: il ribilanciamento delle merci sarà gestito nel controller tramite continui loadItem e loseItem a scelta dell'utente

    public Color getColor(){
        return colorContainer;
    }

    public int getSize(){
        return size;
    }

    @Override
    public String toSymbol() {
        return this.getColor() == Color.Red ? "Cr" : "Cb";
    }

    @Override
    public String getInfo() {
        return "Container di colore "+ this.getColor() + " e capacità " + this.getSize()
                + ". Contiene le seguenti merci: " + this.getItems() + "\n";
    }
}

