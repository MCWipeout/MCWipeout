package org.stormdev.mcwipeout.commands;
/*
  Created by Stormbits at 4/21/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.commands.CommandContext;
import org.stormdev.commands.StormCommand;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.game.GameType;
import org.stormdev.mcwipeout.frame.team.Team;
import org.stormdev.mcwipeout.frame.team.WipeoutPlayer;
import org.stormdev.mcwipeout.utils.helpers.CachedItems;


import java.util.Arrays;

public class TogglePlayersCmd extends StormCommand<Player> {

    private Wipeout plugin;

    public TogglePlayersCmd(Wipeout plugin) {
        super("toggleplayers", Player.class);
        this.plugin = plugin;

        invalidArgs("&c/toggleplayers");

        setAliases(Arrays.asList("toggleplayers", "players"));
    }

    @Override
    public void execute(CommandContext<Player> commandContext) {
        Player player = commandContext.sender();
        WipeoutPlayer wipeoutPlayer = plugin.getTeamManager().fromUUID(player.getUniqueId());

        if (plugin.getGameManager().getActiveMap() == null) {
            player.sendMessage(ChatColor.RED + "A game is not currently running!");
            return;
        }
        if (wipeoutPlayer.isVisiblePlayers()) {
            wipeoutPlayer.setVisiblePlayers(false);

            player.sendMessage(ChatColor.RED + "Disabled player visibility!");

            player.getInventory().setItem(8, CachedItems.playersOffItem);

            if (plugin.getGameManager().getType() == GameType.SOLO) {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.getUniqueId().equals(player.getUniqueId())) continue;
                    player.hidePlayer(plugin, pl);
                }
                return;
            } else {
                Team team = plugin.getTeamManager().getTeamFromUUID(player.getUniqueId());
                if (team == null) return;

                plugin.getGameManager().getPlayersExcludeTeamMembers(team.getUUIDMembers(), player).forEach(pl -> player.hidePlayer(plugin, pl));
            }


        } else {
            wipeoutPlayer.setVisiblePlayers(true);

            player.sendMessage(ChatColor.GREEN + "Enabled player visibility!");

            player.getInventory().setItem(8, CachedItems.playersOnItem);

            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.getUniqueId().equals(player.getUniqueId())) continue;
                player.showPlayer(plugin, pl);
            }

        }
    }
}
