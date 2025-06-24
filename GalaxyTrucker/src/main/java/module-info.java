module it.polimi.ingsw.psp23 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;

    // (if you have other reflective needs you can open more packages similarly)

    requires org.controlsfx.controls;
    requires java.desktop;
    requires jdk.jfr;
    requires jdk.jshell;
    requires java.smartcardio;

    opens it.polimi.ingsw.psp23 to javafx.fxml;
    // allow code in other modules to compile‐against your RMI interfaces:
    exports it.polimi.ingsw.psp23.network.rmi;
    // open the package reflectively to the RMI runtime:
    opens it.polimi.ingsw.psp23.network.rmi to java.rmi;
    exports it.polimi.ingsw.psp23;
    exports it.polimi.ingsw.psp23.model.cards;
    exports it.polimi.ingsw.psp23.exceptions;
    opens it.polimi.ingsw.psp23.model.cards to javafx.fxml;
    exports it.polimi.ingsw.psp23.model.components;
    opens it.polimi.ingsw.psp23.model.components to javafx.fxml;
    exports it.polimi.ingsw.psp23.model.enumeration;
    opens it.polimi.ingsw.psp23.model.enumeration to javafx.fxml;
    exports it.polimi.ingsw.psp23.model.Game;
    opens it.polimi.ingsw.psp23.view.gui.guicontrollers to javafx.fxml;
    opens it.polimi.ingsw.psp23.view.gui to javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.psp23.model.Game to javafx.fxml;
    exports it.polimi.ingsw.psp23.model.cards.visitor;
    opens it.polimi.ingsw.psp23.model.cards.visitor to javafx.fxml;

}