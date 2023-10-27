package org.stormdev.mcwipeout.utils;
/*
  Created by Stormbits at 3/1/2023
*/

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.team.Team;
import org.stormdev.utils.Pair;
import org.stormdev.utils.StringUtils;
import org.stormdev.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WipeoutPlaceholderExpansion extends PlaceholderExpansion {

    public static String yellow = "&#F7CE50";
    public static String orange = "&#EAAB30";

    @Override
    public String getAuthor() {
        return "stormbits";
    }

    @Override
    public String getIdentifier() {
        return "wipeout";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.startsWith("lb_")) {
            String noPrefix = params.substring("top_scores_".length());

            int firstUnderscoreIndex = noPrefix.indexOf("_");
            if (firstUnderscoreIndex == -1) {
                return "&cNo data";
            }

            String numStr = noPrefix.substring(0, firstUnderscoreIndex);
            String mapStr = noPrefix.substring(firstUnderscoreIndex + 1);

            int num;
            int map;
            try {
                num = Integer.parseInt(numStr);
                map = Integer.parseInt(mapStr);
            } catch (NumberFormatException e) {
                return "&cNo data";
            }

            List<Pair<UUID, Long>> sortedEntries = new ArrayList<>(Wipeout.get().getLeaderboardManager().getAllSortedScores("map_"));
            if (num > 0 && num <= sortedEntries.size()) {
                UUID uuid = sortedEntries.get(num - 1).getFirst();
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                long time = sortedEntries.get(num - 1).getSecond();

                String time1;
                if (time == 0) {
                    time1 = "0";
                } else {
                    time1 = Utils.formatTime(time);
                }

                return yellow + "&l" + num + ". &f" + offlinePlayer.getName() + " &8- " + orange + time1;
            }
            return yellow + "&l" + num + ". &cNo data";
        }
        if (params.equalsIgnoreCase("hex")) {
            Optional<Team> team = Wipeout.get().getTeamManager().getTeamList().stream().filter(team1 -> team1.containsPlayer(player)).findFirst();

            if (team.isPresent()) {
                return StringUtils.hex(team.get().getColor());
            }
        }
        if (params.equalsIgnoreCase("team")) {
            Optional<Team> team = Wipeout.get().getTeamManager().getTeamList().stream().filter(team1 -> team1.containsPlayer(player)).findFirst();

            if (team.isPresent()) {
                return team.get().getId();
            }
        }

        return "";
    }
}
