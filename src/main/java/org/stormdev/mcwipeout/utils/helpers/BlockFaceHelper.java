package org.stormdev.mcwipeout.utils.helpers;
/*
  Created by Stormbits at 9/11/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.*;

public class BlockFaceHelper {

    private static final BlockFace[] faces = BlockFace.values();

    public static Set<Block> getConnection(Block block) {
        Set<Block> set = new HashSet<>();
        LinkedList<Block> list = new LinkedList<>();

        list.add(block);

        while ((block = list.poll()) != null) {
            getConnectedBlocks(block, set, list);
        }

        return set;

    }

    private static void getConnectedBlocks(Block block, Set<Block> results, List<Block> toCheck) {
        Set<Block> result = results;

        for (BlockFace face : faces) {
            Block b = block.getRelative(face);
            if (b.getType() != Material.AIR) {
                if (result.add(b)) {
                    toCheck.add(b);
                }
            }
        }
    }

    public static Set<Block> getConnectionWithType(Block block, Material type) {
        Set<Block> set = new HashSet<>();
        LinkedList<Block> list = new LinkedList<>();

        list.add(block);

        while ((block = list.poll()) != null) {
            getConnectedBlocksWithType(block, set, list, type);
        }

        return set;

    }

    private static void getConnectedBlocksWithType(Block block, Set<Block> results, List<Block> toCheck, Material type) {
        Set<Block> result = results;

        for (BlockFace face : faces) {
            Block b = block.getRelative(face);
            if (b.getType() == type) {
                if (result.add(b)) {
                    toCheck.add(b);
                }
            }
        }
    }
}
