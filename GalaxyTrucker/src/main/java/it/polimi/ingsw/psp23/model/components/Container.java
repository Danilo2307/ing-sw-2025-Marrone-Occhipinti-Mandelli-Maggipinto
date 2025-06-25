package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.exceptions.ContainerException;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.helpers.Item;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import java.util.ArrayList;
import java.util.List;


/**
 * The Container class represents a specialized component that contains items.
 * It manages a collection of items while adhering to specific size and color constraints.
 *
 * The container is immutable in terms of its size and color properties; however, its content
 * (items) can be modified by addition or removal. Attempts to modify the contained items outside
 * the allowed conditions or constraints will result in exceptions.
 *
 * The color of the container enforces additional constraints on which items can be loaded based
 * on their color.
 */
public final class Container extends Component {
    private final int size;
    private final Color colorContainer;
    private final ArrayList<Item> goods;   // il riferimento è immutabile, ma posso fare add/remove

    public Container(Side up, Side down, Side left, Side right, int size, Color colorContainer, ArrayList<Item> goods, int id) {
        super(up,down,left,right, id);
        this.size = size;
        this.colorContainer = colorContainer;
        this.goods = goods;
    }

    // ritorno una copia di goods al fine di evitare side-effects esterni. Principio Encapsulation OOP.
    public List<Item> getItems(){
        return new ArrayList<>(goods);
    }

    /**
     * Determines whether the given item can be loaded into the container.
     * The method checks if the container has enough space and whether the item's color
     * is compatible with the container's color settings.
     *
     * @param item the item to be checked for loading into the container
     * @return true if the item can be loaded into the container, false otherwise
     */
    // disaccoppio logica controllo inserimento dall'inserimento. Principio single responsibility OOP.
    public boolean canLoadItem(Item item) {
        if (goods.size() >= size) return false;
        else if(colorContainer == Color.Red){
            if(item.getColor() != Color.Green && item.getColor() != Color.Red){
                return false;
            }
        }
        else if(colorContainer == Color.Blue){
            if(item.getColor() != Color.Green && item.getColor() != Color.Yellow && item.getColor() != Color.Blue){
                return false;
            }
        }
        return true;
    }

    /**
     * Attempts to load the specified item into the container. If the item cannot
     * be loaded due to insufficient space or incompatible color, a ContainerException is thrown.
     *
     * @param item the item to be loaded into the container
     * @throws ContainerException if the container is full or the item's color
     *                             is incompatible with the container's color settings
     */
    public void loadItem(Item item){
        if (canLoadItem(item)) {
            goods.add(item);
        }
        else
            // eccezione che verrà raccolta nel model e gestita nel controller con un try-catch (se scegliamo di dare 2^ chance al player)
            throw new ContainerException("Cannot load item: either the container is full or the color is not allowed.");
    }

    /**
     * Removes the specified item from the container. If the container is empty
     * or the item is not found within the container, a ContainerException is thrown.
     *
     * @param item the item to be removed from the container
     * @throws ContainerException if the container is empty or the item
     *                             is not found in the container
     */
    public void loseItem(Item item){
        if (goods.isEmpty())
            throw new ContainerException("Cannot remove: container is empty.");
        if (!goods.remove(item))
            throw new ContainerException("Cannot remove: item not found in container.");
    }


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

