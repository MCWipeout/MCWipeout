package org.stormdev.mcwipeout.frame.obstacles.fans;
/*
  Created by Stormbits at 2/28/2023
*/

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.utils.Cuboid;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.concurrent.TimeUnit;

@Accessors(fluent = true)
public class FanObject {

    @Getter
    private Cuboid fanBoundingBox;

    @Getter
    private FanRotation rotation;

    @Getter
    private int enableAt;
    @Getter
    private int disableAt;

    private int power;

    @Getter
    @Setter
    private boolean enabled;

    public static FanObject of(Cuboid fanBoundingBox, FanRotation rotation, int enableAt, int disableAt, int power) {
        return new FanObject(fanBoundingBox, rotation, enableAt, disableAt, power);
    }

    private FanObject(Cuboid fanBoundingBox, FanRotation rotation, int enableAt, int disableAt, int power) {
        this.fanBoundingBox = fanBoundingBox;
        this.rotation = rotation;
        this.enableAt = enableAt;
        this.disableAt = disableAt - 5;
        this.power = power;
        this.enabled = false;
    }

    public void displayParticle() {
        fanBoundingBox.blockList()
                .forEachRemaining(block -> block.getLocation().getWorld().spawnParticle(Particle.CLOUD, block.getLocation().add(0.5, 0, 0.5), 1, 0.1, 0.1, 0.1, 0));
    }

    public void toggle(boolean enabled) {

        if (enabled) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    enabled(true);
                }
            }.runTaskLater(Wipeout.get(), 5L);
        } else {
            enabled(false);
        }
    }

    public void fling(Player player) {
        player.setVelocity(new Vector(rotation.getRelativeX(power), 0.5, rotation.getRelativeZ(power)));
    }
}
