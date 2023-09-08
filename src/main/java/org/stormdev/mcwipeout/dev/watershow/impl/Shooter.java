package org.stormdev.mcwipeout.dev.watershow.impl;
/*
  Created by Stormbits at 8/21/2023
*/

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.stormdev.mcwipeout.Wipeout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Shooter {
    private List<Item> items = new ArrayList<>();

    public Shooter(final Location sl, final double xd, final double yd, final double zd, final int fadeIn, final int stay, final int fadeOut, final Material mat) {
        this.remover();
        new BukkitRunnable() {
            int time;
            double dx = 0.0;
            double dy = 0.0;
            double dz = 0.0;

            public void run() {
                ++this.time;
                if (this.time >= fadeIn + stay + fadeOut) {
                    this.cancel();
                }
                if (this.time >= 0 && this.time <= fadeIn) {
                    this.dx = xd / (double) fadeIn * (double) this.time;
                    this.dy = yd / (double) fadeIn * (double) this.time;
                    this.dz = zd / (double) fadeIn * (double) this.time;
                } else if (this.time >= fadeIn + stay && this.time <= fadeIn + stay + fadeOut) {
                    this.dx = xd / (double) fadeOut * (double) (fadeOut - (this.time - fadeIn - stay));
                    this.dy = yd / (double) fadeOut * (double) (fadeOut - (this.time - fadeIn - stay));
                    this.dz = zd / (double) fadeOut * (double) (fadeOut - (this.time - fadeIn - stay));
                } else {
                    this.dx = xd;
                    this.dy = yd;
                    this.dz = zd;
                }
                MaterialData matd = new MaterialData(mat);
                FallingBlock fb = Objects.requireNonNull(sl.getWorld()).spawnFallingBlock(sl, matd);
                fb.setDropItem(false);
                fb.setHurtEntities(false);
                fb.setVelocity(new Vector(this.dx, this.dy, this.dz));
            }
        }.runTaskTimer(Wipeout.get(), 0L, 1L);
    }

    public void remover() {
        new BukkitRunnable() {

            public void run() {
                ArrayList<Item> removes = new ArrayList<Item>();
                for (Item item : Shooter.this.items) {
                    if (item.isOnGround()) {
                        item.remove();
                        removes.add(item);
                        continue;
                    }
                    item.setRotation(45.0f, 45.0f);
                }
                for (Item it : removes) {
                    Shooter.this.items.remove(it);
                }
                removes.clear();
            }
        }.runTaskTimer(Wipeout.get(), 0L, 1L);
    }
}
