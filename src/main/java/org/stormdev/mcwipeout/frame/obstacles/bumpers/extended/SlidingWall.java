package org.stormdev.mcwipeout.frame.obstacles.bumpers.extended;
/*
  Created by Stormbits at 9/18/2023
*/

import lombok.Getter;
import org.bukkit.entity.ItemDisplay;
import org.stormdev.mcwipeout.utils.helpers.Cuboid;
import org.stormdev.mcwipeout.utils.helpers.WLocation;

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

        cuboid.blockList().forEachRemaining(block -> slidingBlockList.add(new SlidingBlock(WLocation.from(block), dx, dz)));
    }


    public static SlidingWall of(WLocation cornerLocation1, WLocation cornerLocation2, float dx, float dz, float xTranslation, float zTranslation, int delay) {
        return new SlidingWall(cornerLocation1, cornerLocation2, dx, dz, xTranslation, zTranslation, delay);
    }

    public void move() {
        slidingBlockList.forEach(slidingBlock -> slidingBlock.moveTo(xTranslation, 0, zTranslation, 32, true));


    }

    public void load() {
        slidingBlockList.forEach(SlidingBlock::load);

//        ItemDisplay itemDisplay = cornerLocation1.getWorld().spawn(cuboid.getCenter(), ItemDisplay.class);
//
//        ItemStack head = new ItemStack(Material.GHAST_TEAR, 1);
//        ItemMeta itemMeta = head.getItemMeta();
//        itemMeta.setCustomModelData(10017);
//        head.setItemMeta(itemMeta);
//
//        itemDisplay.setItemStack(head);
//
//        Location location = itemDisplay.getLocation();
//
//        location.setYaw(-90f);
//
//        itemDisplay.teleport(location);
//
//        displays.add(itemDisplay);
    }

    public void reset() {
        for (SlidingBlock slidingBlock : slidingBlockList) {
            slidingBlock.remove();
        }

        for (ItemDisplay display : displays) {
            display.remove();
        }

        displays.clear();

        slidingBlockList.clear();
    }

    public static double[] lerp3D(double amount, double x1, double y1, double z1, double x2, double y2, double z2) {
        return new double[]{x1 + (x2 - x1) * amount, y1 + (y2 - y1) * amount, z1 + (z2 - z1) * amount};
    }
}
