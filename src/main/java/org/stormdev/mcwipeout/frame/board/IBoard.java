package org.stormdev.mcwipeout.frame.board;
/*
  Created by Stormbits at 10/24/2023
*/

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.stormdev.utils.Color;

import java.util.*;

public class IBoard {
    private Scoreboard scoreboard;
    private String objective;
    private String title;
    private Map<String, BoardLine> boardLineMap;

    public IBoard(Scoreboard scoreboard, String title, int size) {
        this.boardLineMap = new HashMap<>();
        this.title = Color.colorize(title);

        this.scoreboard = scoreboard;
        this.objective = size + "_customBoard";

        Objective obj = this.scoreboard.registerNewObjective(objective, "dummy", this.title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void addLine(String key, String line) {
        line = Color.colorize(line);
        BoardLine boardLine;

        if (line.length() <= 16) {
            boardLine = new BoardLine(line, "");

        } else {
            String prefix = line.substring(0, 16);
            String suffix;
            if (line.length() >= 32) {
                suffix = line.substring(16, 32);
            } else {
                suffix = line.substring(16);
            }

            String lastColor = ChatColor.getLastColors(prefix);
            suffix = lastColor + suffix;

            boardLine = new BoardLine(prefix, suffix);
        }

        set(boardLine);

        boardLineMap.put(key, boardLine);
    }

    private void set(BoardLine boardLine) {
        int i = boardLineMap.size();
        Team team = getOrCreateTeam(i + "IBOARD", i);

        team.setPrefix(boardLine.getPrefix());
        team.setSuffix(boardLine.getSuffix());

        boardLine.setTeam(team);

        getObjective().getScore(getNameForIndex(i)).setScore(16 - i);
    }

    public void addLine(String line) {
        line = Color.colorize(line);
        BoardLine boardLine;

        if (line.length() <= 16) {
            boardLine = new BoardLine(line, "");

        } else {
            String prefix = line.substring(0, 16);
            String suffix = "";
            if (line.length() >= 32) {
                suffix = line.substring(16, 32);
            } else {
                suffix = line.substring(16);
            }

            String lastColor = ChatColor.getLastColors(prefix);
            suffix = lastColor + suffix;

            boardLine = new BoardLine(prefix, suffix);
        }

        set(boardLine);

        boardLineMap.put(UUID.randomUUID().toString(), boardLine);
    }

    public void updateLine(String key, String text) {
        text = Color.colorize(text);

        BoardLine boardLine = boardLineMap.get(key); // Get the board line
        if (boardLine == null) return;

        if (text.length() <= 16) {
            boardLine.setPrefix(text);
            boardLine.setSuffix("");
        } else {
            String prefix = text.substring(0, 16);
            String suffix = "";
            if (text.length() >= 32) {
                suffix = text.substring(16, 32);
            } else {
                suffix = text.substring(16);
            }

            String lastColor = ChatColor.getLastColors(prefix);
            suffix = lastColor + suffix;

            boardLine.setPrefix(prefix);
            boardLine.setSuffix(suffix);
        }

        boardLine.getTeam().setPrefix(boardLine.getPrefix());
        boardLine.getTeam().setSuffix(boardLine.getSuffix());

        boardLineMap.replace(key, boardLine);
    }

    public void clear() {
        boardLineMap.clear();

        for (Team team : scoreboard.getTeams()) {
            if (team.getName().endsWith("IBOARD")) {
                scoreboard.resetScores(team.getEntries().stream().findFirst().get());
                team.unregister();
            }
        }

        getObjective().unregister();
    }

    public Objective getObjective() {
        Objective obj = this.scoreboard.getObjective(this.objective);
        if (obj == null) {
            obj = this.scoreboard.registerNewObjective(this.objective, "dummy", this.title);
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        return obj;
    }

    public void setTitle(String title) {
        getObjective().setDisplayName(title);
        getObjective().setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void update(Player player) {
        player.setScoreboard(scoreboard);
    }

    public Team getOrCreateTeam(String team, int i) {
        Team value = this.scoreboard.getTeam(team);
        if (value == null) {
            value = this.scoreboard.registerNewTeam(team);
            String entry = getNameForIndex(i);
            value.addEntry(entry);
        }
        return value;
    }

    public String getNameForIndex(int index) {
        return ChatColor.values()[index].toString() + ChatColor.RESET;
    }

}
