package org.stormdev.mcwipeout.frame.obstacles.water;
/*
  Created by Stormbits at 9/12/2023
*/

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.GenericLocationSet;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.DissapearingSection;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.JsonPlatformSection;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaterSlide extends Obstacle {

    private final List<WaterPreset> presetList;

    @Getter
    private final int downTime, runTime;

    private int totalDuration;

    @SneakyThrows
    public WaterSlide(String fileId, int downTime, int runTime) {
        this.presetList = new ArrayList<>();
        this.downTime = downTime;
        this.runTime = runTime;

        File file = new File(Wipeout.get().getDataFolder() + "/exported/", fileId + ".json");
        if (!file.exists()) {
            return;
        }

        GenericLocationSet[] genericLocations = Wipeout.getGson().fromJson(new FileReader(file), GenericLocationSet[].class);

        for (GenericLocationSet fromList : genericLocations) {
            presetList.add(new WaterPreset(fromList));
        }

        totalDuration = runTime * presetList.size();
        totalDuration += presetList.size() * downTime;
    }


    @Override
    public void handle(Event event) {
        if (!isEnabled()) return;
        if (event instanceof PlayerMoveEvent e) {
            if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;

            if (Wipeout.get().getPlayer(e.getPlayer().getUniqueId()).isInRegion("map_1_water")) {

                Location to = e.getTo();
                Block steppedOn = to.clone().add(0.0, -1.0, 0.0).getBlock();

                if (steppedOn.getType() == Material.WATER) {
                    Wipeout.get().getGameManager().getActiveMap().handleCheckPoint(e.getPlayer());
                }
            }
        }
        if (event instanceof BlockFromToEvent e) {
            if (e.getBlock().getType() == Material.WATER) {
                e.setCancelled(true);
            }
        }
    }

    @Override
    public void run() {
        new BukkitRunnable() {

            int timer = 0;
            int downTimeTimer = 0;
            int runTimeTimer = 0;
            int selection = 0;
            WaterPreset selected = null;

            @Override
            public void run() {

                if (!isEnabled()) {
                    this.cancel();
                }

                if (timer <= totalDuration) {

                    if (downTimeTimer < downTime) {
                        downTimeTimer++;
                    } else {

                        if (selected == null) {
                            selected = presetList.get(selection);
                            selected.place();

                            runTimeTimer = 0;

                            if (selection < presetList.size() - 1) {
                                selection++;
                            } else {
                                selection = 0;
                            }
                        } else {
                            if (runTimeTimer < runTime) {
                                runTimeTimer++;
                            } else {
                                downTimeTimer = 0;
                                selected.remove();
                                selected = null;
                            }
                        }
                    }

                    timer++;
                } else {
                    timer = 0;
                }
            }
        }.runTaskTimer(Wipeout.get(), 20L, 0L);
    }

    @Override
    public void reset() {
        presetList.forEach(WaterPreset::remove);
    }

    @Override
    public void enable() {

    }

    @Override
    public void load() {
        presetList.forEach(WaterPreset::remove);
    }
}
