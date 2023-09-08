package org.stormdev.mcwipeout.frame.obstacles.fans;
/*
  Created by Stormbits at 2/28/2023
*/

public enum FanRotation {

    POS_X, POS_Z, NEG_X, NEG_Z;

    public int getRelativeX(int power) {
        if (this.equals(POS_X)) {
            return power;
        }
        if (this.equals(POS_Z)) {
            return 0;
        }
        if (this.equals(NEG_X)) {
            return power * -1;
        }
        if (this.equals(NEG_Z)) {
            return 0;
        }

        return 0;
    }

    public int getRelativeZ(int power) {
        if (this.equals(POS_Z)) {
            return power;
        }
        if (this.equals(POS_X)) {
            return 0;
        }
        if (this.equals(NEG_Z)) {
            return power * -1;
        }
        if (this.equals(NEG_X)) {
            return 0;
        }

        return 0;
    }
}
