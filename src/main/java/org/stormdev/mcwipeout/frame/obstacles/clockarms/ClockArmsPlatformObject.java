package org.stormdev.mcwipeout.frame.obstacles.clockarms;
/*
  Created by Stormbits at 9/24/2023
*/

import lombok.Getter;
import org.stormdev.mcwipeout.frame.obstacles.patterns.FakePatternBlock;
import org.stormdev.mcwipeout.utils.helpers.WLocation;
import org.stormdev.mcwipeout.utils.math.MathUtils;

public class ClockArmsPlatformObject {

    private float dx, dz;

    private FakePatternBlock fakePatternBlock;

    @Getter
    private boolean inverse;

    private double radius;

    public ClockArmsPlatformObject(WLocation wLocation, float dx, float dz, boolean inverse, double radius) {
        this.dx = dx;
        this.dz = dz;


        this.fakePatternBlock = new FakePatternBlock(WLocation.from(wLocation.getX(), wLocation.getY() - 0.4, wLocation.getZ()).asLocation(), wLocation.asBlock().getType(), true, false);
        this.inverse = inverse;
        this.radius = radius;
    }

    public void teleport(float x, float z, float angle) {
        float radius = 6.5f;
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
