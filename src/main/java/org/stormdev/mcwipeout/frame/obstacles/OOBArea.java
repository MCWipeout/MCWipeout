package org.stormdev.mcwipeout.frame.obstacles;
/*
  Created by Stormbits at 9/8/2023
*/

import lombok.AllArgsConstructor;
import org.stormdev.mcwipeout.utils.worldguardhook.RegionEnteredEvent;

@AllArgsConstructor
public class OOBArea {

    private String region;

    public boolean isPlayerWithin(RegionEnteredEvent event) {
        return event.getRegion().getId().contains(region);
    }
}
