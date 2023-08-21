package org.stormdev.mcwipeout.utils;
/*
  Created by Stormbits at 3/1/2023
*/

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.team.Team;
import org.stormdev.utils.StringUtils;

import java.util.Optional;

public class WipeoutPlaceholderExpansion extends PlaceholderExpansion {

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
