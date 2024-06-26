package org.stormdev.mcwipeout.frame.game;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.stormdev.chat.ActionBar;
import org.stormdev.chat.Titles;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.board.BoardManager;
import org.stormdev.mcwipeout.frame.obstacles.OOBArea;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.ObstacleRegion;
import org.stormdev.mcwipeout.frame.team.Team;
import org.stormdev.utils.StringUtils;
import org.stormdev.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Map {

    @Getter
    @Setter
    private String mapName;

    @Getter
    @Setter
    public List<Team> teamsPlaying;

    @Getter
    @Setter
    public List<CheckPoint> checkPoints;

    @Getter
    @Setter
    public List<Obstacle> obstacles;

    @Getter
    @Setter
    public CheckPoint spawnPoint;

    @Getter
    @Setter
    public CheckPoint finish;

    @Getter
    @Setter
    public List<OOBArea> oobAreas;

    @Getter
    @Setter
    public List<ObstacleRegion> obstacleRegions;

    @Getter
    @Setter
    private boolean enabled;

    protected abstract void setupCheckpoints();

    protected abstract void setupObstacles();

    public void start() {
        enabled = true;

        for (Obstacle obstacle : obstacles) {
            obstacle.setEnabled(true);
            obstacle.enable();
            obstacle.run();
        }

        for (Team team : teamsPlaying) {
            if (team.getFinishedMembers() == null) {
                team.setFinishedMembers(new ArrayList<>());
            } else {
                team.getFinishedMembers().clear();
            }
            if (team.getCheckPointMap() == null) {
                team.setCheckPointMap(new HashMap<>());
            } else {
                team.getCheckPointMap().clear();
            }
            team.getMembers().forEach(uuid -> {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    if (spawnPoint != null) {
                        spawnPoint.reset(player);
                    }
                }
            });
        }
    }

    public Map(String name) {
        this.checkPoints = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.teamsPlaying = new ArrayList<>();

        this.mapName = Utils.color(name);

        setupCheckpoints();
        setupObstacles();

        enabled = false;
    }

    public CheckPoint byRegion(String region) {
        for (CheckPoint checkPoint : checkPoints) {
            if (checkPoint.getRegion().equalsIgnoreCase(region)) {
                return checkPoint;
            }
        }
        return null;
    }

    public void moveCheckPoint(String region, Player player) {
        if (player.getGameMode() == GameMode.SPECTATOR) return;
        for (Team team : teamsPlaying) {
            if (team.containsPlayer(player)) {
                if (!team.getCheckPointMap().containsKey(player.getUniqueId())) {
                    team.getCheckPointMap().put(player.getUniqueId(), byRegion(region));
                    player.sendMessage(StringUtils.hex("&8[#8eee3a✔&8] #F7CE50Your checkpoint has been updated"));
                    Titles.sendTitle(player, "", StringUtils.hex("#F7CE50Checkpoint set!"));

                    Wipeout.get().getAdventure().player(player).playSound(Sound.sound(Key.key("wipeout:mcw.sfx.checkpoint"), Sound.Source.MASTER, 1.0f, 1.0f));

                    if (spawnPoint.getObstacleRegion() != null) {
                        BoardManager.getInstance().finishObstacle(player, spawnPoint.getObstacleRegion());
                    }

                    if (byRegion(region).getObstacleRegion() != null)
                        Wipeout.get().getObstacleBar().updateBossBar(player, byRegion(region).getObstacleRegion());
                }

                CheckPoint point = byRegion(region);

                if (point == null) return;

                if (checkPoints.indexOf(team.getCheckPointMap().get(player.getUniqueId())) < checkPoints.indexOf(point)) {
                    ObstacleRegion old = team.getCheckPointMap().get(player.getUniqueId()).getObstacleRegion();
                    team.getCheckPointMap().replace(player.getUniqueId(), point);

                    player.sendMessage(StringUtils.hex("&8[#8eee3a✔&8] #F7CE50Your checkpoint has been updated"));
                    Titles.sendTitle(player, "", StringUtils.hex("#F7CE50Checkpoint set!"));

                    Wipeout.get().getAdventure().player(player).playSound(Sound.sound(Key.key("wipeout:mcw.sfx.checkpoint"), Sound.Source.MASTER, 1.0f, 1.0f));

                    if (old != null) {
                        BoardManager.getInstance().finishObstacle(player, old);
                    }

                    if (point.getObstacleRegion() != null)
                        Wipeout.get().getObstacleBar().updateBossBar(player, point.getObstacleRegion());
                    else Wipeout.get().getObstacleBar().updateBossBar(player, ObstacleRegion.FINISH);
                }
            }
        }
    }

    public void handleCheckPoint(Player player) {
        for (Team team : teamsPlaying) {
            if (team.containsPlayer(player)) {

                if (!team.getCheckPointMap().containsKey(player.getUniqueId())) {
                    spawnPoint.reset(player);
                } else {
                    team.getCheckPointMap().get(player.getUniqueId()).reset(player);
                }

                ActionBar.sendActionBar(player, "&c&lFAIL! &r&cYou've been respawned");
            }
        }
    }
}
