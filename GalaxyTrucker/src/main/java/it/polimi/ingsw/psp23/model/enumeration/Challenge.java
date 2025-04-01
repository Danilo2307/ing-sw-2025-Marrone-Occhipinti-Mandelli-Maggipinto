package it.polimi.ingsw.psp23.model.enumeration;

    public enum Challenge {
        CannonStrength,
        Members,
        EngineStrength;

        public String toString() {
            if(this == EngineStrength){
                return "Engine_strength";
            }
            else if(this == CannonStrength){
                return "Cannon_strength";
            }
            else{
                return "Members";
            }
        }
    }
