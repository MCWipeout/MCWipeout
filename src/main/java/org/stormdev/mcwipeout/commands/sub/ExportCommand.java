package org.stormdev.mcwipeout.commands.sub;
/*
  Created by Stormbits at 2/12/2023
*/

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.stormdev.commands.CommandContext;
import org.stormdev.commands.StormSubCommand;
import org.stormdev.mcwipeout.Wipeout;

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
    }
}
