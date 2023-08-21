package org.stormdev.mcwipeout.dev.showtest.impl;
/*
  Created by Stormbits at 8/21/2023
*/

import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.dev.showtest.ShowElement;
import org.stormdev.mcwipeout.utils.LocationUtil;

import java.util.List;

public class BloomJet extends ShowElement {

    private Location location;
    @Setter
    private double size;
    @Setter
    private double height;
    private int duration;

    public BloomJet(Location location, double size, double height, int duration) {
        this.location = location;
        this.size = size;
        this.height = height;
        this.duration = duration;
    }

    @Override
    public void execute() {
        int amount = 7 * (int) this.size;
        new BukkitRunnable() {
            int timer = 0;

            public void run() {
                if (timer < duration) {
                    List<Vector> circle = LocationUtil.getCirclePoints(location.toVector(), size, amount);
                    for (Vector loc : circle) {
                        FallingBlock fb = location.getWorld().spawnFallingBlock(location, Material.BLUE_CONCRETE.createBlockData());
                        fb.setDropItem(false);
                        fb.setCustomName("fountain-block");
                        fb.setVelocity(new Vector(loc.getX() * 0.02 * size, height, loc.getZ() * 0.02 * size));
                    }
                    timer++;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Wipeout.get(), 0L, 1L);
    }
}
