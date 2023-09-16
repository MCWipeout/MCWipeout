package org.stormdev.mcwipeout.frame.obstacles.fans;
/*
  Created by Stormbits at 9/11/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;

import java.util.List;

public class FanWall extends Obstacle {

    private final List<FanObject> fanObjectList;

    private final int totalDuration;

    public FanWall(List<FanObject> fanObjects, int totalDuration) {
        this.fanObjectList = fanObjects;
        this.totalDuration = totalDuration;
    }


    @Override
    public void handle(Event event) {
        if (!isEnabled()) return;
        if (event instanceof PlayerMoveEvent e) {
            Player player = e.getPlayer();

            if (player.getGameMode() == GameMode.SPECTATOR) return;

            Location to = e.getTo();
            if (to == null) return;
            fanObjectList.forEach(fanObject -> {
                if (fanObject.enabled()) {
                    if (fanObject.fanBoundingBox().isInWithMarge(to, 0.5)) fanObject.fling(player);
                }
            });
        }
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

                    for (FanObject fanObject : fanObjectList) {
                        if (fanObject.enableAt() == timer) {
                            fanObject.toggle(true);
                        } else if (fanObject.disableAt() == timer) {
                            fanObject.toggle(false);
                        }

                        if (fanObject.enabled()) {
                            fanObject.displayParticle();
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                if (fanObject.fanBoundingBox().isInWithMarge(onlinePlayer.getLocation(), 0.5))
                                    fanObject.fling(onlinePlayer);
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
