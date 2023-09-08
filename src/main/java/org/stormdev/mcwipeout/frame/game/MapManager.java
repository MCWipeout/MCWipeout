package org.stormdev.mcwipeout.frame.game;
/*
  Created by Stormbits at 2/12/2023
*/

import lombok.Getter;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.maps.map1.MapOneModule;

import java.util.HashMap;

public class MapManager {

    private Wipeout plugin;

    @Getter
    private java.util.Map<String, Map> maps;

    public MapManager(Wipeout plugin) {
        this.plugin = plugin;
        this.maps = new HashMap<>();

        this.maps.put("map1", new MapOneModule());
    }
}
