package org.stormdev.mcwipeout.utils;
/*
  Created by Stormbits at 8/21/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static boolean isSimilar(Location location1, Location location2) {
        double x1 = location1.getX();
        double y1 = location1.getY();
        double z1 = location1.getZ();
        double x2 = location2.getX();
        double y2 = location2.getY();
        double z2 = location2.getZ();
        return (x1 == x2 && z1 == z2 && y2 <= y1);
    }

    public static boolean isStandingOnBlock(Entity e, List<Material> materials) {
        BoundingBox bb = e.getBoundingBox();
        Location min = new Location(e.getWorld(), bb.getMinX(), bb.getMinY() - .01, bb.getMinZ());
        return materials.contains(min.getBlock().getType())
                || materials.contains(min.add(bb.getWidthX(), 0, 0).getBlock().getType())
                || materials.contains(min.add(0, 0, bb.getWidthZ()).getBlock().getType())
                || materials.contains(min.add(0, 0, -bb.getWidthZ()).getBlock().getType())
                || materials.contains(min.add(-bb.getWidthX(), 0, 0).getBlock().getType());
    }

    public static List<Block> getSupportingBlocks(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;

        BoundingBox playerBB = craftPlayer.getBoundingBox().expand(0, -0.2, 0);
        Map<Block, BoundingBox> boundingBoxMap = new HashMap<>();
        List<Block> supportingBlocks = new ArrayList<>();

        final Location cornerLoc = player.getLocation().add(-1, -1, -1);
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                Block block = cornerLoc.clone().add(x, 0, z).getBlock();
                BoundingBox boundingBox = block.getBoundingBox();
                if (block.getType() != Material.AIR)
                    boundingBoxMap.put(block, boundingBox);
            }
        }
        if (boundingBoxMap.isEmpty()) return null;
        for (Block block : boundingBoxMap.keySet()) {

            if (playerBB.contains(boundingBoxMap.get(block))) supportingBlocks.add(block);
        }

        return supportingBlocks;
    }

    public String getFormattedTime(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
}
