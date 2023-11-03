package org.stormdev.mcwipeout.frame.game;
/*
  Created by Stormbits at 11/3/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.builder.QueueBuilder;
import org.stormdev.mcwipeout.Wipeout;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeleportTimer {
    private List<UUID> players;

    private CheckPoint checkPoint;

    private QueueBuilder<UUID> builder;

    public TeleportTimer(CheckPoint checkPoint) {
        this.players = new ArrayList<>();
        this.checkPoint = checkPoint;
    }

    public TeleportTimer(List<UUID> players, CheckPoint checkPoint) {
        this.players = players;
        this.checkPoint = checkPoint;
    }

    public void addPlayer(UUID uuid) {
        this.players.add(uuid);
    }

    public void start() {

        builder = new QueueBuilder<>();

        builder.addToQueue(players);

        builder.interval(5);
        builder.maxInterval(5);
        builder.consumer(uuid -> {
            Player target = Bukkit.getPlayer(uuid);
            if (target != null) checkPoint.reset(target);
        });

        builder.start(Wipeout.get());
    }

    public void reset() {
        builder.clearQueue();
        builder.stop();

        builder = null;
    }
}
