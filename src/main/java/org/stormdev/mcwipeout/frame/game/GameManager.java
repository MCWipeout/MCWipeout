package org.stormdev.mcwipeout.frame.game;
/*
  Created by Stormbits at 2/12/2023
*/

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.team.Team;
import org.stormdev.mcwipeout.frame.team.WipeoutPlayer;
import org.stormdev.utils.Color;
import org.stormdev.utils.StringUtils;
import org.stormdev.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GameManager {

    private final Wipeout plugin;

    @Getter
    private BukkitTask task;

    @Getter
    @Setter
    private Map activeMap;

    @Getter
    private List<Team> finishedTeams;

    @Getter
    private List<UUID> finishedPlayers;

    @Getter
    @Setter
    private boolean frozen;

    private Stopwatch stopwatch;

    private BukkitTask timerTask;

    private BossBar bossBar;

    @Getter
    @Setter
    private GameType type;

    public GameManager(Wipeout plugin) {
        this.plugin = plugin;
        this.activeMap = null;
        this.finishedTeams = new ArrayList<>();
        this.finishedPlayers = new ArrayList<>();
        this.frozen = false;
        this.task = null;
        this.type = GameType.TEAMS;
    }

    public void runTimer() {
        bossBar = Bukkit.createBossBar(Color.colorize("&eTime left: 600 seconds"), BarColor.RED, BarStyle.SOLID);
        bossBar.setVisible(true);
        bossBar.setProgress(1.0);

        Bukkit.getOnlinePlayers().forEach(x -> bossBar.addPlayer(x));
        this.timerTask = new BukkitRunnable() {

            int timer = 600;

            @Override
            public void run() {
                if (timer > 1) {
                    timer--;
                    bossBar.setProgress(timer / 600);
                    int S = timer % 60;
                    int H = timer / 60;
                    int M = H % 60;
                    H = H / 60;

                    bossBar.setTitle(Color.colorize("&eTime left: " + M + ":" + S));

                } else {
                    stopActiveMap();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void start() {
        activeMap.start();

        frozen = true;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                activeMap.getSpawnPoint().reset(player);
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.JUMP);
            }
        }

        for (Team team : activeMap.getTeamsPlaying()) {
            for (WipeoutPlayer wipeoutPlayer : team.getMembers()) {
                Player player = Bukkit.getPlayer(wipeoutPlayer.getUuid());
                if (player == null) continue;

                getPlayersExcludeTeamMembers(team.getUUIDMembers(), player).forEach(x -> {

                    player.hidePlayer(plugin, x);
                });
            }
        }

        new BukkitRunnable() {

            int secondsLeft = 11;

            @Override
            public void run() {
                if (secondsLeft == 1) {
                    frozen = false;
                    this.cancel();
                }
                secondsLeft--;

                if (secondsLeft == 7) {
                    Bukkit.getOnlinePlayers().forEach(x -> plugin.getAdventure().player(x).playSound(Sound.sound(Key.key("wipeout:mcw.countdown"), Sound.Source.MASTER, 0.5f, 1.0f)));
                }

                switch (secondsLeft) {
                    case 0 -> {
                        stopwatch = Stopwatch.createStarted();

                        if (type == GameType.SOLO) {
                            runTimer();
                        }

                        Bukkit.getOnlinePlayers().forEach(x -> plugin.getAdventure().player(x).showTitle(Title.title(Component.text(ChatColor.GREEN + "GO GO GO!"), Component.empty())));
                    }
                    case 1 -> {
                        Bukkit.getOnlinePlayers().forEach(x -> plugin.getAdventure().player(x).showTitle(Title.title(Component.text(ChatColor.GREEN + "1 second!"), Component.empty())));

                    }
                    case 2, 3, 4, 5 -> {
                        Bukkit.getOnlinePlayers().forEach(x -> plugin.getAdventure().player(x).showTitle(Title.title(Component.text(ChatColor.GREEN + "" + secondsLeft + " seconds!"), Component.empty())));
                    }
                    case 6, 7, 8, 9, 10 ->
                            Bukkit.broadcastMessage(ChatColor.GREEN + "The game will commence in " + secondsLeft + " seconds.");
                }

            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public void finish(String region, Player player) {
        if (type == GameType.SOLO) {
            if (activeMap == null || activeMap.getTeamsPlaying() == null) return;
            for (Team team : activeMap.getTeamsPlaying()) {
                if (team.containsPlayer(player)) {
                    if (team.getFinishedMembers().contains(player.getUniqueId())) return;
                    team.getFinishedMembers().add(player.getUniqueId());

                    team.getCheckPointMap().put(player.getUniqueId(), activeMap.getFinish());

                    plugin.getAdventure().player(player).playSound(Sound.sound(Key.key("wipeout:mcw.gamefinish"), Sound.Source.MASTER, 1.0f, 1.0f));

                    player.sendMessage(ChatColor.GREEN + "You have finished!");
                    int timer = (int) stopwatch.elapsed(TimeUnit.MILLISECONDS);

                    Bukkit.broadcastMessage(StringUtils.hex("&aPlayer &e" + player.getName() + " &ahas finished! &7Time: &e" + Utils.formatTime(timer) + "&7, Position: &e" + (finishedTeams.size() + 1)));

                    player.setGameMode(GameMode.SPECTATOR);

                    getPlayersExcludeTeamMembers(team.getUUIDMembers(), player).forEach(x -> {
                        player.showPlayer(plugin, x);
                    });

                    if (team.getFinishedMembers().size() == team.getMembers().size()) {
                        team.getFinishedMembers().forEach(x -> {
                            if (Bukkit.getPlayer(x) != null) {
                                Bukkit.getPlayer(x).sendMessage(ChatColor.GREEN + "You have finished! Time: " + stopwatch);
                            }
                        });

                        if (region.contains("finish")) team.finish(player, activeMap.getFinish());

                        Bukkit.getOnlinePlayers().forEach(player1 -> plugin.getAdventure().player(player1).playSound(Sound.sound(Key.key("wipeout:mcw.gamefinish"), Sound.Source.MASTER, 0.5f, 1.0f)));

                        finishedTeams.add(team);

                        if (finishedTeams.size() == getMaxPlayersNeeded()) {
                            stopActiveMap();
                        }
                    }
                }
            }
        } else if (type == GameType.TEAMS) {
            if (activeMap == null || activeMap.getTeamsPlaying() == null) return;
            for (Team team : activeMap.getTeamsPlaying()) {
                if (team.containsPlayer(player)) {
                    if (team.getFinishedMembers().contains(player.getUniqueId())) return;
                    team.getFinishedMembers().add(player.getUniqueId());

                    team.getCheckPointMap().put(player.getUniqueId(), activeMap.getFinish());

                    plugin.getAdventure().player(player).playSound(Sound.sound(Key.key("wipeout:mcw.gamefinish"), Sound.Source.MASTER, 1.0f, 1.0f));

                    player.sendMessage(ChatColor.GREEN + "You have finished!");
                    int timer = (int) stopwatch.elapsed(TimeUnit.MILLISECONDS);
                    Bukkit.broadcastMessage(StringUtils.hex("&aPlayer " + team.getColor() + player.getName() + " &ahas finished in place " + (finishedPlayers.size() + 1) + "! (" + team.getFinishedMembers().size() + " / " + team.getMembers().size() + ")"));

                    player.setGameMode(GameMode.SPECTATOR);

                    finishedPlayers.add(player.getUniqueId());

                    getPlayersExcludeTeamMembers(team.getUUIDMembers(), player).forEach(x -> {
                        player.showPlayer(plugin, x);
                    });

                    if (team.getFinishedMembers().size() == team.getMembers().size()) {
                        team.getFinishedMembers().forEach(x -> {
                            if (Bukkit.getPlayer(x) != null) {
                                Bukkit.getPlayer(x).sendMessage(ChatColor.GREEN + "Your team has finished! Time: " + stopwatch);
                            }
                        });

                        if (region.contains("finish")) team.finish(player, activeMap.getFinish());

                        Bukkit.getOnlinePlayers().forEach(player1 -> plugin.getAdventure().player(player1).playSound(Sound.sound(Key.key("wipeout:mcw.gamefinish"), Sound.Source.MASTER, 0.5f, 1.0f)));

                        Bukkit.broadcastMessage(Color.colorize("&8&m-----------------------------------"));
                        switch (finishedTeams.size()) {
                            case 0 -> {
                                Bukkit.broadcastMessage(StringUtils.hex("&aTeam " + team.getColor() + team.getId() + " &ahas finished in 1st place! Time: " + Utils.formatTime(timer)));
                            }
                            case 1 -> {
                                Bukkit.broadcastMessage(StringUtils.hex("&aTeam " + team.getColor() + team.getId() + " &ahas finished in 2nd place! Time: " + Utils.formatTime(timer)));
                            }
                            case 2 -> {
                                Bukkit.broadcastMessage(StringUtils.hex("&aTeam " + team.getColor() + team.getId() + " &ahas finished in 3rd place! Time: " + Utils.formatTime(timer)));
                            }
                            default ->
                                    Bukkit.broadcastMessage(StringUtils.hex("&aTeam " + team.getColor() + team.getId() + " &ahas finished in place " + (finishedTeams.size() + 1) + "! Time: " + Utils.formatTime(timer)));
                        }

                        Bukkit.broadcastMessage(Color.colorize("&8&m-----------------------------------"));

                        finishedTeams.add(team);

                        if (finishedTeams.size() == activeMap.getTeamsPlaying().size()) {
                            stopActiveMap();
                        }

                        if (finishedTeams.size() == getMaxPlayersNeeded()) {
                            stopActiveMap();
                        }

                        if (finishedTeams.size() == 3) {
                            endOfGameTimer();
                        }
                    }
                }
            }
        }
    }

    public void stopActiveMap() {
        if (task != null) {
            task.cancel();
        }

        if (timerTask != null) {
            timerTask.cancel();
        }

        task = null;
        timerTask = null;
        bossBar = null;

        for (Player player : Bukkit.getOnlinePlayers()) {
            plugin.getAdventure().player(player).playSound(Sound.sound(Key.key("wipeout:mcw.game_end"), Sound.Source.MASTER, 1.0f, 1.0f));
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(new Location(Bukkit.getWorld("maps"), -94.5, 35, -361.5, 90F, 0.0F));
            Bukkit.getOnlinePlayers().forEach(x -> {
                if (!x.getUniqueId().equals(player.getUniqueId())) {
                    player.showPlayer(plugin, x);
                }
            });
            if (stopwatch != null) {
                int timer = (int) stopwatch.elapsed(TimeUnit.MILLISECONDS);

                player.sendMessage(Color.colorize("&aYour personal time: " + Utils.formatTime(timer)));
            }

            for (Team team : activeMap.getTeamsPlaying()) {
                if (team.containsPlayer(player)) {
                    getPlayersExcludeTeamMembers(team.getUUIDMembers(), player).forEach(x -> player.showPlayer(plugin, x));
                }
            }
        }
        if (!finishedTeams.isEmpty()) {
            Bukkit.broadcastMessage(Color.colorize("&8&m-----------------------------------"));
            int maxI = 3;

            if (finishedTeams.size() < maxI) {
                maxI = finishedTeams.size();
            }

            for (int i = 0; i < maxI; i++) {
                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage(ChatColor.GREEN + "" + (i + 1) + ": " + StringUtils.hex(finishedTeams.get(i).getColor() + finishedTeams.get(i).getId()));
                StringBuilder builder = new StringBuilder();
                finishedTeams.get(i).getMembers().forEach(x -> builder.append(Bukkit.getOfflinePlayer(x.getUuid()).getName()).append(" "));
                if (type == GameType.TEAMS) {
                    Bukkit.broadcastMessage(ChatColor.GRAY + "Players: " + builder);
                } else {
                    Bukkit.broadcastMessage(ChatColor.GRAY + "" + builder);
                }
                Bukkit.broadcastMessage(" ");
            }


            Bukkit.broadcastMessage(Color.colorize("&8&m-----------------------------------"));
        }

        stopwatch = null;

        finishedTeams.clear();
        finishedPlayers.clear();

        activeMap.getObstacles().forEach(Obstacle::reset);

        activeMap.getObstacles().forEach(obstacle -> obstacle.setEnabled(false));

        activeMap.getTeamsPlaying().forEach(team -> team.getCheckPointMap().clear());
        activeMap.getTeamsPlaying().clear();

        activeMap = null;
    }

    public void endOfGameTimer() {
        task = new BukkitRunnable() {

            int secondsLeft = 181;

            @Override
            public void run() {
                if (secondsLeft == 0) {
                    stopActiveMap();
                    cancel();
                }

                secondsLeft--;

                if (secondsLeft == 180) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "There are 3 minutes left before the game is concluded!");
                }

                if (secondsLeft == 120) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "There are 2 minutes left before the game is concluded!");
                }

                if (secondsLeft == 60) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "There is 1 minute left before the game is concluded!");
                }

                if (secondsLeft < 10 && secondsLeft > 0) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "There are " + secondsLeft + " seconds left before the game is concluded!");
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public List<Player> getPlayersExcludeTeamMembers(List<UUID> members, Player player) {
        List<Player> playerList = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (members.contains(onlinePlayer.getUniqueId()) || onlinePlayer.getUniqueId().equals(player.getUniqueId()))
                continue;

            playerList.add(onlinePlayer);
        }

        return playerList;
    }

    public int getMaxPlayersNeeded() {
        int total = 0;
        for (Team team : getActiveMap().getTeamsPlaying()) {
            if (Bukkit.getPlayer(team.getUUIDMembers().get(0)) != null) {
                total++;
            }
        }
        return total;
    }
}
