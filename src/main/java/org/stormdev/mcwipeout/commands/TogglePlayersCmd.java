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
import org.stormdev.mcwipeout.frame.team.WipeoutPlayer;


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
        if (wipeoutPlayer.isVisiblePlayers()) {
            wipeoutPlayer.setVisiblePlayers(false);

            player.sendMessage(ChatColor.RED + "Disabled player visibility!");

            if (plugin.getGameManager().getActiveMap() != null) {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    player.hidePlayer(plugin, pl);
                }
            }
        } else {
            wipeoutPlayer.setVisiblePlayers(true);

            player.sendMessage(ChatColor.GREEN + "Enabled player visibility!");

            if (plugin.getGameManager().getActiveMap() != null) {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    player.showPlayer(plugin, pl);
                }
            }
        }
    }
}
