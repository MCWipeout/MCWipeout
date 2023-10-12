package org.stormdev.mcwipeout.frame.obstacles.sweeper;
/*
  Created by Stormbits at 9/24/2023
*/

import lombok.Getter;
import org.stormdev.mcwipeout.frame.obstacles.patterns.FakePatternBlock;
import org.stormdev.mcwipeout.utils.helpers.WLocation;
import org.stormdev.mcwipeout.utils.math.MathUtils;

public class SweeperBlockObject {

    private float dx, dz;

    @Getter private FakePatternBlock fakePatternBlock;

    @Getter
    private boolean inverse;

    @Getter private double radius;

    public SweeperBlockObject(WLocation wLocation, float dx, float dz, boolean inverse, double radius) {
        this.dx = dx;
        this.dz = dz;


        this.fakePatternBlock = new FakePatternBlock(WLocation.from(wLocation.getX(), wLocation.getY(), wLocation.getZ()).asLocation(), wLocation.asBlock().getType(), true, false);
        this.inverse = inverse;
        this.radius = radius;
    }

    public void teleport(float x, float z, float angle) {
        double radians = 0;
        if (inverse) {
            radians = MathUtils.getFixedYaw(angle + 180) + 4;
        } else {
            radians = angle;
        }

        radians = Math.toRadians(radians);
        double xNew = (radius * Math.sin(radians));
        double zNew = (radius * Math.cos(radians));

        fakePatternBlock.teleport((float) (x + dx + xNew), (float) (z + dz + zNew));
    }

    public void remove() {
        fakePatternBlock.remove();
    }
}
