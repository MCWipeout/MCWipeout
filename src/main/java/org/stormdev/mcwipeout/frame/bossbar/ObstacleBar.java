package org.stormdev.mcwipeout.frame.bossbar;
/*
  Created by Stormbits at 10/10/2023
*/

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.ObstacleRegion;
import org.stormdev.utils.Color;
import org.stormdev.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ObstacleBar {

    @Getter
    private static ObstacleBar instance;

    private Wipeout plugin;

    private Map<UUID, BossBar> bossBarMap;

    @Getter
    private Map<UUID, ObstacleRegion> currentObstacles;

    @Getter
    private Map<UUID, Integer> obstacleIndex;

    public ObstacleBar(Wipeout plugin) {
        this.plugin = plugin;
        instance = this;
        this.bossBarMap = new HashMap<>();
        this.currentObstacles = new HashMap<>();
        this.obstacleIndex = new HashMap<>();
    }

    public void startMap(Player player) {
        BossBar bossBar = Bukkit.createBossBar(StringUtils.hex("&#F7CE50Current obstacle: &#BF1542None"), BarColor.YELLOW, BarStyle.SOLID);
        bossBar.setVisible(true);
        bossBar.setProgress(1.0);

        bossBar.addPlayer(player);

        bossBarMap.put(player.getUniqueId(), bossBar);
        obstacleIndex.put(player.getUniqueId(), 0);
    }

    public void updateBossBar(Player player, ObstacleRegion obstacleRegion) {
        BossBar bossBar = bossBarMap.get(player.getUniqueId());
        if (bossBar == null) {
            return;
        }

        if (obstacleRegion != ObstacleRegion.FINISH) {
            bossBar.setTitle(Color.colorize("&e&lCurrent obstacle: &r&c" + obstacleRegion.getText()));
            obstacleIndex.put(player.getUniqueId(), obstacleIndex.getOrDefault(player.getUniqueId(), 0) + 1);
        } else {
            bossBar.setTitle(Color.colorize("&e&lCurrent obstacle: &r&cFinish"));
        }

        currentObstacles.replace(player.getUniqueId(), obstacleRegion);
    }

    public void disable(Player player) {
        BossBar bossBar = bossBarMap.get(player.getUniqueId());
        if (bossBar == null) {
            return;
        }

        bossBar.setVisible(false);
        bossBar.removeAll();

        bossBarMap.remove(player.getUniqueId());
        currentObstacles.remove(player.getUniqueId());
        obstacleIndex.remove(player.getUniqueId());

        bossBar = null;
    }
}
