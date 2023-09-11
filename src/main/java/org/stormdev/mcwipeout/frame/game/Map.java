package org.stormdev.mcwipeout.frame.game;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.OOBArea;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.team.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Map {

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

    protected abstract void setupCheckpoints();

    protected abstract void setupObstacles();

    public void start() {
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
                Player player = Bukkit.getPlayer(uuid.getUuid());
                if (player != null) {
                    spawnPoint.reset(player);
                }
            });
        }
    }

    public Map() {
        this.checkPoints = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.teamsPlaying = new ArrayList<>();

        setupCheckpoints();
        setupObstacles();
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
                    player.sendMessage(ChatColor.GREEN + "Your checkpoint has been updated!");

                    Wipeout.get().getAdventure().player(player).playSound(Sound.sound(Key.key("wipeout:mcw.sfx.checkpoint"), Sound.Source.MASTER, 1.0f, 1.0f));
                }

                CheckPoint point = byRegion(region);

                if (point == null) return;

                if (checkPoints.indexOf(team.getCheckPointMap().get(player.getUniqueId())) < checkPoints.indexOf(point)) {
                    team.getCheckPointMap().replace(player.getUniqueId(), point);

                    player.sendMessage(ChatColor.GREEN + "Your checkpoint has been updated!");
                    Wipeout.get().getAdventure().player(player).playSound(Sound.sound(Key.key("wipeout:mcw.sfx.checkpoint"), Sound.Source.MASTER, 1.0f, 1.0f));
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

                Wipeout.get().getAdventure().player(player).sendActionBar(Component.text(ChatColor.RED + "Failed! You've been respawned."));
            }
        }
    }
}
