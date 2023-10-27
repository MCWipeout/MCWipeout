package org.stormdev.mcwipeout.frame.obstacles.bumpers.extended;
/*
  Created by Stormbits at 9/18/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.BumperObject;

import java.util.List;

public class ExtendedBumperWall extends Obstacle {

    private List<SlidingWall> slidingWalls;

    private List<BumperObject> bumperObjects;

    private int totalDuration;

    public ExtendedBumperWall(int totalDuration, List<BumperObject> bumperObjects, SlidingWall... slidingWall) {
        this.totalDuration = totalDuration;
        this.slidingWalls = List.of(slidingWall);
        this.bumperObjects = bumperObjects;

        slidingWalls.forEach(slidingWall1 -> slidingWall1.getCornerLocation1().asLocation().getChunk().addPluginChunkTicket(Wipeout.get()));
        bumperObjects.forEach(bumperObject -> bumperObject.getEntityLocation().asLocation().getChunk().addPluginChunkTicket(Wipeout.get()));
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
                    return;
                }

                if (timer <= totalDuration) {

                    for (SlidingWall wall : slidingWalls) {
                        if (timer == wall.getDelay()) {
                            wall.move();
                        }
                    }
                    for (BumperObject bumperObject : bumperObjects) {
                        if (bumperObject.getDelayInCycle() == timer) {
                            bumperObject.move();
                        }
                    }

                    timer++;
                } else {
                    timer = 0;
                }
            }
        }.runTaskTimer(Wipeout.get(), 20L, 0L);
    }

    @Override
    public void reset() {
        slidingWalls.forEach(SlidingWall::reset);
        for (BumperObject bumperObject : bumperObjects) {
            if (bumperObject.getDisplayEntity() != null) {
                bumperObject.getDisplayEntity().remove();
            }

            bumperObject.getBlockLocation().asBlock().setType(Material.AIR);
            bumperObject.setDisplayEntity(null);
        }
    }

    @Override
    public void enable() {
        slidingWalls.forEach(SlidingWall::load);
        bumperObjects.forEach(BumperObject::setupDisplayEntity);
    }

    @Override
    public void load() {

    }
}
