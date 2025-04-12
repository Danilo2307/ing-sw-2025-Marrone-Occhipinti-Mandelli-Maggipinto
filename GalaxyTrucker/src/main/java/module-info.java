module it.polimi.ingsw.psp23 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires java.rmi;

    opens it.polimi.ingsw.psp23 to javafx.fxml;
    exports it.polimi.ingsw.psp23;
    exports it.polimi.ingsw.psp23.model.cards;
    opens it.polimi.ingsw.psp23.model.cards to javafx.fxml;
    exports it.polimi.ingsw.psp23.model.components;
    opens it.polimi.ingsw.psp23.model.components to javafx.fxml;
    exports it.polimi.ingsw.psp23.model.enumeration;
    opens it.polimi.ingsw.psp23.model.enumeration to javafx.fxml;
}