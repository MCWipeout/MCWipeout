package org.stormdev.mcwipeout.frame.obstacles.platforms;
/*
  Created by Stormbits at 9/12/2023
*/

import lombok.SneakyThrows;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanObject;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.DissapearingSection;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.JsonPlatformSection;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.MoveableSection;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DissapearingPlatforms extends Obstacle {


    private final List<DissapearingSection> sections;

    private final int totalDuration;

    @SneakyThrows
    public DissapearingPlatforms(String fileId, int totalDuration) {
        sections = new ArrayList<>();
        this.totalDuration = totalDuration;

        File file = new File(Wipeout.get().getDataFolder() + "/exported/", fileId + ".json");
        if (!file.exists()) {
            return;
        }

        JsonPlatformSection[] jsonPlatformSections = Wipeout.getGson().fromJson(new FileReader(file), JsonPlatformSection[].class);

        for (JsonPlatformSection fromList : jsonPlatformSections) {
            sections.add(new DissapearingSection(fromList));
        }
    }

    @Override
    public void handle(Event event) {

    }

    @Override
    public void run() {
        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                if (!isEnabled()) {
                    this.cancel();
                }

                if (timer <= totalDuration) {

                    for (DissapearingSection dissapearingSection : sections) {
                        if (dissapearingSection.getJsonSection().getSettings().getShowAt() == timer) {
                            dissapearingSection.show();
                        }
                        if (dissapearingSection.getJsonSection().getSettings().getHideAt() == timer) {
                            dissapearingSection.hide();
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
        sections.forEach(DissapearingSection::show);
    }

    @Override
    public void enable() {

    }

    @Override
    public void load() {
        sections.forEach(DissapearingSection::show);
    }
}
