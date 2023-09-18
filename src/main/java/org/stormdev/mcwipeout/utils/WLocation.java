package org.stormdev.mcwipeout.utils;
/*
  Created by Stormbits at 9/8/2023
*/

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class WLocation {

    private static World world = Bukkit.getWorld("maps");

    @Getter
    @Setter
    private double x, y, z;

    @Getter
    @Setter
    private float pitch, yaw;

    public static WLocation from(double x, double y, double z) {
        return new WLocation(x, y, z, 0f, 0f);
    }

    public static WLocation from(Block block) {
        return new WLocation(block.getX(), block.getY(), block.getZ(), 0f, 0f);
    }

    private WLocation(double x, double y, double z, float pitch, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Location asLocation() {
        return new Location(world, x, y, z, pitch, yaw);
    }

    public Block asBlock() {
        return asLocation().getBlock();
    }

    public WLocation toCenter() {
        return new WLocation(x + 0.5, y, z + 0.5, pitch, yaw);
    }

    public WLocation applyRotation(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
        return this;
    }

    public String asStringLocation() {
        return this.x + ":" + this.y + ":" + this.z;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof WLocation && ((WLocation) obj).x == x && ((WLocation) obj).y == y && ((WLocation) obj).z == z;
    }
}
