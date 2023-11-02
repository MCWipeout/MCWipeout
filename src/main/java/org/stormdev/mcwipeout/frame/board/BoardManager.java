package org.stormdev.mcwipeout.frame.board;
/*
  Created by Stormbits at 10/10/2023
*/

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.WipeoutAPI;
import org.stormdev.mcwipeout.frame.obstacles.ObstacleRegion;
import org.stormdev.mcwipeout.frame.team.Team;
import org.stormdev.mcwipeout.frame.team.WipeoutPlayer;
import org.stormdev.utils.Pair;
import org.stormdev.utils.Utils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
public class BoardManager implements Runnable {

    @Getter
    private static BoardManager instance;

    private final Wipeout plugin;

    private java.util.Map<UUID, List<Pair<ObstacleRegion, Boolean>>> obstacles;

    private static Map<UUID, IBoard> BOARD_MAP;

    public BoardManager(Wipeout plugin) {
        this.plugin = plugin;
        instance = this;
        this.obstacles = new HashMap<>();
        BOARD_MAP = new HashMap<>();

        Bukkit.getScoreboardManager().getMainScoreboard().getObjectives().forEach(objective -> {
            if (objective.getName().endsWith("_customBoard")) objective.unregister();
        });
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateScoreboard(player);
        }
    }

    public void addObstacles(Player player, List<ObstacleRegion> obstacleRegions) {
        obstacles.put(player.getUniqueId(), obstacleRegions.stream().map(obstacleRegion -> Pair.of(obstacleRegion, false)).toList());
    }

    private void updateScoreboard(Player player) {
        WipeoutPlayer wipeoutPlayer = Wipeout.get().getTeamManager().fromUUID(player.getUniqueId());

        if (WipeoutAPI.getInstance().isMapRunning()) {

            //CHECK IF PLAYER HAS A SCOREBOARD
            if (!BOARD_MAP.containsKey(player.getUniqueId())) {
                createRunningScoreboard(player, wipeoutPlayer);
            } else {
                updateRunningScoreboard(player, wipeoutPlayer);
            }
        } else {

            if (!BOARD_MAP.containsKey(player.getUniqueId())) {
                createLobbyScoreboard(player, wipeoutPlayer);
            } else {
                updateLobbyScoreboard(player, wipeoutPlayer);
            }
        }
    }

    private void updateLobbyScoreboard(Player player, WipeoutPlayer wipeoutPlayer) {
        IBoard iBoard = BOARD_MAP.get(player.getUniqueId());

        if (plugin.getGameManager().getActiveMap() == null) {
            iBoard.updateLine("map", "&aNext Map: &fNone");
        } else {
            iBoard.updateLine("map", "&aNext Map: &f" + plugin.getGameManager().getActiveMap().getMapName());
        }
    }

    private void createLobbyScoreboard(Player player, WipeoutPlayer wipeoutPlayer) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        player.setScoreboard(scoreboard);

        wipeoutPlayer.setBoard(new IBoard(scoreboard, "§6    §6§lMCWipeout    §6", BOARD_MAP.size()));

        checkForTeam(player);

        BOARD_MAP.put(player.getUniqueId(), wipeoutPlayer.getBoard());

        IBoard iBoard = wipeoutPlayer.getBoard();

        iBoard.addLine(" ");
        iBoard.addLine("&cStarting soon!");

        if (plugin.getGameManager().getActiveMap() == null) {
            iBoard.addLine("map", "&aNext Map: &fNone");
        } else {
            iBoard.addLine("map", "&aNext Map: &f" + plugin.getGameManager().getActiveMap().getMapName());
        }

        iBoard.addLine(" ");
        iBoard.addLine(StringUtils.center("§b@ᴍᴄ_ᴡɪᴘᴇᴏᴜᴛ", 20));
        iBoard.addLine(StringUtils.center("§7ᴍᴄᴡɪᴘᴇᴏᴜᴛ.ᴄᴏᴍ", 20));

        iBoard.update(player);
    }

    public void createRunningScoreboard(Player player, WipeoutPlayer wipeoutPlayer) {
        if (!player.hasPermission("wipeout.play")) return;

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        player.setScoreboard(scoreboard);

        wipeoutPlayer.setBoard(new IBoard(scoreboard, "§6    §6§lMCWipeout    §6", BOARD_MAP.size()));

        BOARD_MAP.put(player.getUniqueId(), wipeoutPlayer.getBoard());

        checkForTeam(player);

        IBoard iBoard = wipeoutPlayer.getBoard();

        iBoard.addLine(" ");
        iBoard.addLine("&eMap: &f" + Wipeout.get().getGameManager().getActiveMap().getMapName());

        if (plugin.getGameManager().getCurrentCheckPoint(player) == null) {
            iBoard.addLine("obstacleIndex", "&bObstacle: &fNone");
        } else {
            iBoard.addLine("obstacleIndex", "&bObstacle: &f" + plugin.getObstacleBar().getObstacleIndex().get(player.getUniqueId()) + "/" + plugin.getGameManager().getActiveMap().getObstacles().size());
        }

        iBoard.addLine("timeElapsed", "&bTime Elapsed: &f0s");

        iBoard.addLine(" ");

        if (obstacles.get(player.getUniqueId()) == null) {
            iBoard.addLine("obstacleLine1", "&7&oLoading...");
            iBoard.addLine("obstacleLine2", "&7&oLoading...");
        } else {

            int size = obstacles.get(player.getUniqueId()).size();

            int firstLine = size / 2;


            StringBuilder line1 = new StringBuilder();
            for (int i = 0; i < firstLine; i++) {
                line1.append(obstacles.get(player.getUniqueId()).get(i).getFirst().getGrayIcon()).append("  ");
            }

            iBoard.addLine("obstacleLine1", line1.toString());

            StringBuilder line2 = new StringBuilder();
            for (int i = firstLine; i < size; i++) {
                line2.append(obstacles.get(player.getUniqueId()).get(i).getFirst().getGrayIcon()).append("  ");
            }

            iBoard.addLine("obstacleLine2", line2.toString());
        }

        iBoard.addLine(" ");

        iBoard.addLine("teamFinished", "&aTeam Finished: &f0/0");
        iBoard.addLine("finished", "&aFinished: &f0/0");

        iBoard.addLine(" ");
        iBoard.addLine(StringUtils.center("§7ᴍᴄᴡɪᴘᴇᴏᴜᴛ.ᴄᴏᴍ", 20));

        iBoard.update(player);
    }

    private void checkForTeam(Player player) {
        org.bukkit.scoreboard.Team team = player.getScoreboard().getTeam("players");
        if (team == null) {
            team = player.getScoreboard().registerNewTeam("players");
            team.setOption(org.bukkit.scoreboard.Team.Option.COLLISION_RULE, org.bukkit.scoreboard.Team.OptionStatus.FOR_OWN_TEAM);
            team.setOption(org.bukkit.scoreboard.Team.Option.NAME_TAG_VISIBILITY, org.bukkit.scoreboard.Team.OptionStatus.NEVER);
        }

        team.addPlayer(player);

        org.bukkit.scoreboard.Team finalTeam = team;
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            onlinePlayer.getScoreboard().getTeam("players").addPlayer(player);
            finalTeam.addPlayer(onlinePlayer);
        });
    }

    private void updateRunningScoreboard(Player player, WipeoutPlayer wipeoutPlayer) {
        if (!player.hasPermission("wipeout.play")) return;

        IBoard iBoard = BOARD_MAP.get(player.getUniqueId());

        if (plugin.getGameManager().getCurrentCheckPoint(player) == null) {
            iBoard.updateLine("obstacleIndex", "&bObstacle: &fNone");
        } else {
            if (plugin.getObstacleBar().getObstacleIndex().get(player.getUniqueId()) == null) {
                iBoard.updateLine("obstacleIndex", "&bObstacle: &fNone");
            } else {
                iBoard.updateLine("obstacleIndex", "&bObstacle: &f" + plugin.getObstacleBar().getObstacleIndex().get(player.getUniqueId()) + "/" + plugin.getGameManager().getActiveMap().getObstacles().size());
            }
        }

        if (plugin.getGameManager().getStopwatch() == null) {
            iBoard.updateLine("timeElapsed", "&bTime Elapsed: &f0s");
        } else {
            int timer = (int) plugin.getGameManager().getStopwatch().elapsed(TimeUnit.MILLISECONDS);
            String formatted = Utils.formatTime(timer);
            if (formatted.isBlank()) {
                iBoard.updateLine("timeElapsed", "&bTime Elapsed: &f0s");
            } else {
                iBoard.updateLine("timeElapsed", "&bTime Elapsed: &f" + formatted);
            }
        }

        if (obstacles.get(player.getUniqueId()) == null) return;

        int size = obstacles.get(player.getUniqueId()).size();

        int firstLine = size / 2;

        StringBuilder line1 = new StringBuilder();
        for (int i = 0; i < firstLine; i++) {
            Pair<ObstacleRegion, Boolean> pair = obstacles.get(player.getUniqueId()).get(i);
            if (pair.getSecond()) line1.append(pair.getFirst().getColorIcon()).append("  ");
            else line1.append(pair.getFirst().getGrayIcon()).append("  ");
        }

        iBoard.updateLine("obstacleLine1", line1.toString());

        StringBuilder line2 = new StringBuilder();
        for (int i = firstLine; i < size; i++) {
            Pair<ObstacleRegion, Boolean> pair = obstacles.get(player.getUniqueId()).get(i);
            if (pair.getSecond()) line2.append(pair.getFirst().getColorIcon()).append("  ");
            else line2.append(pair.getFirst().getGrayIcon()).append("  ");
        }

        iBoard.updateLine("obstacleLine2", line2.toString());

        org.stormdev.mcwipeout.frame.team.Team playerTeam = plugin.getGameManager().getTeamFromUUID(player.getUniqueId());
        if (playerTeam != null) {
            iBoard.updateLine("teamFinished", "&aTeam Finished: &f" + playerTeam.getFinishedMembers().size() + "/" + playerTeam.getMembers().size());
        }

        List<UUID> uuids = new ArrayList<>();
        for (org.stormdev.mcwipeout.frame.team.Team team : plugin.getGameManager().getActiveMap().getTeamsPlaying()) {
            uuids.addAll(team.getUUIDMembers());
        }

        iBoard.updateLine("finished", "&aFinished: &f" + plugin.getGameManager().getFinishedPlayers().size() + "/" + uuids.size());

        iBoard.update(player);
    }

    public void resetScoreboard(Player player) {
        obstacles.remove(player.getUniqueId());

        IBoard board = BOARD_MAP.get(player.getUniqueId());

        if (board != null)
            board.clear();

        BOARD_MAP.remove(player.getUniqueId());

        WipeoutPlayer wipeoutPlayer = Wipeout.get().getTeamManager().fromUUID(player.getUniqueId());
        if (wipeoutPlayer != null)
            wipeoutPlayer.setBoard(null);
    }

    public void finishObstacle(Player player, ObstacleRegion region) {
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

    private String format(String s) {
        return s.replace("&", "§");
    }
}
