package org.stormdev.mcwipeout.frame.obstacles.moving;
/*
  Created by Stormbits at 8/20/2023
*/

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.stormdev.mcwipeout.frame.obstacles.moving.MoveableBlock;
import org.stormdev.mcwipeout.utils.Cuboid;

import java.util.List;

public class MovingPlatform {

    @Getter
    private Cuboid boundingBox;

    @Getter
    private List<MoveableBlock> blockList;

    public MovingPlatform(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.boundingBox = new Cuboid(new Location(world, x1, y1, z1), new Location(world, x2, y2, z2));


    }
}
