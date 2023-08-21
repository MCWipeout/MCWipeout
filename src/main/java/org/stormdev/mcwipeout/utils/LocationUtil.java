package org.stormdev.mcwipeout.utils;
/*
  Created by Stormbits at 8/21/2023
*/

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LocationUtil {

    public static Location getCenter(@NotNull Location location) {
        return getCenter(location, true);
    }

    public static Location getPointOnCircle(@NotNull Location loc, boolean doCopy, double n, double n2, double n3) {
        return (doCopy ? loc.clone() : loc).add(Math.cos(n) * n2, n3, Math.sin(n) * n2);
    }

    public static List<Vector> getCirclePoints(Vector center, double radius, int total) {
        center = new Vector(0, 0, 0);
        List<Vector> points = new ArrayList<Vector>(total);
        double interval = Math.PI * 2 / (double) total;
        double currentAngle = Math.PI * 2;
        while (currentAngle > 0.0) {
            points.add(new Vector(center.getX() + radius * Math.sin(currentAngle), center.getY(), center.getZ() + radius * Math.cos(currentAngle)));
            currentAngle -= interval;
        }
        return points;
    }

    @NotNull
    public static Location getCenter(@NotNull Location location, boolean doVertical) {
        Location centered = location.clone();
        location.setX(location.getBlockX() + 0.5);
        location.setY(location.getBlockY() + (doVertical ? 0.5 : 0));
        location.setZ(location.getBlockZ() + 0.5);
        return location;
    }

    public static Vector getDirection(@NotNull Location from, @NotNull Location to) {
        Location origin = from.clone();
        origin.setDirection(to.toVector().subtract(origin.toVector()));
        return origin.getDirection();
    }
}
