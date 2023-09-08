package org.stormdev.mcwipeout.frame.obstacles.bumpers;
/*
  Created by Stormbits at 9/8/2023
*/

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;

import java.util.List;

public class BumperWall extends Obstacle {

    private List<BumperObject> bumperObjectList;

    private int totalDuration;

    public BumperWall(List<BumperObject> bumperObjects, int totalDuration) {
        this.bumperObjectList = bumperObjects;
        this.totalDuration = totalDuration;
    }


    @Override
    public void handle(Event event) {

    }

    @Override
    public void run() {
        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                if (!isEnabled()) {
                    this.cancel();
                }
                timer++;

                if (timer < totalDuration) {

                    for (BumperObject bumperObject : bumperObjectList) {
                        if (bumperObject.getDelayInCycle() == timer) {
                            bumperObject.move();
                        }
                    }

                } else {
                    timer = 0;
                }

            }
        }.runTaskTimer(Wipeout.get(), 20L, 0L);
    }

    @Override
    public void reset() {
        for (BumperObject bumperObject : bumperObjectList) {
            if (bumperObject.getDisplayEntity() != null) {
                bumperObject.getDisplayEntity().remove();
            }

            bumperObject.getBlockLocation().asBlock().setType(Material.AIR);
        }
    }

    @Override
    public void enable() {
        for (BumperObject bumperObject : bumperObjectList) {
            if (bumperObject.getDisplayEntity() == null) {
                bumperObject.setupDisplayEntity();
            }

            bumperObject.getBlockLocation().asBlock().setType(Material.AIR);

        }
    }
}
