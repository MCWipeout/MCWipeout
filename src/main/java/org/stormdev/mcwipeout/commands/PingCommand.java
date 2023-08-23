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
import org.stormdev.mcwipeout.dev.floatytest.ShulkerTest;
import org.stormdev.mcwipeout.dev.floatytest.ShulkerTestSide;
import org.stormdev.mcwipeout.dev.showtest.ShowTest;
import org.stormdev.mcwipeout.frame.team.WipeoutPlayer;


import java.util.Arrays;

public class PingCommand extends StormCommand<Player> {

    private Wipeout plugin;

    public PingCommand(Wipeout plugin) {
        super("ping", Player.class);
        this.plugin = plugin;

        invalidArgs("&c/ping");

        setAliases(Arrays.asList("ping", "moreping"));
    }

    @Override
    public void execute(CommandContext<Player> commandContext) {
        Player player = commandContext.sender();
        WipeoutPlayer wipeoutPlayer = plugin.getTeamManager().fromUUID(player.getUniqueId());

        if (commandContext.args().length == 1) {
            if (commandContext.args()[0].equalsIgnoreCase("water")) {
                new ShowTest();
                player.sendMessage(ChatColor.GREEN + "Show Started!");
                return;
            } else if (commandContext.args()[0].equalsIgnoreCase("shulkerup")) {
                new ShulkerTest().setup();
                player.sendMessage(ChatColor.GREEN + "Shulker Started!");
                return;
            } else if (commandContext.args()[0].equalsIgnoreCase("shulkerside")) {
                new ShulkerTestSide().setup();
                player.sendMessage(ChatColor.GREEN + "Shulker Started!");
                return;
            }
        }

        player.sendMessage(ChatColor.GREEN + "/ping");
    }
}
