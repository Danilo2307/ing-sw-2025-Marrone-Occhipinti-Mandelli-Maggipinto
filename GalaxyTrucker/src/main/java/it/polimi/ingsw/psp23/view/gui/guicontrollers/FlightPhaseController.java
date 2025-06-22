package it.polimi.ingsw.psp23.view.gui.guicontrollers;
import it.polimi.ingsw.psp23.protocol.request.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import it.polimi.ingsw.psp23.network.Client;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FlightPhaseController {
    private Client client;
    @FXML Button button1;
    @FXML Button button2;
    @FXML Button button3;
    @FXML Button button4;
    @FXML Button button5;
    @FXML Button button6;
    @FXML Button button7;
    @FXML ImageView card;
    @FXML Button drawBtn;
    @FXML Label textLabel;
    @FXML StackPane ship;
    @FXML GridPane shipGrid;

    private SingleTileSelector singleSelector = null;
    private TwoStepsTileSelector doubleSelector = null;


    public void setClient(Client client) {
        this.client = client;
    }

    public Button getButton1() {
        return button1;
    }
    public Button getButton2() {
        return button2;
    }
    public Button getButton3() {
        return button3;
    }
    public Button getButton4() {
        return button4;
    }
    public Button getButton5() {
        return button5;
    }
    public Button getButton6() {
        return button6;
    }
    public Button getDrawBtn() { return drawBtn; }
    public Label getTextLabel(){
        return textLabel;
    }

    public void setShip(StackPane ship, GridPane shipGrid){
        this.ship.getChildren().clear();
        this.ship.getChildren().add(ship);
        this.shipGrid = shipGrid;
    }

    public void installClickHandlers() {
        for (Node tile: shipGrid.getChildren()) {
            tile.setOnMouseClicked(event -> {
                // prendo le coordinate del click
                Integer r = GridPane.getRowIndex(tile);
                Integer c = GridPane.getColumnIndex(tile);
                // se null allora è 0
                int row = r == null ? 0 : r;
                int col = c == null ? 0 : c;

                // utente ha cliccato bottone che prevede un click sulla ship
                if (singleSelector != null) {
                    singleSelector.handleClick(row, col);
                }
                /// OSS: ogni volta che invio azione devo rimettere i selector a null, così da resettare e permettere nuovi click
                // utente ha cliccato bottone che prevede doppio sulla ship (attivazioni)
                else if (doubleSelector != null) {
                    doubleSelector.handleClick(row, col);
                }

                // se non è stata selezionata alcuna modalità --> info
                else {
                    try {
                        client.sendAction(new RequestTileInfo(row, col));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public void setCardImage(int id){
        String imagePath = "/it/polimi/ingsw/psp23/images/cards/" + id + ".jpg";
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        card.setImage(image);
    }

    @FXML
    public void onLeaveClicked() throws RemoteException {
        client.sendAction(new LeaveFlight());
    }

    @FXML
    public void onFlightBoardClicked(){
        try {
            client.sendAction(new RequestFlightBoard());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void drawCard(){
        try {
            client.sendAction(new DrawCard());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void enable(Button button){
        button.setVisible(true);
        button.setManaged(true);
    }

    public void disable(Button button){
        button.setVisible(false);
        button.setManaged(false);
    }

    public void disableAllButtons() {
        disable(button1);
        disable(button2);
        disable(button3);
        disable(button4);
        disable(button5);
        disable(button6);
        disable(button7);
    }


    private void setupPassBtn(Button pass) {
        pass.setText("Passa");
        enable(pass);
        pass.setOnAction(e -> {
            try {
                client.sendAction(new NextTurn());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });

    }

    private void setupLoadGoodBtn(Button load) {
        load.setText("Carica merce");
        enable(load);
        load.setOnAction(e -> {
            singleSelector = new SingleTileSelector((container) -> {
                try {
                    client.sendAction(new LoadGood(container.x(), container.y()));
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                } finally {
                    singleSelector = null;
                }
            });
        });
    }

    private void setupDropGoodBtn(Button drop) {
        drop.setText("Scarica merce");
        enable(drop);
        drop.setOnAction(e -> {
            singleSelector = new SingleTileSelector((container) -> {
                int x = container.x();
                int y = container.y();

                List<Integer> choices = List.of(1,2,3);
                ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, choices);
                dialog.setTitle("Scarica merce");
                dialog.setHeaderText("Scegli la merce da rimuovere");
                dialog.setContentText("Indice: ");

                dialog.showAndWait().ifPresent(idx -> {
                    try {
                        client.sendAction(new LoseGood(x,y, idx));
                    }
                    catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    finally {
                        singleSelector = null;
                    }
                });
            });
        });
    }

    private void setupLandBtn(Button land, int planetIndex) {
        enable(land);
        land.setOnAction(e -> {
            try {
                client.sendAction(new Land(planetIndex));
            }
            catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void setupReadyBtn(Button ready) {
        ready.setText("Pronto");
        enable(ready);
        ready.setOnAction(e -> {
            try {
                client.sendAction(new Ready());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void setupCannonBtn(Button activeCannon) {
        activeCannon.setText("Attiva cannone");
        enable(activeCannon);

        activeCannon.setOnAction(e -> {
            doubleSelector = new TwoStepsTileSelector((cannon, battery) -> {
                int cx = cannon.x();
                int cy = cannon.y();
                int bx = battery.x();
                int by = battery.y();
                try {
                    client.sendAction(new ActivateCannon(cx,cy,bx,by));
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
                finally {
                    doubleSelector = null;
                }
            });
        });
    }

    private void setupShieldBtn(Button s) {
        s.setText("Attiva scudo");
        enable(s);

        s.setOnAction(e -> {
            doubleSelector = new TwoStepsTileSelector((shield, battery) -> {
                int sx = shield.x();
                int sy = shield.y();
                int bx = battery.x();
                int by = battery.y();
                try {
                    client.sendAction(new ActivateShield(sx,sy,bx,by));
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
                finally {
                    doubleSelector = null;
                }
            });
        });
    }

    private void setupEngineBtn(Button e) {
        e.setText("Attiva motore");
        enable(e);

        e.setOnAction(event -> {
            // creo il selector
            doubleSelector = new TwoStepsTileSelector( (engine, battery) -> {
                // callback dopo i 2 click
                int ex = engine.x();
                int ey = engine.y();
                int bx = battery.x();
                int by = battery.y();
                try {
                    // invio azione e resetto il selettore
                    client.sendAction(new ActivateEngine(ex, ey, bx, by));
                }
                catch (RemoteException exception) {
                    exception.printStackTrace();
                }
                finally {
                    doubleSelector = null;
                }
            });
        });
    }

    /// volendo, si potrebbe creare un "annulla" cioè permettere all'utente di cliccare "attiva cannone" e poi "attiva scudo" e
    /// prenderebbe i comandi di "attiva scudo" . Basterebbe mettere i selettori a null prima di creare quello nuovo

    public void openSpaceCommands() {
        setupEngineBtn(button1);
        setupReadyBtn(button2);
    }

    public void planetsCommands(int id) {
        button1.setText("Atterra Pianeta 1");
        button2.setText("Atterra Pianeta 2");
        button3.setText("Atterra Pianeta 3");
        button4.setText("Atterra Pianeta 4");

        switch (id) {
            // 4 pianeti
            case 112, 206 -> {
                setupLandBtn(button1, 1);
                setupLandBtn(button2, 2);
                setupLandBtn(button3, 3);
                setupLandBtn(button4, 4);

                setupPassBtn(button5);
                setupLoadGoodBtn(button6);
                setupDropGoodBtn(button7);

            }

            // 3 pianeti
            case 102, 114, 204, 207 -> {
                setupLandBtn(button1, 1);
                setupLandBtn(button2, 2);
                setupLandBtn(button3, 3);

                setupPassBtn(button4);
                setupLoadGoodBtn(button5);
                setupDropGoodBtn(button6);

            }

            // 2 pianeti
            case 113, 205 -> {
                setupLandBtn(button1, 1);
                setupLandBtn(button2, 2);

                setupPassBtn(button3);
                setupLoadGoodBtn(button4);
                setupDropGoodBtn(button5);

            }
        }
    }

    public void startdustCommands() {
        textLabel.setText("Non puoi fare nulla: perderai giorni di volo in base al numero di connettori esposti");
    }


    public void abandonedshipCommands() {
        button1.setText("Compra nave");
        enable(button1);

        button1.setOnAction(e -> {
            try {
                client.sendAction(new BuyShip());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });

        setupPassBtn(button2);

        button3.setText("Riduci equipaggio");
        enable(button3);
        button3.setOnAction(e -> {
            // creo selettore per ridurre
            singleSelector = new SingleTileSelector(coord -> {
                int x = coord.x();
                int y = coord.y();
                try {
                    // invio azione e resetto selettore
                    client.sendAction(new ReduceCrew(x, y, 1));
                    singleSelector = null;
                }
                catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            });
        });
    }

    public void slaversCommands() {

        setupCannonBtn(button1);
        setupReadyBtn(button2);

        button3.setText("Riduci equipaggio");
        enable(button3);
        button3.setOnAction(e -> {
            singleSelector = new SingleTileSelector(coord -> {
                int x = coord.x();
                int y = coord.y();

                try {
                    client.sendAction(new ReduceCrew(x,y,1));
                    singleSelector = null;
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });

        setupPassBtn(button4);

        button5.setText("Prendi i crediti");
        enable(button5);
        button5.setOnAction(e -> {
            try {
               client.sendAction(new EarnCredits());
            } catch (RemoteException ex) {
               ex.printStackTrace();
            }
        });

    }

    public void meteorSwarmCommands() {
        setupCannonBtn(button1);
        setupShieldBtn(button2);
        setupReadyBtn(button3);

    }

    public void epidemicCommands() {
        textLabel.setText("Non puoi fare nulla: perderai 1 membro dell'equipaggio per ogni cabina contagiata");
    }

    public void piratesCommands() {

        setupCannonBtn(button1);
        setupReadyBtn(button2);
        setupShieldBtn(button3);
        setupPassBtn(button4);

        button5.setText("Prendi crediti");
        enable(button5);
        button5.setOnAction(e -> {
            try {
                client.sendAction(new EarnCredits());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
    }


    public void abandonedStationCommands() {
        button1.setText("Attracca");
        enable(button1);
        button1.setOnAction(e -> {
            try {
                client.sendAction(new DockStation());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });

        // passa turno
        setupPassBtn(button2);
        //  carica o scarica merce
        setupLoadGoodBtn(button3);
        setupDropGoodBtn(button4);

    }


}
