package org.stormdev.mcwipeout.frame.board;
/*
  Created by Stormbits at 10/10/2023
*/

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.game.Map;
import org.stormdev.mcwipeout.frame.obstacles.ObstacleRegion;
import org.stormdev.utils.Pair;
import org.stormdev.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Board implements Runnable {

    @Getter
    private static Board instance;

    private Wipeout plugin;

    private java.util.Map<UUID, List<Pair<ObstacleRegion, Boolean>>> obstacles;

    public Board(Wipeout plugin) {
        this.plugin = plugin;
        instance = this;
        this.obstacles = new HashMap<>();
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getScoreboard();
            if (player.getScoreboard().getObjective(Wipeout.get().getName()) != null || player.getScoreboard().getObjective("GameSB") != null) {
                updateScoreboard(player);
            } else {
                createNewScoreboard(player);
            }
        }
    }

    public void addObstacles(Player player, List<ObstacleRegion> obstacleRegions) {
        obstacles.put(player.getUniqueId(), obstacleRegions.stream().map(obstacleRegion -> Pair.of(obstacleRegion, false)).toList());
    }

    public void finishObstacle(Player player, ObstacleRegion region) {
        Bukkit.broadcastMessage(region.name());
        List<Pair<ObstacleRegion, Boolean>> list = obstacles.get(player.getUniqueId());

        List<Pair<ObstacleRegion, Boolean>> copyList = new ArrayList<>();
        if (list == null) return;

        for (Pair<ObstacleRegion, Boolean> obstacleRegionBooleanPair : list) {
            if (obstacleRegionBooleanPair.getFirst() == region) {
                copyList.add(Pair.of(obstacleRegionBooleanPair.getFirst(), true));
            } else {
                copyList.add(obstacleRegionBooleanPair);
            }
        }

        obstacles.replace(player.getUniqueId(), copyList);
    }

    private void createNewScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.registerNewObjective(Wipeout.get().getName(), "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§6    §6§lMCWipeout    §6");

        objective.getScore(ChatColor.RED + "").setScore(5);
        objective.getScore(format("&cStarting soon!")).setScore(4);

        Team team = scoreboard.getTeam("map");
        if (team == null) team = scoreboard.registerNewTeam("map");

        String teamKey = ChatColor.GOLD.toString();

        team.addEntry(teamKey);
        team.setPrefix(format("&aNext map: "));
        team.setSuffix(format("&fMap"));

        objective.getScore(teamKey).setScore(3);

        objective.getScore(ChatColor.GREEN + "").setScore(2);
        objective.getScore(StringUtils.center("§b@ᴍᴄᴡɪᴘᴇᴏᴜᴛ", 20)).setScore(1);
        objective.getScore(StringUtils.center("§7ᴍᴄᴡɪᴘᴇᴏᴜᴛ.ᴄᴏᴍ", 20)).setScore(0);

        player.setScoreboard(scoreboard);
    }

    public void resetScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        if (scoreboard.getObjective(Wipeout.get().getName()) != null)
            scoreboard.getObjective(Wipeout.get().getName()).unregister();
        if (scoreboard.getObjective("GameSB") != null)
            scoreboard.getObjective("GameSB").unregister();

        obstacles.clear();
    }

    private void createGameScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Map activeMap = plugin.getGameManager().getActiveMap();

        scoreboard.getObjective(Wipeout.get().getName()).unregister();

        Objective objective = scoreboard.registerNewObjective("GameSB", "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§6    §6§lMCWipeout    §6");

        objective.getScore(ChatColor.RED + "").setScore(13);
        objective.getScore(format("&eMap: &f" + activeMap.getMapName())).setScore(12);
        objective.getScore(ChatColor.GREEN + "").setScore(11);

        Team obstacleTeam = scoreboard.getTeam("obstacle");
        if (obstacleTeam == null) obstacleTeam = scoreboard.registerNewTeam("obstacle");

        String obstacleKey = ChatColor.GOLD.toString();

        obstacleTeam.addEntry(obstacleKey);
        obstacleTeam.setPrefix(format("&bObstacle: "));
        if (plugin.getGameManager().getCurrentCheckPoint(player) == null) {
            obstacleTeam.setSuffix(format("&fNone"));
        } else {
            obstacleTeam.setSuffix(format("&f" + plugin.getObstacleBar().getObstacleIndex().get(player.getUniqueId()) + "/" + plugin.getGameManager().getActiveMap().getObstacles().size()));
        }

        objective.getScore(obstacleKey).setScore(10);

        Team timeTeam = scoreboard.getTeam("timeElapsed");
        if (timeTeam == null) timeTeam = scoreboard.registerNewTeam("timeElapsed");

        String timeKey = ChatColor.YELLOW.toString();

        timeTeam.addEntry(timeKey);
        timeTeam.setPrefix(format("&bTime Elapsed: "));
        timeTeam.setSuffix(format("&f0s"));

        objective.getScore(timeKey).setScore(9);

        objective.getScore(ChatColor.DARK_PURPLE + "").setScore(8);

        int size = obstacles.get(player.getUniqueId()).size();

        int firstLine = size / 2;

        Team obstacleLine1 = scoreboard.getTeam("obstacleLine1");
        if (obstacleLine1 == null) obstacleLine1 = scoreboard.registerNewTeam("obstacleLine1");

        String obstacleKey1 = ChatColor.GRAY.toString();

        obstacleLine1.addEntry(obstacleKey1);
        StringBuilder line1 = new StringBuilder();
        for (int i = 0; i < firstLine; i++) {
            line1.append(obstacles.get(player.getUniqueId()).get(i).getFirst().getGrayIcon()).append("  ");
        }

        obstacleLine1.setSuffix(format("&f" + line1));

        objective.getScore(obstacleKey1).setScore(7);

        Team obstacleLine2 = scoreboard.getTeam("obstacleLine2");

        if (obstacleLine2 == null) obstacleLine2 = scoreboard.registerNewTeam("obstacleLine2");

        String obstacleKey2 = ChatColor.AQUA.toString();

        obstacleLine2.addEntry(obstacleKey2);

        StringBuilder line2 = new StringBuilder();
        for (int i = firstLine; i < size; i++) {
            line2.append(obstacles.get(player.getUniqueId()).get(i).getFirst().getGrayIcon()).append("  ");
        }

        obstacleLine2.setSuffix(format("&f" + line2));

        objective.getScore(obstacleKey2).setScore(6);

        objective.getScore(ChatColor.LIGHT_PURPLE + "").setScore(5);

        String teamFinishedKey = ChatColor.DARK_AQUA.toString();
        Team teamFinishedTeam = scoreboard.getTeam("teamFinished");
        if (teamFinishedTeam == null) teamFinishedTeam = scoreboard.registerNewTeam("teamFinished");

        teamFinishedTeam.addEntry(teamFinishedKey);

        teamFinishedTeam.setPrefix(format("&aTeam Finished: "));
        teamFinishedTeam.setSuffix(format("&f0/0"));

        objective.getScore(teamFinishedKey).setScore(4);

        String finishedKey = ChatColor.BLACK.toString();
        Team finishedTeam = scoreboard.getTeam("finished");
        if (finishedTeam == null) finishedTeam = scoreboard.registerNewTeam("finished");

        finishedTeam.addEntry(finishedKey);

        finishedTeam.setPrefix(format("&aFinished: "));
        finishedTeam.setSuffix(format("&f0/0"));

        objective.getScore(finishedKey).setScore(3);

        objective.getScore(ChatColor.WHITE + "").setScore(2);

        objective.getScore(StringUtils.center("§7ᴍᴄᴡɪᴘᴇᴏᴜᴛ.ᴄᴏᴍ", 20)).setScore(1);
    }

    private void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        Map activeMap = plugin.getGameManager().getActiveMap();

        if (activeMap != null && activeMap.isEnabled() && plugin.getGameManager().isPlaying(player)) {

            if (scoreboard.getObjective("GameSB") != null) {

                Team obstacleTeam = scoreboard.getTeam("obstacle");
                obstacleTeam.setSuffix(format("&f" + plugin.getObstacleBar().getObstacleIndex().get(player.getUniqueId()) + "/" + plugin.getGameManager().getActiveMap().getObstacles().size()));

                Team timeTeam = scoreboard.getTeam("timeElapsed");
                if (plugin.getGameManager().getStopwatch() == null) {
                    timeTeam.setSuffix(format("&f0s"));
                } else {
                    int timer = (int) plugin.getGameManager().getStopwatch().elapsed(TimeUnit.MILLISECONDS);
                    String formatted = Utils.formatTime(timer);
                    if (formatted.isBlank()) {
                        timeTeam.setSuffix(format("&f0s"));
                    } else {
                        timeTeam.setSuffix(format("&f" + formatted));
                    }
                }

                int size = obstacles.get(player.getUniqueId()).size();

                int firstLine = size / 2;

                Team obstacleLine1 = scoreboard.getTeam("obstacleLine1");
                StringBuilder line1 = new StringBuilder();
                for (int i = 0; i < firstLine; i++) {
                    Pair<ObstacleRegion, Boolean> pair = obstacles.get(player.getUniqueId()).get(i);
                    if (pair.getSecond()) line1.append(pair.getFirst().getColorIcon()).append("  ");
                    else line1.append(pair.getFirst().getGrayIcon()).append("  ");
                }

                obstacleLine1.setSuffix(format("&f" + line1));

                Team obstacleLine2 = scoreboard.getTeam("obstacleLine2");

                StringBuilder line2 = new StringBuilder();
                for (int i = firstLine; i < size; i++) {
                    Pair<ObstacleRegion, Boolean> pair = obstacles.get(player.getUniqueId()).get(i);
                    if (pair.getSecond()) line2.append(pair.getFirst().getColorIcon()).append("  ");
                    else line2.append(pair.getFirst().getGrayIcon()).append("  ");
                }
                obstacleLine2.setSuffix(format("&f" + line2));

                Team finishedTeam = scoreboard.getTeam("teamFinished");
                Team finishedTotal = scoreboard.getTeam("finished");

                org.stormdev.mcwipeout.frame.team.Team playerTeam = plugin.getGameManager().getTeamFromUUID(player.getUniqueId());
                if (playerTeam != null) {
                    finishedTeam.setSuffix(format("&f" + playerTeam.getFinishedMembers().size() + "/" + playerTeam.getMembers().size()));
                }

                List<UUID> uuids = new ArrayList<>();
                for (org.stormdev.mcwipeout.frame.team.Team team : plugin.getGameManager().getActiveMap().getTeamsPlaying()) {
                    uuids.addAll(team.getUUIDMembers());
                }

                finishedTotal.setSuffix(format("&f" + plugin.getGameManager().getFinishedPlayers().size() + "/" + uuids.size()));

            } else {
                createGameScoreboard(player);
            }


        } else {

            Team team = scoreboard.getTeam("map");

            if (plugin.getGameManager().getActiveMap() == null) {
                team.setSuffix(format("&fNone"));
                return;
            } else {
                team.setSuffix(format("&f" + plugin.getGameManager().getActiveMap().getMapName()));
            }
        }
    }

    private String format(String s) {
        return s.replace("&", "§");
    }
}
