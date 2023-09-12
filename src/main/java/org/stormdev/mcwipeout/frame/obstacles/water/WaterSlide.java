package org.stormdev.mcwipeout.frame.obstacles.water;
/*
  Created by Stormbits at 9/12/2023
*/

import org.bukkit.event.Event;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;

import java.util.Arrays;
import java.util.List;

public class WaterSlide extends Obstacle {

    private List<WaterPreset> presetList;

    public WaterSlide(WaterPreset... presets) {
        this.presetList = Arrays.stream(presets).toList();
    }

    @Override
    public void handle(Event event) {

    }

    @Override
    public void run() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void enable() {

    }

    @Override
    public void load() {

    }
}
