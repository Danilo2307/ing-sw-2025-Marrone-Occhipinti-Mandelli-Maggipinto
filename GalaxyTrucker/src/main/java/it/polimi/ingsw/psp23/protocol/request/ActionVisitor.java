package it.polimi.ingsw.psp23.protocol.request;


public interface ActionVisitor<T> {

    /**
     * Visits and processes the activation of a cannon for a specific player.
     *
     * This method is invoked when a player attempts to activate a double cannon during the game's action phase.
     * It verifies the request, processes the activation, and updates the game state or player resources
     * if the action is deemed valid.
     *
     * @param activateCannon The action object containing the details of the cannon activation attempt,
     *                       including the position of the cannon and the battery hub for resource consumption.
     * @param username       The username of the player initiating the request to activate the cannon.
     * @return A generic return type representing the result of processing the cannon activation.
     */
    public T visitForActivateCannon(ActivateCannon activateCannon, String username);

    /**
     * Processes the given ActivateEngine instance and performs actions needed
     * to activate the engine with respect to the provided username.
     *
     * @param activateEngine the ActivateEngine instance to be processed
     * @param username the username associated with the activation process
     * @return a value of type T that represents the result of the activation process
     */
    public T visitForActivateEngine(ActivateEngine activateEngine, String username);

    /**
     * Processes and applies the logic for activating a shield based on the given parameters.
     *
     * @param activateShield the object containing details and parameters for the shield activation process
     * @param username the name of the user requesting shield activation
     * @return the result of the shield activation process, encapsulated in the corresponding type T
     */
    public T visitForActivateShield(ActivateShield activateShield, String username);

    /**
     * Processes the addition of a tile based on the provided AddTile instance and username.
     *
     * @param addTile an instance of AddTile containing the details of the tile to be added
     * @param username the username of the user performing the operation
     * @return an instance of type T as the result of the processing
     */
    public T visitForAddTile(AddTile addTile, String username);

    /**
     * Processes a draw action from the face-up pile for the specified user.
     *
     * @param drawFromFaceUp the object representing the draw action from the face-up pile
     * @param username the username of the player performing the action
     * @return an instance of type T resulting from the processing of the draw action
     */
    public T visitForDrawFromFaceUp(DrawFromFaceUp drawFromFaceUp, String username);

    /**
     * Handles the process of visiting and performing actions for a draw operation
     * from a heap. The method processes the specified draw action and associates
     * it with the given username.
     *
     * @param drawFromHeap the draw action to be performed from the heap
     * @param username the name of the user associated with the draw operation
     * @return the result of the draw operation, of type T
     */
    public T visitForDrawFromHeap(DrawFromHeap drawFromHeap, String username);

    /**
     * Handles the visit operation for the ReleaseTile action. This method is invoked when a player performs
     * the action to release the tile currently held in their hand.
     *
     * @param releaseTile an instance of ReleaseTile representing the action triggered by the player.
     * @param username the username of the player who initiated the action.
     * @return a result of type T, as processed by the implementation of this method in the concrete visitor.
     */
    public T visitForReleaseTile(ReleaseTile releaseTile, String username);

    /**
     * Processes the removal of a tile for a given username.
     *
     * @param removeTile the tile to be removed, encapsulated in a RemoveTile object
     * @param username the username of the user associated with the tile removal operation
     * @return a result of type T based on the processing of the tile removal
     */
    public T visitForRemoveTile(RemoveTile removeTile, String username);

    /**
     * Handles the invocation of a visit action related to the RequestShip event. This method is responsible
     * for processing the request to view the current ship layout for a specific player. It triggers appropriate
     * logic in the visitor implementation to retrieve the necessary information and prepare a response.
     *
     * @param requestShip the RequestShip event containing the details of the request to view the player's ship
     * @param username the username of the player who initiated the request
     * @return a generic object of type T as defined by the specific ActionVisitor implementation
     */
    public T visitForRequestShip(RequestShip requestShip, String username);

    /**
     * Processes a RequestTileInfo action, triggered when a user requests information
     * about a specific tile located at the ship's specified coordinates.
     *
     * @param requestTileInfo the action containing the coordinates of the requested tile
     * @param username the username of the player requesting the tile information
     * @return a result based on the implementation of the action processing, typically
     *         providing the required tile information or an appropriate response
     */
    public T visitForRequestTileInfo(RequestTileInfo requestTileInfo, String username);

    /**
     * Processes the given RequestUncovered instance for the specified username.
     *
     * @param requestUncovered the RequestUncovered instance to be processed
     * @param username the username associated with the request
     * @return an instance of type T resulting from the processing of the request
     */
    public T visitForRequestUncovered(RequestUncovered requestUncovered, String username);

    /**
     * Visits the RotateTile action and performs the associated operation.
     * This method is typically called to rotate a tile currently held by the user.
     *
     * @param rotateTile the RotateTile action to be visited
     * @param username the username of the player performing the action
     * @return a generic result of type T, representing the outcome of the operation
     */
    public T visitForRotateTile(RotateTile rotateTile, String username);

    /**
     * Visits the SetCrew action and allows the processing of its logic.
     *
     * @param setCrew the SetCrew action containing the details for adding crew members to a specific location
     * @param username the username of the player who initiated the action
     * @return the result of the action processing, determined by the implementation
     */
    public T visitForSetCrew(SetCrew setCrew, String username);

    /**
     * Processes the action of setting a username by visiting the respective action.
     *
     * @param setUsername the action object containing the username information to be set
     * @param username the username of the player performing the action
     * @return a result of type T determined by the implementation of the visit operation
     */
    public T visitForSetUsername(SetUsername setUsername, String username);

    /**
     * Processes the given TurnHourglass instance and performs an operation based on the user's action.
     *
     * @param turnHourglass the TurnHourglass object to be visited and processed
     * @param username the name of the user associated with the operation on the TurnHourglass
     * @return a result of type T after processing the TurnHourglass object
     */
    public T visitForTurnHourGlass(TurnHourglass turnHourglass, String username);

    /**
     * Handles the action for taking a reserved tile during the game.
     * This method is part of the visitor pattern used to process different actions.
     *
     * @param takeReservedTile the action object containing the index of the reserved tile to take
     * @param username the username of the player performing the action
     * @return a generic result depending on the implementation of the visitor
     */
    public T visitForTakeReservedTile(TakeReservedTile takeReservedTile, String username);

    /**
     * Processes the visitation logic for a ReserveTile entity by a specific user.
     *
     * @param reserveTile the ReserveTile instance to be visited
     * @param username the username of the user visiting the ReserveTile
     * @return a result of the operation, determined by the specific implementation
     */
    public T visitForReserveTile(ReserveTile reserveTile, String username);

    /**
     * Visits the specific action for registering the number of players in the game.
     * This method processes the {@link RegisterNumPlayers} action and performs
     * the necessary logic for assigning the number of requested players.
     *
     * @param registerNumPlayers the action containing the number of players to be registered
     * @param username the username of the player initiating the action
     * @return an object of type T resulting from the execution of the visit
     */
    public T visitForRegisterNumPlayers(RegisterNumPlayers registerNumPlayers, String username);

    /**
     * Handles the "next turn" action for a specific user. It processes the action
     * by invoking the appropriate logic for transitioning to the next turn within
     * the game. This method is a part of the visitor pattern implementation.
     *
     * @param nextTurn the NextTurn action that represents the transition request to the next turn
     * @param username the username of the player who initiated the action
     * @return an object of type T, which represents the outcome or result of visiting this action
     */
    public T visitForNextTurn(NextTurn nextTurn, String username);
    
    /**
     * Processes a buying ship action for a specific user.
     *
     * @param buyShip the object representing the ship to be bought
     * @param username the username of the user performing the buy operation
     * @return a result of the type T after processing the buy ship action
     */
    public T visitForBuyShip(BuyShip buyShip, String username);

    /**
     * Executes the visit operation for the given Help action and username.
     * The method is implemented within the visitor pattern, allowing specific behavior
     * to be executed for the Help action.
     *
     * @param help an instance of the Help action that needs to be processed
     * @param username the username of the player requesting the help operation
     * @return an object of type T resulting from the processing of the given action and username
     */
    public T visitForHelp(Help help, String username);

    /**
     * This method processes the given DockStation and performs operations based on its state
     * or configuration in relation to the specified user.
     *
     * @param dockStation the DockStation object to be processed
     * @param username the username of the individual performing the operation
     * @return a result of type T based on the processing of the DockStation
     */
    public T visitForDockStation(DockStation dockStation, String username);

    /**
     * Processes the loading of goods by visiting the specified loadGood context.
     *
     * @param loadGood the context containing details about the goods to be loaded
     * @param username the username of the person initiating the loading process
     * @return an instance of type T resulting from the processing of the loading operation
     */
    public T visitForLoadGoods(LoadGood loadGood, String username);

    /**
     * Handles the specified "Ready" action from a user.
     * Delegates the operation to the provided {@code ActionVisitor} implementation.
     *
     * @param ready the "Ready" action to be visited
     * @param username the username of the player initiating this action
     * @return the result of the visitor's operation, defined by the generic type {@code T}
     */
    public T visitForReady(Ready ready, String username);

    /**
     * Processes an action for landing on a planet.
     *
     * @param land the land object representing the planet to land on
     * @param username the username of the individual performing the action
     * @return a result of type T based on the landing action
     */
    public T visitForLandOnPlanet(Land land, String username);

    /**
     * Processes the given ReduceCrew operation for reducing the crew size.
     *
     * @param reduceCrew the ReduceCrew operation containing the details of the crew reduction
     * @param username the username of the individual initiating the operation
     * @return the result of processing the ReduceCrew operation
     */
    public T visitForReduceCrew(ReduceCrew reduceCrew, String username);

    /**
     * Visits the specified {@link RemovePreciousItem} action to process and execute
     * the removal of a precious item based on the game's context.
     *
     * @param removePreciousItem the action object containing the details of the
     *                           precious item removal, including coordinates and quantity.
     * @param username           the username of the player who initiated the action.
     * @return a result of type T, representing the outcome of the action processing.
     */
    public T visitForRemovePreciousItem(RemovePreciousItem removePreciousItem, String username);

    /**
     * Processes the action of taking cards from the visible deck.
     *
     * @param takeVisibleDeck the action object that represents taking cards from the visible deck
     * @param username the name of the user performing the action
     * @return a result of type T after processing the visible deck action
     */
    public T visitForTakeVisibleDeck(TakeVisibleDeck takeVisibleDeck, String username);

    /**
     * Processes the given ReleaseDeck object and performs operations based on the username provided.
     *
     * @param releaseDeck the ReleaseDeck object to be processed
     * @param username the username associated with the operation or context
     * @return an object of type T resulting from the operation
     */
    public T visitForReleaseDeck(ReleaseDeck releaseDeck, String username);

    /**
     * Processes the given finished entity in the context of the specified username.
     *
     * @param finished the Finished object to visit and process
     * @param username the username associated with the process or operation
     * @return the result of processing the Finished object, with type T
     */
    public T visitForFinished(Finished finished, String username);

    /**
     * Processes the provided Put operation for the given username.
     *
     * @param put the Put operation to be processed
     * @param username the username associated with this operation
     * @return the result of processing the Put operation
     */
    public T visitForPut(Put put, String username);

    /**
     * Handles the visitation logic for a Fixed object based on the given username.
     *
     * @param fixed the Fixed object to be processed
     * @param username the username associated with the operation
     * @return a result of type T based on the visitation logic
     */
    public T visitForFixed(Fixed fixed, String username);

    /**
     * Processes the provided ShowPlayersPositions object and a username to perform
     * operations related to showing the positions of players.
     *
     * @param showPlayersPositions the object containing data for showing players' positions
     * @param username the username associated with the operation
     * @return the result of the operation, represented as a generic type T
     */
    public T visitForShowPlayersPositions(ShowPlayersPositions showPlayersPositions, String username);

    /**
     * Processes the specified LoseGood object and associates it with the provided username.
     *
     * @param loseGood the LoseGood object to be processed
     * @param username the username to associate with the LoseGood
     * @return a result of type T representing the outcome of the processing
     */
    public T visitForLoseGood(LoseGood loseGood, String username);

    /**
     * Processes the provided MoveGood object in the context of a user action for moving goods.
     *
     * @param moveGood the object containing details about the goods to be moved
     * @param username the name of the user initiating the move action
     * @return a result of type T representing the outcome of the move operation
     */
    public T visitForMoveGood(MoveGood moveGood, String username);

    /**
     * Processes the action of removing batteries from a specific tile on the player's ship.
     *
     * @param removeBatteries an instance of RemoveBatteries containing the coordinates and number of batteries to remove
     * @param username the username of the player initiating the action
     * @return a generic type T that represents the result of the visit action
     */
    public T visitForRemoveBatteries(RemoveBatteries removeBatteries, String username);

    /**
     * Handles the "Leave Flight" action for a specific user.
     * This method is invoked when a player decides to leave their current flight.
     *
     * @param leaveFlight the LeaveFlight action that encapsulates the details of this command
     * @param username the username of the player executing the action
     * @return a value of type T based on the implementation of the method in the visitor
     */
    public T visitForLeaveFlight(LeaveFlight leaveFlight, String username);

    /**
     * Handles the action of a card draw event in the game.
     * This method is triggered when a player attempts to draw a card
     * and determines the outcome based on the input parameters.
     *
     * @param drawCard the card to be drawn, encapsulated in a {@code DrawCard} object
     * @param username the username of the player attempting to draw a card
     * @return an object of type {@code T}, representing the result of the draw event
     */
    public T visitForDrawCard(DrawCard drawCard, String username);

    /**
     * Processes a user decision and associates it with a specific username.
     *
     * @param userDecision an instance of UserDecision that represents the decision made by the user
     * @param username the username of the user making the decision
     * @return an object of type T that represents the outcome of processing the user's decision
     */
    public T visitForUserDecision(UserDecision userDecision, String username);

    /**
     * Processes the provided flight board request and username.
     *
     * @param requestFlightBoard the flight board request object to be processed
     * @param username the username associated with the request
     * @return a generic type T representing the result of the processing
     */
    public T visitForRequestFlightBoard(RequestFlightBoard requestFlightBoard, String username);

    /**
     * Processes an EarnCredits object and performs an operation associated with earning credits
     * for a specific user.
     *
     * @param earnCredits the EarnCredits object containing credit-related details
     * @param username the username of the user earning credits
     * @return a result of type T, representing the outcome of processing the given EarnCredits object
     */
    public T visitForEarnCredits(EarnCredits earnCredits, String username);
}
