package org.stormdev.mcwipeout.frame.obstacles.clockarms;
/*
  Created by Stormbits at 9/26/2023
*/

import org.bukkit.event.Event;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;

import java.util.List;

public class ClockArmsHolder extends Obstacle {

    private List<ClockArms> clockArmsList;

    public ClockArmsHolder(ClockArms... clockArmsList) {
        this.clockArmsList = List.of(clockArmsList);
    }

    @Override
    public void handle(Event event) {

    }

    @Override
    public void run() {
        clockArmsList.forEach(ClockArms::run);
    }

    @Override
    public void reset() {
        clockArmsList.forEach(ClockArms::reset);
    }

    @Override
    public void enable() {
        clockArmsList.forEach(ClockArms::enable);
    }

    @Override
    public void load() {
        clockArmsList.forEach(ClockArms::load);
    }
}
