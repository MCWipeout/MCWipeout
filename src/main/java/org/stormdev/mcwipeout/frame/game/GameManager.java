package org.stormdev.mcwipeout.frame.game;
/*
  Created by Stormbits at 2/12/2023
*/

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.stormdev.builder.CustomItemBuilder;
import org.stormdev.chat.Chat;
import org.stormdev.chat.Titles;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.board.Board;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.team.Team;
import org.stormdev.mcwipeout.frame.team.WipeoutPlayer;
import org.stormdev.mcwipeout.utils.helpers.CachedItems;
import org.stormdev.utils.Color;
import org.stormdev.utils.StringUtils;
import org.stormdev.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
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
    private final List<Team> finishedTeams;

    @Getter
    private final List<UUID> finishedPlayers;

    @Getter
    @Setter
    private boolean frozen;
    @Getter
    public Stopwatch stopwatch;

    private BukkitTask timerTask;

    @Getter
    private BossBar bossBar;

    @Getter
    private java.util.Map<UUID, Integer> playerTimers;

    @Getter
    private java.util.Map<Team, Integer> teamTimers;

    @Getter
    @Setter
    private GameType type;

    public GameManager(Wipeout plugin) {
        this.plugin = plugin;
        activeMap = null;
        this.finishedTeams = new ArrayList<>();
        this.finishedPlayers = new ArrayList<>();
        this.playerTimers = new HashMap<>();
        this.teamTimers = new HashMap<>();
        this.frozen = false;
        this.task = null;
        this.type = GameType.TEAMS;
    }

    public void runTimer() {
        bossBar = Bukkit.createBossBar(Color.colorize("&eTime left: 600 seconds"), BarColor.YELLOW, BarStyle.SOLID);
        bossBar.setVisible(true);
        bossBar.setProgress(1.0);


        Bukkit.getOnlinePlayers().forEach(x -> bossBar.addPlayer(x));
        this.timerTask = new BukkitRunnable() {

            int timer = 600;

            @Override
            public void run() {
                if (timer > 1) {
                    timer--;
                    bossBar.setProgress(Math.round((float) timer / 600));
                    int S = timer % 60;
                    int H = timer / 60;
                    int M = H % 60;
                    H = H / 60;

                    if (S < 10) {
                        bossBar.setTitle(StringUtils.hex("#F7CE50Time Left: #EAAB30" + M + ":0" + S));
                    } else {
                        bossBar.setTitle(StringUtils.hex("#F7CE50Time Left: #EAAB30" + M + ":" + S));
                    }

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
            boolean isFound = false;
            for (Team team : activeMap.getTeamsPlaying()) {
                if (team.getUUIDMembers().contains(player.getUniqueId())) {
                    isFound = true;
                }
            }

            if (!isFound) {
                if (activeMap.getSpawnPoint() != null) {
                    activeMap.getSpawnPoint().reset(player);
                    player.setGameMode(GameMode.SPECTATOR);
                }
                continue;
            }

            if (!player.hasPermission("wipeout.play")) continue;
            if (activeMap.getSpawnPoint() != null) {
                Board.getInstance().addObstacles(player, activeMap.getObstacleRegions());
                getTeamFromUUID(player.getUniqueId()).getCheckPointMap().put(player.getUniqueId(), activeMap.getSpawnPoint());
                activeMap.getSpawnPoint().reset(player);
                Wipeout.get().getObstacleBar().startMap(player);
                Wipeout.get().getObstacleBar().updateBossBar(player, activeMap.getSpawnPoint().getObstacleRegion());
            }
            player.setGameMode(GameMode.ADVENTURE);
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.JUMP);

            player.getInventory().setItem(4, CachedItems.rewindItem);
            player.getInventory().setItem(8, CachedItems.playersOffItem);
        }

        for (Team team : activeMap.getTeamsPlaying()) {
            for (WipeoutPlayer wipeoutPlayer : team.getMembers()) {
                Player player = Bukkit.getPlayer(wipeoutPlayer.getUuid());
                if (player == null) continue;

                getPlayersExcludeTeamMembers(team.getUUIDMembers(), player).forEach(x -> {
                    player.hidePlayer(Wipeout.get(), x);
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
                    Bukkit.getOnlinePlayers().forEach(x -> plugin.getAdventure().player(x).playSound(Sound.sound(Key.key("wipeout:mcw.sfx.countdown"), Sound.Source.MASTER, 0.5f, 1.0f)));
                }

                switch (secondsLeft) {
                    case 0 -> {
                        stopwatch = Stopwatch.createStarted();

                        if (type == GameType.SOLO) {
                            runTimer();
                        }

                        Bukkit.getOnlinePlayers().forEach(x -> Titles.sendTitle(x, 0, 100, 20, StringUtils.hex("#EAAB30&lGO!"), ""));
                        Bukkit.broadcastMessage(StringUtils.hex("#5A6E9C&l| #EAAB30&lGO GO GO!"));
                    }
                    case 1 -> {
                        Bukkit.getOnlinePlayers().forEach(x -> Titles.sendTitle(x, 0, 100, 20, StringUtils.hex("#EAAB30&l1"), ""));
                        Bukkit.broadcastMessage(StringUtils.hex("#5A6E9C&l| #A1BDD7Game starts in #8eee3a1 second"));

                    }
                    case 2, 3, 4, 5 -> {
                        Bukkit.getOnlinePlayers().forEach(x -> Titles.sendTitle(x, 0, 100, 20, StringUtils.hex("#EAAB30&l" + secondsLeft), ""));
                        Bukkit.broadcastMessage(StringUtils.hex("#5A6E9C&l| #A1BDD7Game starts in #8eee3a%s seconds").formatted(secondsLeft));
                    }
                    case 6, 7, 8, 9, 10 ->
                            Bukkit.broadcastMessage(StringUtils.hex("#5A6E9C&l| #A1BDD7Game starts in #8eee3a%s seconds").formatted(secondsLeft));
                }

            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public void finish(String region, Player player) {
        if (activeMap == null) return;
        if (type == GameType.SOLO) {
            if (activeMap == null || activeMap.getTeamsPlaying() == null) return;
            List<Team> teamList = activeMap.getTeamsPlaying();
            for (Team team : teamList) {
                if (team.containsPlayer(player)) {
                    if (team.getFinishedMembers().contains(player.getUniqueId())) return;
                    team.getFinishedMembers().add(player.getUniqueId());

                    team.getCheckPointMap().put(player.getUniqueId(), activeMap.getFinish());

                    plugin.getAdventure().player(player).playSound(Sound.sound(Key.key("wipeout:mcw.sfx.game_finish"), Sound.Source.MASTER, 1.0f, 1.0f));

                    spawnFirework(player);

                    plugin.getObstacleBar().disable(player);

                    Titles.sendTitle(player, 0, 100, 20, "", StringUtils.hex("#F7CE50Finished!"));
                    int timer = (int) stopwatch.elapsed(TimeUnit.MILLISECONDS);

                    playerTimers.put(player.getUniqueId(), timer);

                    Bukkit.broadcastMessage(StringUtils.hex("&8[#8eee3a⭐&8] #F7CE50" + player.getName() + " #A1BDD7has finished in #F7CE50" + ordinal((finishedTeams.size() + 1)) + " place! &8(#8eee3a" + Utils.formatTime(timer) + "&8)"));

                    player.setGameMode(GameMode.SPECTATOR);

                    getPlayersExcludeTeamMembers(team.getUUIDMembers(), player).forEach(x -> {
                        player.showPlayer(Wipeout.get(), x);
                    });

                    if (team.getFinishedMembers().size() == team.getMembers().size()) {
                        team.getFinishedMembers().forEach(x -> {
                            if (Bukkit.getPlayer(x) != null) {
                                Titles.sendTitle(Bukkit.getPlayer(x), 0, 100, 20, "", StringUtils.hex("#F7CE50Finished!"));
                            }
                        });

                        if (region.contains("finish")) team.finish(player, activeMap.getFinish());
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

                    spawnFirework(player);

                    team.getCheckPointMap().put(player.getUniqueId(), activeMap.getFinish());

                    plugin.getAdventure().player(player).playSound(Sound.sound(Key.key("wipeout:mcw.sfx.game_finish"), Sound.Source.MASTER, 1.0f, 1.0f));

                    Titles.sendTitle(player, 0, 100, 20, "", StringUtils.hex("#F7CE50Finished!"));
                    int timer = (int) stopwatch.elapsed(TimeUnit.MILLISECONDS);
                    Bukkit.broadcastMessage(StringUtils.hex("&8[#8eee3a⭐&8] " + team.getColor() + player.getName() + " #A1BDD7has finished in #F7CE50" + Utils.formatTime(timer)
                            + "! &8(#8eee3a" + team.getFinishedMembers().size() + "/" + team.getMembers().size() + "&8)"));

                    playerTimers.put(player.getUniqueId(), timer);

                    player.setGameMode(GameMode.SPECTATOR);


                    finishedPlayers.add(player.getUniqueId());

                    getPlayersExcludeTeamMembers(team.getUUIDMembers(), player).forEach(x -> {
                        player.showPlayer(Wipeout.get(), x);
                    });

                    if (team.getFinishedMembers().size() == team.getMembers().size()) {
                        team.getFinishedMembers().forEach(x -> {
                            if (Bukkit.getPlayer(x) != null) {
                                Titles.sendTitle(Bukkit.getPlayer(x), 0, 100, 20, "", StringUtils.hex("#F7CE50Team Finish!"));
                            }
                        });

                        if (region.contains("finish")) team.finish(player, activeMap.getFinish());

                        teamTimers.put(team, timer);

                        Bukkit.getOnlinePlayers().forEach(player1 -> plugin.getAdventure().player(player1).playSound(Sound.sound(Key.key("wipeout:mcw.gamefinish"), Sound.Source.MASTER, 0.5f, 1.0f)));

                        Bukkit.broadcastMessage(Color.colorize("&8&m                                                            "));
                        Bukkit.broadcastMessage(StringUtils.hex("&8[#BF1542⭐&8] #A1BDD7Team " + team.getColor() + team.getId() + " #A1BDD7has finished in #8eee3a"
                                + ordinal((finishedTeams.size() + 1)) + " place! &8(#F7CE50" + Utils.formatTime(timer) + "&8)"));
                        Bukkit.broadcastMessage(Color.colorize("&8&m                                                            "));

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

    private void spawnFirework(Player player) {
        Location loc = player.getLocation();

        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.addEffect(FireworkEffect.builder()
                .with(FireworkEffect.Type.BURST)
                .flicker(true)
                .withColor(org.bukkit.Color.fromRGB(Integer.parseInt("#FECB4F".substring(1), 16)))
                .build());

        fw.setFireworkMeta(fwm);

        fw.detonate();
    }

    public void stopActiveMap() {
        if (task != null) {
            task.cancel();
        }

        task = null;
        timerTask = null;

        if (bossBar != null) {
            bossBar.removeAll();
            bossBar = null;
        }

        activeMap.setEnabled(false);


        for (Player player : Bukkit.getOnlinePlayers()) {
            Board.getInstance().resetScoreboard(player);

            player.getInventory().remove(Material.GHAST_TEAR);
            if (!player.hasPermission("wipeout.play")) continue;
            plugin.getObstacleBar().disable(player);
            plugin.getAdventure().player(player).playSound(Sound.sound(Key.key("wipeout:mcw.sfx.game_end"), Sound.Source.MASTER, 1.0f, 1.0f));
            Titles.sendTitle(player, 0, 100, 20, "", StringUtils.hex("#BF1542&lGame Over!"));
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(new Location(Bukkit.getWorld("maps"), 0.5, 0, 0.5, -180f, 0.0F));
            Bukkit.getOnlinePlayers().forEach(x -> {
                if (!x.getUniqueId().equals(player.getUniqueId())) {
                    player.showPlayer(Wipeout.get(), x);
                }
            });

            for (Team team : activeMap.getTeamsPlaying()) {
                if (team.containsPlayer(player)) {
                    getPlayersExcludeTeamMembers(team.getUUIDMembers(), player).forEach(x -> player.showPlayer(Wipeout.get(), x));
                }
            }
        }
        if (!finishedTeams.isEmpty()) {
            Bukkit.broadcastMessage(Color.colorize("&8&m                                                            "));
            Bukkit.broadcastMessage(StringUtils.hex("#F7CE50&lMap " + activeMap.getMapName() + " Leaderboard:"));

            int maxI = 3;
            if (finishedTeams.size() < maxI) {
                maxI = finishedTeams.size();
            }

            for (int i = 0; i < maxI; i++) {
                Bukkit.broadcastMessage(" ");

                StringBuilder builder = new StringBuilder();
                if (type == GameType.TEAMS) {
                    Bukkit.broadcastMessage(StringUtils.hex("#8eee3a" + ordinal((i + 1)) + ": " + finishedTeams.get(i).getColor() + finishedTeams.get(i).getId()));

                    for (int j = 0; j < finishedTeams.get(i).getMembers().size(); j++) {
                        builder.append(Bukkit.getOfflinePlayer(finishedTeams.get(i).getMembers().get(j).getUuid()).getName());
                        if (j < finishedTeams.get(i).getMembers().size() - 1) {
                            builder.append(", ");
                        }
                    }
                    Bukkit.broadcastMessage(StringUtils.hex("#F7CE50" + builder));
                } else {
                    finishedTeams.get(i).getMembers().forEach(x -> builder.append(Bukkit.getOfflinePlayer(x.getUuid()).getName()).append(" "));
                    Bukkit.broadcastMessage(StringUtils.hex("#8eee3a" + ordinal((i + 1)) + ": #F7CE50" + builder));
                }
                Bukkit.broadcastMessage(" ");
            }

            for (Player p : Bukkit.getOnlinePlayers()) {

                if (type == GameType.TEAMS) {
                    p.sendMessage(StringUtils.hex("#A1BDD7Your time: #EAAB30") + Utils.formatTime(playerTimers.get(p.getUniqueId()))); //TODO: solo time
                    p.sendMessage(StringUtils.hex("#A1BDD7Your team's time: #EAAB30" + Utils.formatTime(playerTimers.get(p.getUniqueId())))); //TODO: team time
                } else if (type == GameType.SOLO) {
                    p.sendMessage(StringUtils.hex("#A1BDD7Your time: #EAAB30" + Utils.formatTime(playerTimers.get(p.getUniqueId())))); //TODO: solo time
                }
            }
            Bukkit.broadcastMessage(Color.colorize("&8&m                                                            "));
        }

        stopwatch = null;

        finishedTeams.clear();
        finishedPlayers.clear();

        activeMap.getObstacles().forEach(Obstacle::reset);

        activeMap.getObstacles().forEach(obstacle -> obstacle.setEnabled(false));

        activeMap.getTeamsPlaying().forEach(team -> team.getCheckPointMap().clear());
        activeMap.getTeamsPlaying().clear();

        //saveTimers();

        playerTimers.clear();
        teamTimers.clear();

        if (timerTask != null) {
            timerTask.cancel();
        }

        timerTask = null;

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
                    Bukkit.broadcastMessage(StringUtils.hex("#F7CE50⌚ #8eee3a3 minutes remaining!"));
                }

                if (secondsLeft == 120) {
                    Bukkit.broadcastMessage(StringUtils.hex("#F7CE50⌚ #8eee3a2 minutes remaining!"));
                }

                if (secondsLeft == 60) {
                    Bukkit.broadcastMessage(StringUtils.hex("#F7CE50⌚ #8eee3a1 minute remaining!"));
                }

                if (secondsLeft < 10 && secondsLeft > 0) {
                    Bukkit.broadcastMessage(StringUtils.hex("#F7CE50⌚ #8eee3a" + secondsLeft + " seconds remaining!"));
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

    public boolean isPlaying(Player player) {
        if (activeMap == null) return false;
        for (Team team : activeMap.getTeamsPlaying()) {
            if (team.containsPlayer(player)) {
                return true;
            }
        }
        return false;
    }

    public CheckPoint getCurrentCheckPoint(Player player) {
        if (activeMap == null) return null;
        for (Team team : activeMap.getTeamsPlaying()) {
            if (team.containsPlayer(player)) {
                return team.getCheckPointMap().get(player.getUniqueId());
            }
        }
        return null;
    }

    public int getMaxPlayersNeeded() {
        int total = 0;
        if (getActiveMap() == null) return total;

        for (Team team : getActiveMap().getTeamsPlaying()) {
            if (Bukkit.getPlayer(team.getUUIDMembers().get(0)) != null) {
                total++;
            }
        }
        return total;
    }

    public static String ordinal(int i) {
        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        return switch (i % 100) {
            case 11, 12, 13 -> i + "th";
            default -> i + suffixes[i % 10];
        };
    }

    public Team getTeamFromUUID(UUID uuid) {
        for (Team team : activeMap.getTeamsPlaying()) {
            if (team.getUUIDMembers().contains(uuid)) {
                return team;
            }
        }
        return null;
    }
}
