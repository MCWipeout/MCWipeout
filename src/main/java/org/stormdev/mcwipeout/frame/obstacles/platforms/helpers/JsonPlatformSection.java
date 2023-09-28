package org.stormdev.mcwipeout.frame.obstacles.platforms.helpers;
/*
  Created by Stormbits at 9/11/2023
*/

import lombok.Getter;
import org.bukkit.Material;
import org.stormdev.mcwipeout.utils.helpers.WLocation;

import java.util.Map;

@Getter
public class JsonPlatformSection {

    private Map<WLocation, Material> map;

    private PlatformSettings settings;


    public JsonPlatformSection(Map<WLocation, Material> map, PlatformSettings settings) {
        this.map = map;
        this.settings = settings;
    }
}
