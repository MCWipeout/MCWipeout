package org.stormdev.mcwipeout.frame.obstacles.fans;
/*
  Created by Stormbits at 2/28/2023
*/

import lombok.Getter;
import lombok.Setter;
import org.stormdev.mcwipeout.utils.Cuboid;

public class FanObstacle {

    @Getter private Cuboid fanBoundingBox;
    @Getter private FanRotation rotation;

    @Getter @Setter private boolean enabled;

    public FanObstacle(Cuboid fanBoundingBox, FanRotation rotation) {
        this.fanBoundingBox = fanBoundingBox;
        this.rotation = rotation;
    }
}
