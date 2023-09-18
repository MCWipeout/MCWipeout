package org.stormdev.mcwipeout.utils.helpers;
/*
  Created by Stormbits at 8/21/2023
*/

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;

public class FallingBlockFactory {

    public static FallingBlock buildFallingBlock(@NonNull Location location, Material material) {
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, material.createBlockData());

        fallingBlock.setCancelDrop(true);
        fallingBlock.setDropItem(false);
        fallingBlock.setHurtEntities(false);
        fallingBlock.setGravity(false);
        fallingBlock.setTicksLived(1);

        return fallingBlock;
    }
}
