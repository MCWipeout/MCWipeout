package org.stormdev.mcwipeout.frame.obstacles.bumpers.extended;/*
  Created by Stormbits at 10/1/2023
*/

import lombok.Getter;

public enum WallSize {

    ONE_BY_ONE(10015),
    TWO_BY_TWO(10017),
    TWO_BY_THREE(10018),
    ONE_BY_THREE(10026),
    ;


    @Getter private int customModelData;

    WallSize(int customModelData) {
        this.customModelData = customModelData;
    }
}
