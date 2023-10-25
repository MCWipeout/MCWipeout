package org.stormdev.mcwipeout.commands.sub;
/*
  Created by Stormbits at 2/12/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.stormdev.commands.CommandContext;
import org.stormdev.commands.StormSubCommand;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.io.impl.WipeoutResult;

import java.util.HashSet;
import java.util.Set;

public class ExportCommand extends StormSubCommand {

    public static boolean exportEnabled;
    private Wipeout plugin;
    private Set<String> aliases;

    public ExportCommand(Wipeout plugin) {
        super(0, "&cInvalid Args!");
        this.plugin = plugin;
        this.aliases = new HashSet<>();
        aliases.add("export");
        exportEnabled = false;
    }


    @Override
    public Set<String> aliases() {
        return aliases;
    }

    @Override
    public void execute(CommandContext<?> commandContext) {
        String[] args = commandContext.args();
        CommandSender sender = commandContext.sender();

        if (!sender.hasPermission("wipeout.admin")) {
            sender.sendMessage(ChatColor.RED + "No permissions!");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Map Active: " + (plugin.getGameManager().getActiveMap() != null));
        sender.sendMessage(ChatColor.YELLOW + "Teams loaded: " + (plugin.getGameManager().getActiveMap() != null ? plugin.getGameManager().getActiveMap().getTeamsPlaying().size() : 0));

        if (args.length == 1) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (offlinePlayer == null) {
                sender.sendMessage(ChatColor.RED + "Player has never joined the server!");
                return;
            }

            WipeoutResult.sendPlayerTimeMessage(sender, offlinePlayer.getUniqueId(), 1);
            WipeoutResult.sendPlayerTimeMessage(sender, offlinePlayer.getUniqueId(), 2);
            WipeoutResult.sendPlayerTimeMessage(sender, offlinePlayer.getUniqueId(), 3);
            WipeoutResult.sendPlayerTimeMessage(sender, offlinePlayer.getUniqueId(), 4);

            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                plugin.getWipeoutDatabase().closeConnection();
            }, 20L);
        }
    }
}
