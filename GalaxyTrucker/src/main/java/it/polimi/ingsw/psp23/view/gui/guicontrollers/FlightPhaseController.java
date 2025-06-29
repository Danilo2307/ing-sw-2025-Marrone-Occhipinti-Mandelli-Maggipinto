package it.polimi.ingsw.psp23.view.gui.guicontrollers;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.protocol.request.*;
import it.polimi.ingsw.psp23.view.gui.GuiApplication;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * The FlightPhaseController class manages the various flight phase functionalities
 * in a game. It provides methods to handle user interactions, control UI components,
 * and execute specific actions during the flight phase, such as enabling or disabling
 * buttons, setting ship configurations, and issuing commands. This class interacts
 * with a Client object to send and receive game-related actions and messages.
 */
public class FlightPhaseController {
    private Client client;
    @FXML Button button1;
    @FXML Button button2;
    @FXML Button button3;
    @FXML Button button4;
    @FXML Button button5;
    @FXML Button button6;
    @FXML Button button7;
    @FXML Button button8;
    @FXML ImageView card;
    @FXML Button drawBtn;
    @FXML Label textLabel;
    @FXML StackPane ship;
    @FXML GridPane shipGrid;

    @FXML Button refreshShipBtn;
    @FXML Button player1;
    @FXML Button player2;
    @FXML Button player3;

    private SingleTileSelector singleSelector = null;
    private TwoStepsTileSelector doubleSelector = null;


    public void setClient(Client client) {
        this.client = client;
    }
    public Button getDrawBtn() { return drawBtn; }
    public Label getTextLabel(){
        return textLabel;
    }

    /**
     * Sets the ship view and its corresponding grid representation from the building phase.
     *
     * @param ship the StackPane representing the graphical representation of the ship
     * @param shipGrid the GridPane representing the layout structure of the ship
     */
    public void setShip(StackPane ship, GridPane shipGrid){
        this.ship.getChildren().clear();
        this.ship.getChildren().add(ship);
        this.shipGrid = shipGrid;
    }

    /**
     * Installs click handlers for all tiles within the ship grid. Each tile in the grid
     * is assigned a mouse click event listener that performs specific actions based on
     * the current selection mode.
     *
     * When a tile is clicked:
     * - If a single selection handler is active, the tile's row and column coordinates
     *   are processed, and the single selection action is invoked.
     * - If a double selection handler is active, the click event is used to manage the
     *   two-step selection process, invoking the associated callback on the completion
     *   of the second click.
     * - If no selection mode is active, a request is sent to the server for detailed
     *   information about the clicked tile at the specified coordinates.
     *
     * The method resets the current selection handler to null after processing each
     * action, enabling subsequent clicks to trigger new selection actions.
     *
     * Exceptions:
     * - Catches and wraps any RemoteException occurring during server communication
     *   into a RuntimeException.
     */
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

    /**
     * Updates the displayed card image with the image corresponding to the provided card ID.
     * The image is loaded from the predefined card image resources directory.
     *
     * @param id the identifier of the card, used to determine the image file to load
     */
    public void setCardImage(int id){
        String imagePath = "/it/polimi/ingsw/psp23/images/cards/" + id + ".jpg";
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        card.setImage(image);
    }

    /**
     * Handles the user action of leaving the flight phase in the game.
     *
     * This method sends a {@link LeaveFlight} action to the server via the client object,
     * which informs the server that the user has chosen to exit the ongoing flight phase.
     * The server then processes the action, ensuring all necessary game state updates
     * and notifications are executed.
     *
     * @throws RemoteException if a communication error occurs during the action transmission.
     */
    @FXML
    public void onLeaveClicked() throws RemoteException {
        client.sendAction(new LeaveFlight());
    }

    /**
     * Handles the event triggered when the flight board button is clicked.
     * This method sends a {@code RequestFlightBoard} action to the server using the client object.
     * A server response with updated flight board information is expected.
     *
     * If a {@code RemoteException} occurs during communication with the server,
     * it is caught and wrapped into a {@code RuntimeException}.
     */
    @FXML
    public void onFlightBoardClicked(){
        try {
            client.sendAction(new RequestFlightBoard());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initiates the action to draw the next card in the game.
     *
     * This method sends a {@code DrawCard} action to the server using the client object.
     * It triggers the server to handle the logic required for drawing a card, which
     * includes game state validation and ensuring the appropriate player initiates the action.
     *
     * Exceptions:
     * - Catches and handles {@code RemoteException} to ensure smooth execution of the method.
     */
    @FXML
    public void drawCard(){
        try {
            client.sendAction(new DrawCard());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Enables the specified button by making it visible and managed within the UI layout.
     *
     * @param button the Button to be enabled, which will be set to visible and managed
     */
    public void enable(Button button){
        button.setVisible(true);
        button.setManaged(true);
    }

    /**
     * Disables the given button by making it invisible and unmanaged in the user interface.
     *
     * @param button the Button to be disabled
     */
    public void disable(Button button){
        button.setVisible(false);
        button.setManaged(false);
    }

    /**
     * Disables all associated buttons in the interface.
     * This method sequentially disables the buttons button1 through button7
     * by calling the {@link #disable(Button)} method for each button.
     */
    public void disableAllButtons() {
        disable(button1);
        disable(button2);
        disable(button3);
        disable(button4);
        disable(button5);
        disable(button6);
        disable(button7);
        disable(button8);
    }


    /**
     * Configures the "Pass" button to transition to the next turn in the game.
     * This method sets the button's label to "Passa", enables the button, and attaches
     * an event handler to trigger the "NextTurn" action via the client object when the button
     * is clicked. If a RemoteException occurs during the action transmission, it is caught and
     * its stack trace is printed.
     *
     * @param pass the Button to be configured; this button is used to send the next turn action
     */
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

    /**
     * Configures the "Load Goods" button to allow the user to select a grid tile and load goods.
     * The button is labeled as "Carica merce" and activated for user interaction.
     * When clicked, a {@link SingleTileSelector} instance is created, enabling the
     * user to select a single tile. Upon selection, the tile's coordinates are sent
     * to the server as a {@link LoadGood} action. Any resulting {@link RemoteException}
     * during this process is logged and handled appropriately.
     *
     * @param load the Button to be configured for the load goods action
     */
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

    /**
     * Configures the "Drop Goods" button, determining its label and behavior based on
     * whether the action involves precious goods. If the goods are precious, the button
     * is labeled as "Rimuovi merce preziosa" and given a wider width; otherwise, it is
     * labeled as "Scarica merce". Once configured, it enables the button and associates
     * an event handler to facilitate the removal of goods based on user selection.
     *
     * The removal process allows the user to select a tile and then choose which type
     * of goods to drop via a dialog. The appropriate action is sent to the server
     * depending on whether the goods are precious or not. Any communication error
     * during this interaction is logged.
     *
     * @param drop the button to be configured for the drop goods action
     * @param precious a boolean indicating if the goods are considered precious;
     *                 if true, the button is configured for precious goods removal
     */
    private void setupDropGoodBtn(Button drop, boolean precious) {
        if (precious) {
            drop.setText("Rimuovi merce preziosa");
            drop.setPrefWidth(240);
        }
        else {
            drop.setText("Scarica merce");
        }

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
                        if (precious) {
                            client.sendAction(new RemovePreciousItem(x,y,idx));
                        }
                        else {
                            client.sendAction(new LoseGood(x,y, idx));
                        }
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

    /**
     * Configures the "Land" button for a specific planet. This method enables the button
     * and sets an event handler to send a {@link Land} action to the server when clicked.
     *
     * @param land        the Button to be configured for landing action
     * @param planetIndex the index of the planet on which the landing action will be performed
     */
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

    /**
     * Configures the "Ready" button with text, enables it, and sets an action handler.
     * The button is labeled as "Pronto" and enabled for user interaction. When clicked,
     * a {@link Ready} action is sent to the server via the client object.
     * If an error occurs during communication with the server, it is logged.
     *
     * @param ready the Button to be configured for the "Ready" action
     */
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

    /**
     * Configures the provided button to skip the selection of a "good prize."
     *
     * @param skip the button that will be configured to trigger the skip action
     */
    private void setupSkipGoodPrize(Button skip){
        skip.setText("Skippa merce");
        enable(skip);
        skip.setOnAction(e -> {
            try {
                client.sendAction(new LoadGood(-1,-1));

            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Configures the "Activate Cannon" button. This method sets the button's label to "Attiva cannone,"
     * enables it, and assigns an event handler to initiate actions related to cannon activation.
     * When clicked, the user can select two tiles (representing the cannon and the target battery hub).
     * These selections are processed, and a {@link ActivateCannon} action is sent to the server.
     * Any {@link RemoteException} occurring during this communication is logged.
     *
     * @param activeCannon the Button to be configured for activating the cannon
     */
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

    /**
     * Configures the given button to activate the shield functionality.
     * Sets the button text, enables it, and assigns an action listener
     * to handle shield activation.
     *
     * @param s the button to be set up for activating the shield
     */
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

    /**
     * Configures the provided button to trigger an action for activating the engine.
     * Sets up the button's text, enables it, and assigns an event handler
     * to manage the activation process using a two-step selector for selecting engine
     * and battery coordinates.
     *
     * @param e the Button to be configured for activating the engine
     */
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

    /**
     * Configures the "Remove Crew" button to allow the user to select a tile and reduce the crew on it.
     * The button's text is set and it is enabled. An action handler is added to handle user interaction
     * and communicate the action to the client.
     *
     * @param reduceCrew the button that will be configured to handle removing a crew member
     */
    private void setupRemoveCrewBtn(Button reduceCrew) {
        reduceCrew.setText("Rimuovi membro");
        enable(reduceCrew);

        reduceCrew.setOnAction(e -> {
            // creo selettore per ridurre
            singleSelector = new SingleTileSelector(coord -> {
                int x = coord.x();
                int y = coord.y();
                try {
                    // invio azione e resetto selettore
                    client.sendAction(new ReduceCrew(x, y, 1));
                }
                catch (RemoteException ex) {
                    ex.printStackTrace();
                }
                finally {
                    singleSelector = null;
                }
            });
        });
    }

    /**
     * Initializes and configures UI components related to open space commands.
     * This method sets up the engine and ready buttons to ensure
     * the necessary functionalities are properly prepared for execution.
     */
    public void openSpaceCommands() {
        setupEngineBtn(button1);
        setupReadyBtn(button2);
    }

    /**
     * Configures the text and setup for buttons based on the specified planet ID.
     * The method defines button actions for landing on planets, passing, loading goods,
     * and dropping goods according to the available number of selectable planets
     * determined by the given ID.
     *
     * @param id the identifier used to determine the button configurations and
     *           number of selectable planets. Different ID values correspond to
     *           different scenarios for 4, 3, or 2 planets.
     */
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
                setupDropGoodBtn(button7, false);
                setupSkipGoodPrize(button8);

            }

            // 3 pianeti
            case 102, 114, 204, 207 -> {
                setupLandBtn(button1, 1);
                setupLandBtn(button2, 2);
                setupLandBtn(button3, 3);

                setupPassBtn(button4);
                setupLoadGoodBtn(button5);
                setupDropGoodBtn(button6, false);
                setupSkipGoodPrize(button7);

            }

            // 2 pianeti
            case 113, 205 -> {
                setupLandBtn(button1, 1);
                setupLandBtn(button2, 2);

                setupPassBtn(button3);
                setupLoadGoodBtn(button4);
                setupDropGoodBtn(button5, false);
                setupSkipGoodPrize(button6);

            }
        }
    }

    /**
     * Executes the stardustCommands method logic.
     * Updates a text label to display a message that informs the user
     * they cannot perform any actions and will lose flight days based
     * on the number of exposed connectors.
     */
    public void startdustCommands() {
        textLabel.setText("Non puoi fare nulla: perderai giorni di volo in base al numero di connettori esposti");
    }


    /**
     * Configures the interface and behavior for abandoned ship-related commands.
     * This method sets up the text and actions for the appropriate buttons, including:
     *
     * 1. Initializing and enabling the "Compra nave" button (button1), which sends a buy ship action when clicked.
     * 2. Setting up functionality for the pass button (button2) using the setupPassBtn method.
     * 3. Configuring the remove crew button (button3) using the setupRemoveCrewBtn method.
     *
     * Handles potential RemoteExceptions during the action submission for the "Compra nave" button.
     */
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
        setupRemoveCrewBtn(button3);
    }

    public void slaversCommands() {

        setupCannonBtn(button1);
        setupReadyBtn(button2);
        setupRemoveCrewBtn(button3);
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

    /**
     * Configures the operational buttons used in the Meteor Swarm Commands interface.
     * This method initializes and sets up the functionality for three specific buttons:
     * - The cannon button for firing mechanisms.
     * - The shield button for defensive operations.
     * - The ready button to signal readiness.
     * It sequentially calls methods to appropriately define the behavior
     * for each button using their respective parameters.
     */
    public void meteorSwarmCommands() {
        setupCannonBtn(button1);
        setupShieldBtn(button2);
        setupReadyBtn(button3);

    }

    /**
     * Updates the text label to inform the user that no actions can be taken
     * and that one crew member will be lost for each infected cabin.
     */
    public void epidemicCommands() {
        textLabel.setText("Non puoi fare nulla: perderai 1 membro dell'equipaggio per ogni cabina contagiata");
    }

    /**
     * Configures and sets up the pirate-themed commands for the user interface.
     * This method initializes buttons associated with different pirate actions and sets up their respective event handlers.
     * The buttons include:
     * - Cannon button for firing cannon actions
     * - Ready button to indicate readiness
     * - Shield button for defensive actions
     * - Pass button to skip a turn
     * Additionally, a button is configured for earning credits, which sends an EarnCredits action to the server when pressed.
     * Uses event listeners to handle user interactions, including sending data to the server through client-side actions.
     *
     * Handles potential RemoteException errors if any issues occur during server communication.
     */
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


    /**
     * Configures and sets up the user interface commands available when at an abandoned station.
     *
     * The method initializes several buttons:
     * - Button1 is configured to initiate the docking action by labeling it with "Attracca",
     *   enabling it, and setting its action to send a `DockStation` action to the server.
     * - Button2 is configured as a "pass turn" button using the `setupPassBtn` method.
     * - Button3 is set up to handle loading goods using the `setupLoadGoodBtn` method.
     * - Button4 is set up to handle dropping goods with `setupDropGoodBtn` method, passing
     *   `false` as the method parameter indicating its initial state.
     *
     * Exceptions during button actions, such as `RemoteException`, are logged to the console via stack trace.
     */
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
        setupDropGoodBtn(button4, false);
        setupSkipGoodPrize(button5);
    }


    /**
     * Configures the buttons and actions for the smugglers' commands in the game interface.
     * Winning commands:
     * - Configures the buttons for loading and dropping goods for the current player,
     *   and passing turns.
     * Losing commands:
     * - Configures the button for dropping goods for the opposing team/player.
     * - Configures the button to remove a battery, including interaction logic for
     *   selecting the location and sending the action to the client. Handles potential
     *   remote exceptions during the action processing.
     */
    public void smugglersCommands() {
        setupCannonBtn(button1);
        setupReadyBtn(button2);

        // comando vincitore
        setupLoadGoodBtn(button3);
        setupDropGoodBtn(button4, false);
        setupPassBtn(button5);
        setupSkipGoodPrize(button6);

        // comandi perdenti
        setupDropGoodBtn(button7, true);
        button8.setText("Rimuovi batteria");
        enable(button8);
        button8.setOnAction(e -> {
            singleSelector = new SingleTileSelector(batteryHub -> {
                try {
                    client.sendAction(new RemoveBatteries(batteryHub.x(), batteryHub.y(), 1));
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
                finally {
                    singleSelector = null;
                }
            });
        });
    }

    /**
     * Configures the commands and button actions required for the combat zone.
     * This method initializes and sets up various buttons related to defense mechanisms
     * and other actions, such as resolving penalties during combat scenarios.
     *
     * The configured buttons perform the following actions:
     * - Defense Commands:
     *   - Sets up the cannon button to handle cannon-related actions.
     *   - Configures the engine button to manage engine-related tasks.
     *   - Prepares the ready button to indicate readiness.
     * - Penalty Resolution Commands:
     *   - Configures the shield button for shield-related actions.
     *   - Sets up the crew removal button to handle scenarios requiring crew removal.
     *   - Configures the button for dropping goods, enabling or disabling it as appropriate.
     *   - Prepares a button for removing batteries, with an implemented action that utilizes
     *     a single tile selector. The selector interacts with the client to send a
     *     `RemoveBatteries` action, resolving penalties related to battery hubs.
     *
     * This method also handles potential RemoteException occurrences when sending actions
     * to the client, ensuring the selector is reset afterwards.
     */
    public void combatZoneCommands() {
        // comandi di difesa
        setupCannonBtn(button1);
        setupEngineBtn(button2);
        setupReadyBtn(button3);

        // comandi per risolvere le penalità
        setupShieldBtn(button4);
        setupRemoveCrewBtn(button5);
        setupDropGoodBtn(button6, true);
        button7.setText("Rimuovi batteria");
        enable(button7);
        button7.setOnAction(e -> {
            singleSelector = new SingleTileSelector(batteryHub -> {
                try {
                    client.sendAction(new RemoveBatteries(batteryHub.x(), batteryHub.y(), 1));
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
                finally {
                    singleSelector = null;
                }
            });
        });
    }

    /**
     * Configures and enables buttons to view other players' ships based on the list
     * of other players retrieved from the application.
     * This method retrieves the list of other players through the GuiApplication
     * instance, and dynamically sets the text and enables buttons based on the
     * number of players available in the list.
     */
    public void setupViewOtherShipsBtn() {
        ArrayList<String> otherPlayers = GuiApplication.getInstance().getOtherUsers();

        player1.setText(otherPlayers.get(0));
        enable(player1);

        if (otherPlayers.size() == 2 || otherPlayers.size() == 3) {
            player2.setText(otherPlayers.get(1));
            enable(player2);
        }
        if (otherPlayers.size() == 3) {
            player3.setText(otherPlayers.get(2));
            enable(player3);
        }
    }

    /**
     * Handles the event triggered when a "Spy Others" button is clicked.
     * Fetches the username from the clicked button and sends a request
     * to perform a spying action on the selected user.
     *
     * @param event the action event triggered by clicking the button
     * @throws RemoteException if an error occurs during the remote method invocation
     */
    @FXML
    public void onSpyOthersClicked(javafx.event.ActionEvent event) throws RemoteException {
        Button clickedButton = (Button) event.getSource();  // bottone che ha scatenato l’evento
        String username = clickedButton.getText();          // testo del bottone = username

        client.sendAction(new RequestShip(username));       // invia richiesta con username selezionato
    }

    /**
     * Handles the action event triggered when the refresh ship button is clicked.
     * Sends a request to update the ship layout for the current user.
     *
     * @throws RemoteException if a remote communication error occurs during the action.
     */
    @FXML
    public void onRefreshShipClicked() throws RemoteException{
        String myName = GuiApplication.getInstance().getMyNickname();
        client.sendAction(new RequestShip(myName));
    }


    /**
     * Updates the ship representation on the UI grid by clearing the existing components
     * and adding the elements from the provided ship layout. Click handlers are restored.
     *
     * @param ship a 2D array of {@code Component} objects that represent the layout
     *             of the ship. Each element corresponds to a grid cell, where a
     *             {@code null} value indicates an empty cell and a non-null value
     *             represents a component to be placed at a specific position.
     */
    public void updateShip(Component[][] ship) {
        shipGrid.getChildren().clear();

        for (int row = 0; row < ship.length; row++) {
            for (int col = 0; col < ship[row].length; col++) {
                Component component = ship[row][col];
                if (component != null) {
                    String imagePath = "/it/polimi/ingsw/psp23/images/tiles/" + component.getId() + ".jpg";
                    ImageView imageView = new ImageView(
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)))
                    );
                    imageView.setFitWidth(120);
                    imageView.setFitHeight(95);
                    imageView.setPreserveRatio(true);
                    imageView.setRotate(component.getRotate());
                    shipGrid.add(imageView, col, row);
                }
            }
        }

        // reinstall click handlers sui nuovi nodi
        installClickHandlers();
    }
}
