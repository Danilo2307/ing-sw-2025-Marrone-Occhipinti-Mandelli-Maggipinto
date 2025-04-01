package it.polimi.ingsw.psp23.model.enumeration;

    public enum Challenge {
        CannonStrength,
        Members,
        EngineStrength;

        public String toString() {
            if(this == EngineStrength){
                return "EngineStrength";
            }
            else if(this == CannonStrength){
                return "CannonStrength";
            }
            else{
                return "Members";
            }
        }
    }
