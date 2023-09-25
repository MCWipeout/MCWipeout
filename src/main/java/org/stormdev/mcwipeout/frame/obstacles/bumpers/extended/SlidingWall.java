package org.stormdev.mcwipeout.frame.obstacles.bumpers.extended;
/*
  Created by Stormbits at 9/18/2023
*/

import lombok.Getter;
import org.stormdev.mcwipeout.utils.Cuboid;
import org.stormdev.mcwipeout.utils.WLocation;

import java.util.ArrayList;
import java.util.List;

public class SlidingWall {

    private WLocation cornerLocation1;
    private WLocation cornerLocation2;

    private List<SlidingBlock> slidingBlockList;

    @Getter
    private int delay;

    private float xTranslation, zTranslation;

    private float dx, dz;

    private SlidingWall(WLocation cornerLocation1, WLocation cornerLocation2, float dx, float dz, float xTranslation, float zTranslation, int delay) {
        Cuboid cuboid = new Cuboid(cornerLocation1.asLocation(), cornerLocation2.asLocation());
        this.cornerLocation1 = cornerLocation1;
        this.cornerLocation2 = cornerLocation2;
        this.slidingBlockList = new ArrayList<>();
        this.dx = dx;
        this.dz = dz;
        this.delay = delay;
        this.xTranslation = xTranslation;
        this.zTranslation = zTranslation;

        cuboid.blockList().forEachRemaining(block -> slidingBlockList.add(new SlidingBlock(WLocation.from(block), dx, dz)));
    }


    public static SlidingWall of(WLocation cornerLocation1, WLocation cornerLocation2, float dx, float dz, float xTranslation, float zTranslation, int delay) {
        return new SlidingWall(cornerLocation1, cornerLocation2, dx, dz, xTranslation, zTranslation, delay);
    }

    public void move() {
        slidingBlockList.forEach(slidingBlock -> slidingBlock.moveTo(xTranslation, 0, zTranslation, 40, true));
    }

    public void load() {
        slidingBlockList.forEach(SlidingBlock::load);
    }

    public void reset() {
        for (SlidingBlock slidingBlock : slidingBlockList) {
            slidingBlock.remove();
        }

        slidingBlockList.clear();
    }
}
