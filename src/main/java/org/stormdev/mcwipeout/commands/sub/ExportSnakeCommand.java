package org.stormdev.mcwipeout.commands.sub;
/*
  Created by Stormbits at 2/12/2023
*/

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.stormdev.commands.CommandContext;
import org.stormdev.commands.StormSubCommand;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.utils.helpers.WLocation;
import org.stormdev.utils.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExportSnakeCommand extends StormSubCommand {
    private Wipeout plugin;
    private Set<String> aliases;

    public static List<WLocation> wLocations;

    public static boolean enabled;

    public ExportSnakeCommand(Wipeout plugin) {
        super(0, "&cInvalid Args!");
        this.plugin = plugin;
        this.aliases = new HashSet<>();
        aliases.add("exportsnake");
        wLocations = new ArrayList<>();
        enabled = false;
    }


    @Override
    public Set<String> aliases() {
        return aliases;
    }

    @SneakyThrows
    @Override
    public void execute(CommandContext<?> commandContext) {
        String[] args = commandContext.args();
        CommandSender sender = commandContext.sender();

        if (!sender.hasPermission("wipeout.admin")) {
            sender.sendMessage(ChatColor.RED + "No permissions!");
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/wipeout exportsnake (enable or disable)");
            sender.sendMessage(ChatColor.RED + "/wipeout exportsnake (fileId)");
            return;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("enable")) {
                enabled = true;

                sender.sendMessage(ChatColor.YELLOW + "Enabled export!");
                return;
            }
            if (args[0].equalsIgnoreCase("disable")) {
                enabled = false;

                sender.sendMessage(ChatColor.YELLOW + "Disabled export!");
                return;
            }

            String fileId = args[0];

            save(fileId);

            Bukkit.broadcast(Color.colorize("&eExported: " + wLocations.size() + " Snake Locations to config"), "wipeout.*");

            wLocations.clear();
            return;
        }
    }

    public void save(String path) {
        List<String> strings = new ArrayList<>();

        for (WLocation wLocation : wLocations) {
            strings.add(wLocation.getBlockX() + "," + wLocation.getBlockY() + "," + wLocation.getBlockZ());
        }

        plugin.getConfig().set("Snakes." + path, strings);
        plugin.saveConfig();
    }
}
