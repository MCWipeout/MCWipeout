package org.stormdev.mcwipeout.utils.math;
/*
  Created by Stormbits at 9/22/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class MathUtils {

    public static Location getRelative(Location center, double distance, float angle, float pitch) {
        double yaw = Math.toRadians(angle);
        double X = center.getX() + distance * Math.cos(yaw) * -Math.sin(yaw);
        double Z = center.getZ() + distance * Math.sin(yaw);
        double Y = center.getY() * Math.cos(pitch);
        return new Location(Bukkit.getWorld("maps"), X, Y, Z);
    }

    public static float getFixedYaw(float yaw) {
        if (yaw > 360) {
            return Math.abs(360 - yaw);
        }

        return yaw;
    }

    public static float getPitch(Location p1, Location p2) {
        Location p1_clone = p1.clone();
        Location p2_clone = p2.clone();
        p1_clone.setDirection(p2_clone.subtract(p1_clone.toVector()).toVector());
        return p1_clone.getPitch();
    }

    public static float getAngle(Vector point1, Vector point2) {
        double dx = point2.getX() - point1.getX();
        double dz = point2.getZ() - point1.getZ();
        float angle = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
        if (angle < 0.0F) {
            angle += 360.0F;
        }
        return angle;
    }
}
