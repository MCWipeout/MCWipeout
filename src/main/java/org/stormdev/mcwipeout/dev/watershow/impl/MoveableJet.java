package org.stormdev.mcwipeout.dev.watershow.impl;
/*
  Created by Stormbits at 8/21/2023
*/

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.stormdev.mcwipeout.Wipeout;

public class MoveableJet {

    public Location location;
    @Getter
    public float pitch = -90.0f;
    @Getter
    public float yaw = 0.0f;
    @Getter
    @Setter
    public float range;
    @Setter
    public boolean isOn = false;

    public MoveableJet(Location location, float range) {
        this.location = location;
        this.location.setPitch(this.pitch);
        this.location.setYaw(this.yaw);
        this.range = range;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        this.location.setYaw(yaw);
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        this.location.setPitch(pitch);
    }


    public void reset() {
        this.pitch = -90.0f;
        this.yaw = 0.0f;
        this.range = 1.2f;
        this.location.setPitch(-90.0f);
        this.location.setYaw(0.0f);
    }

    public void toggle() {
        if (!this.isOn) {
            this.isOn = true;
            new BukkitRunnable() {

                public void run() {
                    if (isOn) {
                        if (location.getChunk().isLoaded()) {
                            Vector v = location.getDirection().multiply(range);
                            FallingBlock fb = MoveableJet.this.location.getWorld().spawnFallingBlock(location, Material.BLUE_CONCRETE.createBlockData());
                            fb.setVelocity(v);
                            fb.setDropItem(false);
                        }
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(Wipeout.get(), 0L, 1L);
        } else {
            this.isOn = false;
        }
    }

    public void move(float pitch, float yaw, int time) {
        if (isOn) {
            new BukkitRunnable() {
                public int timer = 0;

                public void run() {
                    if (this.timer < time) {
                        ++this.timer;
                        if (pitch != 0.0f) {
                            setPitch(getPitch() - pitch / (float) time);
                        }
                        if (yaw != 0.0f) {
                            setYaw(getYaw() - yaw / (float) time);
                        }
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(Wipeout.get(), 0L, 1L);
        }
    }

    public void changeRange(float range, final int time) {
        if (isOn) {
            new BukkitRunnable() {
                public int timer = 0;
                public float step;

                {
                    step = getRange() - range;
                }

                public void run() {
                    if (this.timer < 60) {
                        this.timer++;
                        setRange(getRange() - step / (float) time);
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(Wipeout.get(), 0L, 1L);
        }
    }
}
