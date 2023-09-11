package org.stormdev.mcwipeout.dev.watershow.impl;
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
import org.stormdev.mcwipeout.dev.watershow.ShowElement;
import org.stormdev.mcwipeout.utils.helpers.FallingBlockFactory;

import static org.stormdev.mcwipeout.utils.helpers.LocationUtil.getPointOnCircle;

public class HelixJet extends ShowElement {

    private Location location;
    @Setter
    private double size;
    @Setter
    private double height;
    private int duration;

    public HelixJet(Location location, double size, double height, int duration) {
        this.location = location;

        this.size = size;
        this.height = height;
        this.duration = duration;
    }

    @Override
    public void execute() {
        new BukkitRunnable() {
            int timer = 0;

            public void run() {
                if (timer < duration) {
                    Location loc = location.clone().add(0, 0.05D, 0);
                    double n2 = 0.3141592653589793 * timer;
                    double n3 = timer * 0.1 % 2.5;
                    double n4 = 0.75;
                    Location pointOnCircle = getPointOnCircle(loc, true, n2, n4, n3);
                    Location pointOnCircle2 = getPointOnCircle(loc, true, n2 - 3.141592653589793, n4, n3);

                    FallingBlock fallingBlock1 = FallingBlockFactory.buildFallingBlock(loc, Material.BLUE_STAINED_GLASS);
                    Vector difference1 = pointOnCircle.toVector().subtract(loc.toVector()).normalize().multiply(0.2);
                    fallingBlock1.setVelocity(new Vector(difference1.getX() * 0.02 * size, height, difference1.getZ() * 0.02 * size));

                    FallingBlock fallingBlock2 = FallingBlockFactory.buildFallingBlock(loc, Material.BLUE_STAINED_GLASS);
                    Vector difference2 = pointOnCircle2.toVector().subtract(loc.toVector()).normalize().multiply(0.2);
                    fallingBlock2.setVelocity(new Vector(difference2.getX() * 0.02 * size, height, difference2.getZ() * 0.02 * size));


                    timer++;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Wipeout.get(), 0L, 1L);
    }
}
