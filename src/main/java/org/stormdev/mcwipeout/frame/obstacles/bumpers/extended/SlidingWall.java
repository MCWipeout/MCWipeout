package org.stormdev.mcwipeout.frame.obstacles.bumpers.extended;
/*
  Created by Stormbits at 9/18/2023
*/

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.stormdev.mcwipeout.utils.helpers.Cuboid;
import org.stormdev.mcwipeout.utils.helpers.Direction;
import org.stormdev.mcwipeout.utils.helpers.WLocation;
import org.stormdev.utils.SyncScheduler;

import java.util.ArrayList;
import java.util.List;

public class SlidingWall {

    @Getter
    private WLocation cornerLocation1;
    private WLocation cornerLocation2;

    private Cuboid cuboid;

    private List<SlidingBlock> slidingBlockList;

    @Getter
    private int delay;

    private float xTranslation, zTranslation;

    private float dx, dz;

    private WallSize size;

    private Direction direction;

    private List<ItemDisplay> displays;

    private SlidingWall(WLocation cornerLocation1, WLocation cornerLocation2, float dx, float dz, float xTranslation, float zTranslation, int delay) {
        cuboid = new Cuboid(cornerLocation1.asLocation(), cornerLocation2.asLocation());
        this.cornerLocation1 = cornerLocation1;
        this.cornerLocation2 = cornerLocation2;
        this.slidingBlockList = new ArrayList<>();
        this.dx = dx;
        this.dz = dz;
        this.delay = delay;
        this.xTranslation = xTranslation;
        this.zTranslation = zTranslation;
        this.displays = new ArrayList<>();
    }

    public SlidingWall setSize(WallSize wallSize) {
        this.size = wallSize;
        return this;
    }

    public SlidingWall setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }


    public static SlidingWall of(WLocation cornerLocation1, WLocation cornerLocation2, float dx, float dz, float xTranslation, float zTranslation, int delay) {
        return new SlidingWall(cornerLocation1, cornerLocation2, dx, dz, xTranslation, zTranslation, delay);
    }

    public void move() {
        slidingBlockList.forEach(slidingBlock -> slidingBlock.moveTo(xTranslation, 0, zTranslation, 32, true));

        if (displays.size() > 0) {
            ItemDisplay displayEntity = displays.get(0);
            displayEntity.setInterpolationDelay(0);
            displayEntity.setInterpolationDuration(16);
            Transformation transformation = displayEntity.getTransformation();
            transformation.getTranslation().set(xTranslation, 0, zTranslation);

            displayEntity.setTransformation(transformation);

            SyncScheduler.get().runLater(() -> {
                if (displayEntity.isDead()) return;

                displayEntity.setInterpolationDuration(16);
                displayEntity.setInterpolationDelay(0);
                Transformation newTransformation = displayEntity.getTransformation();
                newTransformation.getTranslation().set(0, 0, 0);

                displayEntity.setTransformation(newTransformation);
            }, 16L);
        }
    }

    public void load() {
        cuboid.blockList().forEachRemaining(block -> slidingBlockList.add(new SlidingBlock(WLocation.from(block), dx, dz)));

        slidingBlockList.forEach(SlidingBlock::load);

        if (direction != null) {
            ItemDisplay itemDisplay = cornerLocation1.getWorld().spawn(direction.getTranslatedLocation(cuboid.getCenter(), size, direction), ItemDisplay.class);

            ItemStack head = new ItemStack(Material.GHAST_TEAR, 1);
            ItemMeta itemMeta = head.getItemMeta();
            itemMeta.setCustomModelData(size == null ? 10000 : size.getCustomModelData());
            head.setItemMeta(itemMeta);

            itemDisplay.setBrightness(new Display.Brightness(14, 14));

            itemDisplay.setItemStack(head);

            Location location = itemDisplay.getLocation();

            location.setYaw(0);

            itemDisplay.teleport(location);

            displays.add(itemDisplay);
        }
    }

    public void reset() {
        for (SlidingBlock slidingBlock : slidingBlockList) {
            slidingBlock.remove();
        }

        for (ItemDisplay display : displays) {
            display.remove();
        }

        slidingBlockList.clear();
        displays.clear();
    }

    public static double[] lerp3D(double amount, double x1, double y1, double z1, double x2, double y2, double z2) {
        return new double[]{x1 + (x2 - x1) * amount, y1 + (y2 - y1) * amount, z1 + (z2 - z1) * amount};
    }
}
