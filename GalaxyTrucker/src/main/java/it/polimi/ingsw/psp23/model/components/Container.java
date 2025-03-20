package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import java.util.ArrayList;
import java.util.List;

public class Container extends Component {
    // Danilo
    private final int size;
    private Color colorContainer;
    private ArrayList<Item> goods;

    Container(String type, Side up, Side down, Side left, Side right, int size, Color colorContainer, ArrayList<Item> goods) {
        super("Container", up,down,left,right);
        this.size = size;
        this.colorContainer = colorContainer;
        this.goods = goods;
    }

    public List<Item> getItems(){
        return new ArrayList<Item>(goods);
    }

    public void loadItem(Item item){
        if(goods.size() < size ) {
            if(colorContainer == Color.Red && (item.getItemColor() == Color.Red || item.getItemColor() == Color.Green) ||
            colorContainer == Color.Blue && !(item.getItemColor() == Color.Red))
                goods.add(item);
            else
                System.out.println("You cannot add this item here");
        }else{
            System.out.println("The size of goods is greater than the size of size " + size);
        }
    }

    public void loseItem(Item item){
        goods.remove(item); //qui la remove effettua un confronto tra gli attributi di item che in realtà è solo color
                            // e ne rimuove la prima occorrenza
    }
}

