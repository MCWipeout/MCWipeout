package org.stormdev.mcwipeout.frame.obstacles.patterns;
/*
  Created by Stormbits at 9/13/2023
*/

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Transformation;
import org.stormdev.mcwipeout.frame.obstacles.GenericLocationSet;
import org.stormdev.mcwipeout.utils.WLocation;

import java.util.ArrayList;
import java.util.List;

public class PatternPreset {

    private GenericLocationSet locationSet;

    private List<BlockDisplay> blockDisplayList;

    public PatternPreset(GenericLocationSet locationList) {
        this.locationSet = locationList;
        this.blockDisplayList = new ArrayList<>();
    }

    public void place() {
        for (WLocation location : locationSet.getMap().keySet()) {
            BlockDisplay blockDisplay = location.asLocation().getWorld().spawn(location.asLocation(), BlockDisplay.class);
            blockDisplay.setBlock(locationSet.getMap().get(location).createBlockData());

            blockDisplayList.add(blockDisplay);

            location.asBlock().setType(Material.AIR);

            blockDisplay.setInterpolationDuration(10);
            blockDisplay.setInterpolationDelay(0);

            Transformation transformation = blockDisplay.getTransformation();
            transformation.getTranslation().set(0, 0, 1);
        }
    }

    public void remove() {
    }
}
