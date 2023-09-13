package org.stormdev.mcwipeout.frame.obstacles.patterns;
/*
  Created by Stormbits at 9/13/2023
*/

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.GenericLocationSet;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.water.WaterPreset;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PatternPlatforms extends Obstacle {

    private List<PatternPreset> patternPresetList;

    @Getter
    private final int downTime, runTime;

    private int totalDuration;

    @SneakyThrows
    public PatternPlatforms(String fileId, int downTime, int runTime) {
        this.downTime = downTime;
        this.runTime = runTime;
        this.patternPresetList = new ArrayList<>();

        File file = new File(Wipeout.get().getDataFolder() + "/exported/", fileId + ".json");

        if (!file.exists()) {
            return;
        }

        GenericLocationSet[] genericLocationSet = Wipeout.getGson().fromJson(new FileReader(file), GenericLocationSet[].class);

        for (GenericLocationSet fromList : genericLocationSet) {
            patternPresetList.add(new PatternPreset(fromList));
        }

        totalDuration = runTime * patternPresetList.size();
        totalDuration += patternPresetList.size() * downTime;
    }


    @Override
    public void handle(Event event) {

    }

    @Override
    public void run() {
        new BukkitRunnable() {

            int timer = 0;
            int downTimeTimer = 0;
            int runTimeTimer = 0;
            int selection = 0;
            PatternPreset selected = null;

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
                            selected = patternPresetList.get(selection);
                            selected.place();

                            runTimeTimer = 0;

                            if (selection < patternPresetList.size() - 1) {
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

    }

    @Override
    public void enable() {

    }

    @Override
    public void load() {

    }
}
