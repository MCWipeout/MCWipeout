package org.stormdev.mcwipeout.utils;
/*
  Created by Stormbits at 9/22/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MathUtils {

    public static Location getRelative(Location center, double distance, float angle, float pitch) {
        double yaw = Math.toRadians(angle);
        double X = center.getX() + distance * Math.cos(yaw) * -Math.sin(yaw);
        double Z = center.getZ() + distance * Math.sin(yaw);
        double Y = center.getY() * Math.cos(pitch);
        return new Location(Bukkit.getWorld("maps"), X, Y, Z);
    }
}
