package org.stormdev.mcwipeout.frame.obstacles.ice;
/*
  Created by Stormbits at 9/12/2023
*/

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.GenericLocationSet;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.water.WaterPreset;
import org.stormdev.mcwipeout.utils.WLocation;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FreezingIce extends Obstacle {

    private final List<WLocation> locationList;

    private final List<WLocation> temp;

    private IceState state;

    @SneakyThrows
    public FreezingIce(String fileId) {
        this.locationList = new ArrayList<>();

        this.temp = new ArrayList<>();
        state = IceState.FULL;

        File file = new File(Wipeout.get().getDataFolder() + "/exported/", fileId + ".json");
        if (!file.exists()) {
            return;
        }

        GenericLocationSet[] genericLocations = Wipeout.getGson().fromJson(new FileReader(file), GenericLocationSet[].class);

        for (GenericLocationSet fromList : genericLocations) {
            locationList.addAll(fromList.getMap().keySet());
        }
    }

    @Override
    public void handle(Event event) {
        if (event instanceof BlockFadeEvent e) {
            if (e.getBlock().getType() == Material.ICE || e.getBlock().getType() == Material.PACKED_ICE) {
                e.setCancelled(true);
            }
        }
    }

    @Override
    public void run() {
        new BukkitRunnable() {

            boolean reset = false;

            @Override
            public void run() {
                if (!isEnabled()) {
                    reset();
                    this.cancel();
                }

                if (!reset) {
                    switch (state) {
                        case FULL -> {
                            state = IceState.CRACK;
                            temp.clear();
                            temp.addAll(locationList);
                            ThreadLocalRandom r = ThreadLocalRandom.current();

                            locationList.forEach(point -> {
                                switch (r.nextInt(3)) {
                                    case 0 -> point.asBlock().setType(Material.ICE);
                                    case 1 -> point.asBlock().setType(Material.PACKED_ICE);
                                    case 2 -> point.asBlock().setType(Material.BLUE_ICE);
                                }
                            });
                        }
                        case CRACK -> {
                            state = IceState.CRACKED;
                            ThreadLocalRandom r = ThreadLocalRandom.current();
                            for (int i = 0; i < temp.size() / 2; i++) {
                                Block block = temp.remove(r.nextInt(temp.size())).asBlock();
                                block.setType(Material.AIR);
                            }
                        }
                        case CRACKED -> {
                            state = IceState.EMPTY;
                            ThreadLocalRandom r = ThreadLocalRandom.current();
                            for (int i = 0; i < temp.size() / 4; ++i) {
                                Block block = temp.remove(r.nextInt(temp.size())).asBlock();
                                block.setType(Material.AIR);
                            }
                        }
                        case EMPTY -> {
                            state = IceState.FULL;
                            reset = true;
                            for (int i = 0; i < temp.size(); i++) {
                                Block block = temp.remove(i).asBlock();
                                block.setType(Material.AIR);
                            }
                        }
                    }
                } else {
                    reset = false;
                }

            }
        }.runTaskTimer(Wipeout.get(), 20L, 20L);
    }

    @Override
    public void reset() {
        locationList.forEach(location -> location.asBlock().setType(Material.ICE));
    }

    @Override
    public void enable() {

    }

    @Override
    public void load() {

    }
}
