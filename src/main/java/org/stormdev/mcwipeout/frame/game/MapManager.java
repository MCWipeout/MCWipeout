package org.stormdev.mcwipeout.frame.game;
/*
  Created by Stormbits at 2/12/2023
*/

import lombok.Getter;
import org.bukkit.entity.Player;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.OOBArea;
import org.stormdev.mcwipeout.maps.MapOneModule;
import org.stormdev.mcwipeout.maps.MapThreeModule;
import org.stormdev.mcwipeout.maps.MapTwoModule;
import org.stormdev.mcwipeout.utils.worldguardhook.RegionEnteredEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapManager {

    private Wipeout plugin;

    @Getter
    private java.util.Map<String, Map> maps;

    private List<OOBArea> oobAreaList;

    public MapManager(Wipeout plugin) {
        this.plugin = plugin;
        this.maps = new HashMap<>();
        this.oobAreaList = new ArrayList<>();

        this.maps.put("map1", new MapOneModule());
        oobAreaList.add(new OOBArea("map-1-oob"));

        this.maps.put("map2", new MapTwoModule());
        oobAreaList.add(new OOBArea("map-2-oob"));

        this.maps.put("map3", new MapThreeModule());
        oobAreaList.add(new OOBArea("map-3-oob"));
    }

    public boolean isOOB(RegionEnteredEvent event) {
        for (OOBArea oobArea : oobAreaList) {
            if (oobArea.isPlayerWithin(event)) {
                return true;
            }
        }
        return false;
    }
}
