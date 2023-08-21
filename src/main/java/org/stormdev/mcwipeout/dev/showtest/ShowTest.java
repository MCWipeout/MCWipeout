package org.stormdev.mcwipeout.dev.showtest;
/*
  Created by Stormbits at 8/21/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.dev.showtest.impl.BloomJet;
import org.stormdev.mcwipeout.dev.showtest.impl.MoveableJet;
import org.stormdev.mcwipeout.dev.showtest.impl.Shooter;

public class ShowTest {

    public ShowTest() {
        World world = Bukkit.getWorld("maps");
        new Shooter(new Location(world, 7, -16, -110), 0.5, 0.4, 0.5, 20, 100, 20, Material.BLUE_CONCRETE);
        new Shooter(new Location(world, 6, -16, -110), -0.5, 0.4, 0.5, 20, 100, 20, Material.BLUE_CONCRETE);
        new Shooter(new Location(world, 7, -16, -111), 0.5, 0.4, -0.5, 20, 100, 20, Material.BLUE_CONCRETE);
        new Shooter(new Location(world, 6, -16, -111), -0.5, 0.4, -0.5, 20, 100, 20, Material.BLUE_CONCRETE);


        new BloomJet(new Location(world, 1, -16, -116), 3, 0.8, 200).execute();

        MoveableJet jet = new MoveableJet(new Location(world, -1, -16, -111), 1.2f);
        new BukkitRunnable() {
            int timer = 0;
            boolean once1 = false;
            boolean once2 = false;
            boolean once3 = false;

            @Override
            public void run() {
                timer++;

                if (timer < 200) {
                    jet.toggle();
                }
                if (timer >= 250 && timer < 350 && !once1) {
                    once1 = true;
                    jet.move(45f, 0f, 40);
                }
                if (timer >= 350 && timer < 450 && !once2) {
                    once2 = true;
                    jet.move(-90f, 45, 60);
                }
                if (timer >= 450 && timer < 500 && !once3) {
                    once3 = true;
                    jet.reset();
                }
                if (timer > 550) {
                    jet.setOn(false);
                    this.cancel();
                }
            }
        }.runTaskTimer(Wipeout.get(), 300L, 0L);
    }
}
