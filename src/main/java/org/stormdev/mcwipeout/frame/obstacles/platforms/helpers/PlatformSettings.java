package org.stormdev.mcwipeout.frame.obstacles.platforms.helpers;
/*
  Created by Stormbits at 9/12/2023
*/


import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
public class PlatformSettings {

    private int xOffset = 0;
    private int yOffset = 0;
    private int zOffset = 0;

    private int interval = 0;

    private int delay = 0;

    private boolean mirror = false;

    private int showAt = 0;
    private int hideAt = 0;
}
