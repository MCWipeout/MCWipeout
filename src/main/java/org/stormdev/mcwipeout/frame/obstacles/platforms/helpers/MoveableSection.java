package org.stormdev.mcwipeout.frame.obstacles.platforms.helpers;
/*
  Created by Stormbits at 9/11/2023
*/

import org.bukkit.Material;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.JsonPlatformSection;
import org.stormdev.mcwipeout.utils.WLocation;

import java.util.Map;

public class MoveableSection {

    private JsonPlatformSection jsonSection;

    public MoveableSection(JsonPlatformSection jsonPlatformSection) {
        this.jsonSection = jsonPlatformSection;

        for (Map.Entry<WLocation, Material> entry : jsonPlatformSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(entry.getValue());
        }
    }
}
