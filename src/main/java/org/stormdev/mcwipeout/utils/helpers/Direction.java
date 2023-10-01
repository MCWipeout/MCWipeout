package org.stormdev.mcwipeout.utils.helpers;
/*
  Created by Stormbits at 10/1/2023
*/

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.extended.WallSize;

public enum Direction {

    NORTH(),
    SOUTH();

    public Location getTranslatedLocation(Location location, WallSize wallSize, Direction direction) {
        switch (direction) {
            case SOUTH:
                switch (wallSize) {
                    case ONE_BY_ONE, ONE_BY_THREE -> {
                        return location.clone().add(0.5, 0.5, -0.4);
                    }
                    case TWO_BY_TWO -> {
                        return location.clone().add(1, 0, -0.4);
                    }
                    case TWO_BY_THREE -> {
                        return location.clone().add(1, 0.5, -0.4);
                    }

                }
            default:
                return null;
        }
    }
}
