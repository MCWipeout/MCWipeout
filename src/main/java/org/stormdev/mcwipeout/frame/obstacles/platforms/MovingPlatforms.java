package org.stormdev.mcwipeout.frame.obstacles.platforms;
/*
  Created by Stormbits at 8/20/2023
*/

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.JsonPlatformSection;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.MoveableSection;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MovingPlatforms extends Obstacle {

    private final List<MoveableSection> sections;

    @SneakyThrows
    public MovingPlatforms(String fileId, int totalDuration) {
        sections = new ArrayList<>();

        File file = new File(Wipeout.get().getDataFolder() + "/exported/", fileId + ".json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            Wipeout.get().saveResource(fileId + ".json", false);
            return;
        }

        JsonPlatformSection[] jsonPlatformSections = Wipeout.getGson().fromJson(new FileReader(file), JsonPlatformSection[].class);

        for (JsonPlatformSection fromList : jsonPlatformSections) {
            sections.add(new MoveableSection(fromList));
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

                timer++;
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
