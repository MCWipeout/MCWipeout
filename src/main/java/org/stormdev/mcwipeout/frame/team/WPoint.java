package org.stormdev.mcwipeout.frame.team;
/*
  Created by Stormbits at 2/12/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import toxi.geom.Vec3D;

public class WPoint {

    private double x;
    private double y;
    private double z;

    private World world;

    public WPoint(double x, double y, double z) {
        this.world = Bukkit.getWorld("maps");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static WPoint from(double x, double y, double z) {
        return new WPoint(x, y, z);
    }

    public Location toLocation() {
        return new Location(world, x, y, z);
    }

    public Block getBlock() {
        return toLocation().getBlock();
    }

    public Vec3D toVector() {
        return new Vec3D((float) x, (float) y, (float) z);
    }
}
