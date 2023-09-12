package org.stormdev.mcwipeout.frame.obstacles.water;
/*
  Created by Stormbits at 9/12/2023
*/

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.stormdev.mcwipeout.frame.obstacles.GenericLocationSet;
import org.stormdev.mcwipeout.utils.WLocation;

import java.util.concurrent.ThreadLocalRandom;

public class WaterPreset {

    private GenericLocationSet locationSet;

    public WaterPreset(GenericLocationSet locationList) {
        this.locationSet = locationList;
    }

    public void place() {
        for (WLocation location : locationSet.getMap().keySet()) {

            location.asBlock().setType(Material.WATER);
            World world = location.asLocation().getWorld();
            world.spawnParticle(Particle.WATER_SPLASH, location.asLocation().add(0.5, 1, 0.5), 100, 0.1, 0.1, 0.1);

        }
    }

    public void remove() {
        for (WLocation location : locationSet.getMap().keySet()) {
            location.asBlock().setType(Material.AIR);
            World world = location.asLocation().getWorld();
            //world.spawnParticle(Particle.ASH, location.asLocation().add(0, 0.5, 0), 100, 0.1, 0.1, 0.1);
        }
    }
}
