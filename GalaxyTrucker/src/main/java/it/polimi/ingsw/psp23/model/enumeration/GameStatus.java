package it.polimi.ingsw.psp23.model.enumeration;

/**
 * The GameStatus enum represents game states:
 * Core phases: Setup, Building, CheckBoards, SetCrew, Playing, End
 * Card phases:
 *  -Initialization: INIT_ prefixed states for various scenarios
 *  -End scenarios: END_ prefixed states for scenario conclusions
 *  -Other: WAITING_FOR_NEW_CARD for pending card state
 */
public enum GameStatus {
    Setup,
    Building,
    CheckBoards,
    SetCrew,
    Playing,
    End,
    INIT_ABANDONEDSHIP,
    INIT_PLANETS,
    INIT_ABANDONEDSTATION,
    INIT_COMBATZONE,
    INIT_EPIDEMIC,
    INIT_METEORSWARM,
    INIT_OPENSPACE,
    INIT_PIRATES,
    INIT_SLAVERS,
    INIT_SMUGGLERS,
    INIT_STARDUST,
    END_ABANDONEDSTATION,
    END_ABANDONEDSHIP,
    END_PLANETS,
    COMBATZONE_HUMANS,
    COMBATZONE_GOODS,
    END_SLAVERS,
    END_SMUGGLERS,
    END_STARDUST,
    END_PIRATES,
    FIRST_COMBATZONE,
    SECOND_COMBATZONE,
    THIRD_COMBATZONE,
    ENDTHIRD_COMBATZONE,
    WAITING_FOR_NEW_CARD;
}
