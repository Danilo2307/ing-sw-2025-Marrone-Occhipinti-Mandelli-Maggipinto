package it.polimi.ingsw.psp23.model.enumeration;

    public enum Challenge {
        Cannon_strength,
        Members,
        Engine_strength;

        public String toString() {
            if(this == Engine_strength){
                return "Engine_strength";
            }
            else if(this == Cannon_strength){
                return "Cannon_strength";
            }
            else{
                return "Members";
            }
        }
    }
