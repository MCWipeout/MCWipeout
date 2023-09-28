package org.stormdev.mcwipeout.frame.obstacles.snake;
/*
  Created by Stormbits at 9/27/2023
*/

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.utils.helpers.WLocation;

import java.util.ArrayList;
import java.util.List;

public class SnakeObstacle extends Obstacle {

    @Getter
    private List<WLocation> locationList;

    private Material material;

    public SnakeObstacle(String fileId, Material material) {
        this.locationList = new ArrayList<>();
        this.material = material;

        List<String> strings = Wipeout.get().getConfig().getStringList("Snakes." + fileId);

        for (String string : strings) {
            String[] split = string.split(",");
            locationList.add(WLocation.from(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2])));
        }

        for (WLocation point : locationList) {
            point.asBlock().setType(Material.AIR);
        }
    }


    @Override
    public void handle(Event event) {

    }

    @Override
    public void run() {
        new BukkitRunnable() {
            int ticks = 0;
            int currentIndex = 0;

            @Override
            public void run() {
                if (!isEnabled()) {
                    this.cancel();
                }

                ticks++;

                if (ticks == 8) {
                    List<WLocation> points = getLocationList();
                    int previousIndex = currentIndex - 10;
                    if (currentIndex == getLocationList().size()) {
                        currentIndex = 0;
                    }

                    if (currentIndex - 10 < (getLocationList().size() - 1)) {
                        if (previousIndex >= 0) {
                            points.get(previousIndex).asBlock().setType(Material.AIR);
                        } else {
                            points.get(getLocationList().size() - 1 - (9 - currentIndex)).asBlock().setType(Material.AIR);
                        }
                    }

                    points.get(currentIndex).asBlock().setType(material);

                    currentIndex++;
                    ticks = 0;
                }
            }
        }.runTaskTimer(Wipeout.get(), 0L, 0L);

        new BukkitRunnable() {
            int ticks = 0;
            int currentIndex = 15;

            @Override
            public void run() {
                if (!isEnabled()) {
                    this.cancel();
                }

                ticks++;

                if (ticks == 8) {
                    List<WLocation> points = getLocationList();
                    int previousIndex = currentIndex - 10;
                    if (currentIndex == getLocationList().size()) {
                        currentIndex = 0;
                    }

                    if (currentIndex - 10 < (getLocationList().size() - 1)) {
                        if (previousIndex >= 0) {
                            points.get(previousIndex).asBlock().setType(Material.AIR);
                        } else {
                            points.get(getLocationList().size() - 1 - (9 - currentIndex)).asBlock().setType(Material.AIR);
                        }
                    }

                    points.get(currentIndex).asBlock().setType(material);

                    currentIndex++;
                    ticks = 0;
                }
            }
        }.runTaskTimer(Wipeout.get(), 0L, 0L);

        new BukkitRunnable() {
            int ticks = 0;
            int currentIndex = 30;

            @Override
            public void run() {
                if (!isEnabled()) {
                    this.cancel();
                }

                ticks++;

                if (ticks == 8) {
                    List<WLocation> points = getLocationList();
                    int previousIndex = currentIndex - 10;
                    if (currentIndex == getLocationList().size()) {
                        currentIndex = 0;
                    }

                    if (currentIndex - 10 < (getLocationList().size() - 1)) {
                        if (previousIndex >= 0) {
                            points.get(previousIndex).asBlock().setType(Material.AIR);
                        } else {
                            points.get(getLocationList().size() - 1 - (9 - currentIndex)).asBlock().setType(Material.AIR);
                        }
                    }

                    points.get(currentIndex).asBlock().setType(material);

                    currentIndex++;
                    ticks = 0;
                }
            }
        }.runTaskTimer(Wipeout.get(), 0L, 0L);
    }

    @Override
    public void reset() {
        getLocationList().forEach(location -> location.asBlock().setType(material));
    }

    @Override
    public void enable() {

    }

    @Override
    public void load() {

    }
}
