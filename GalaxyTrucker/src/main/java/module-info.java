module it.polimi.ingsw.psp23 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens it.polimi.ingsw.psp23 to javafx.fxml;
    exports it.polimi.ingsw.psp23;
}