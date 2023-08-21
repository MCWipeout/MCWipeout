package org.stormdev.mcwipeout.commands;
/*
  Created by Stormbits at 4/21/2023
*/

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.stormdev.commands.CommandContext;
import org.stormdev.commands.StormCommand;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.team.WipeoutPlayer;


import java.util.Arrays;

public class StuckCommand extends StormCommand<Player> {

    private Wipeout plugin;

    public StuckCommand(Wipeout plugin) {
        super("stuck", Player.class);
        this.plugin = plugin;

        invalidArgs("&c/stuck");

        setAliases(Arrays.asList("stucj", "unstuck"));
    }

    @Override
    public void execute(CommandContext<Player> commandContext) {
        Player player = commandContext.sender();
        WipeoutPlayer wipeoutPlayer = plugin.getTeamManager().fromUUID(player.getUniqueId());

        if (plugin.getGameManager().getActiveMap() != null) {
            plugin.getGameManager().getActiveMap().handleCheckPoint(player);
            player.sendMessage(ChatColor.GREEN + "Teleported you to your latest checkpoint!");
        }
    }
}
