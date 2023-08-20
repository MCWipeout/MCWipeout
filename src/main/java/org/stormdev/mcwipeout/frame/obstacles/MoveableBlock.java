package org.stormdev.mcwipeout.frame.obstacles;
/*
  Created by Stormbits at 8/21/2023
*/

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.utils.FallingBlockFactory;

import static org.stormdev.mcwipeout.utils.MathUtils.lerp;

@Accessors(fluent = true)
public class MoveableBlock {

    @Getter
    private Location location;

    @Getter
    private Material oldMaterial;
    @Getter
    @Setter
    private Material newMaterial;

    @Getter
    @Setter
    private Location newLocation;

    @Getter
    @Setter
    private boolean shouldMove;

    public MoveableBlock(Block block) {
        this.location = block.getLocation();

        this.oldMaterial = block.getType();
    }

    public void reset() {
        if (location.equals(newLocation)) {
            newLocation = null;

        }
    }

    public void moveIn(int ticks) {
        location.getBlock().setType(Material.AIR);
        if (newLocation != null) {
            FallingBlock fallingBlock = FallingBlockFactory.buildFallingBlock(location, oldMaterial);
            Bukkit.getScheduler().runTaskTimer(Wipeout.get(), () -> {

                int time = 0;
                if (time < ticks) {
                    time++;
                    double dx = lerp(location.getX(), newLocation.getX(), time / ticks);
                    double dy = lerp(location.getY(), newLocation.getY(), time / ticks);
                    double dz = lerp(location.getZ(), newLocation.getZ(), time / ticks);

                    fallingBlock.teleport(fallingBlock.getLocation().add(dx, dy, dz));
                } else {
                    fallingBlock.remove();

                    newLocation.getBlock().setType(oldMaterial);
                }

            }, 0L, 0L);
        }
    }
}
