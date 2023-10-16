package org.stormdev.mcwipeout.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.stormdev.commands.CommandContext;
import org.stormdev.commands.StormCommand;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.commands.sub.*;
import org.stormdev.mcwipeout.frame.game.GameType;
import org.stormdev.mcwipeout.frame.team.Team;
import org.stormdev.utils.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WipeoutCommand extends StormCommand<CommandSender> {

    public WipeoutCommand(Wipeout plugin) {
        super("wipeout", CommandSender.class);

        invalidArgs("&c/wipeout");

        setAliases(Arrays.asList("wipeoutplugin", "wipeoutbase"));

        register(new MapCommand(plugin));
        register(new TeamCommand(plugin));
        register(new ExportCommand(plugin));
        register(new ExportJsonPlatformsCommand(plugin));
        register(new ExportJsonGenericCommand(plugin));
        register(new ExportSnakeCommand(plugin));
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        CommandSender sender = context.sender();

        if (sender.hasPermission("wipeout.admin")) {
            sender.sendMessage(Color.colorize("&8&m-----------------------------------"));
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.GREEN + "/wipeout export (DATA ABOUT GAME)");
            sender.sendMessage(ChatColor.GREEN + "/wipeout exportasjsonplatform");
            sender.sendMessage(ChatColor.GREEN + "/wipeout exportasjsongeneric");
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.GREEN + "/wipeout map start");
            sender.sendMessage(ChatColor.GREEN + "/wipeout map addteam (team id)");
            sender.sendMessage(ChatColor.GREEN + "/wipeout map load (map1/map2/map3/map4/map5/map6/sumo)");
            sender.sendMessage(ChatColor.GREEN + "/wipeout map autoteam");
            sender.sendMessage(ChatColor.GREEN + "/wipeout map type");
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.GREEN + "/wipeout team create (id)");
            sender.sendMessage(ChatColor.GREEN + "/wipeout team delete (id)");
            sender.sendMessage(ChatColor.GREEN + "/wipeout team join (player) (id)");
            sender.sendMessage(ChatColor.GREEN + "/wipeout team unjoin (player) (id)");
            sender.sendMessage(ChatColor.GREEN + "/wipeout team setcolor (id) (hex color code)");
            sender.sendMessage(ChatColor.GREEN + "/wipeout team list");
            sender.sendMessage(" ");
            sender.sendMessage(Color.colorize("&8&m-----------------------------------"));
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("map", "team", "export", "exportasjsongeneric", "exportasjsonplatform"), new ArrayList<>());
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("team")) {
                return StringUtil.copyPartialMatches(args[1], Arrays.asList("create", "join", "unjoin", "setcolor", "list", "delete", "rename"), new ArrayList<>());
            }
            if (args[0].equalsIgnoreCase("map")) {
                return StringUtil.copyPartialMatches(args[1], Arrays.asList("load", "start", "add", "forcestop", "autoteam", "unload", "type", "end"), new ArrayList<>());
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("team")) {
                if (args[1].equalsIgnoreCase("delete")) {
                    List<Team> teams = Wipeout.get().getTeamManager().getTeamList().stream().filter(team -> team.getId().toLowerCase().startsWith(args[2].toLowerCase())).collect(Collectors.toList());

                    List<String> toReturn = new ArrayList<>();
                    teams.forEach(x -> toReturn.add(x.getId()));

                    return toReturn;
                }
                if (args[1].equalsIgnoreCase("setcolor")) {
                    List<Team> teams = Wipeout.get().getTeamManager().getTeamList().stream().filter(team -> team.getId().toLowerCase().startsWith(args[2].toLowerCase())).collect(Collectors.toList());

                    List<String> toReturn = new ArrayList<>();
                    teams.forEach(x -> toReturn.add(x.getId()));

                    return toReturn;
                }
                if (args[1].equalsIgnoreCase("rename")) {
                    List<Team> teams = Wipeout.get().getTeamManager().getTeamList().stream().filter(team -> team.getId().toLowerCase().startsWith(args[2].toLowerCase())).collect(Collectors.toList());

                    List<String> toReturn = new ArrayList<>();
                    teams.forEach(x -> toReturn.add(x.getId()));

                    return toReturn;
                }
                if (args[1].equalsIgnoreCase("join")) {
                    List<Player> playerList = Bukkit.getOnlinePlayers()
                            .stream()
                            .filter(p -> p.getName().toLowerCase().startsWith(args[2].toLowerCase()))
                            .collect(Collectors.toList());

                    List<String> toReturn = new ArrayList<>();
                    playerList.forEach(x -> toReturn.add(x.getName()));
                    return toReturn;
                }
                if (args[1].equalsIgnoreCase("unjoin")) {
                    List<Player> playerList = Bukkit.getOnlinePlayers()
                            .stream()
                            .filter(p -> p.getName().toLowerCase().startsWith(args[2].toLowerCase()))
                            .collect(Collectors.toList());

                    List<String> toReturn = new ArrayList<>();
                    playerList.forEach(x -> toReturn.add(x.getName()));
                    return toReturn;
                }
            }
            if (args[0].equalsIgnoreCase("map")) {
                if (args[1].equalsIgnoreCase("add")) {
                    if (Wipeout.get().getGameManager().getActiveMap() == null) {
                        return new ArrayList<>();
                    }

                    if (Wipeout.get().getGameManager().getType() == GameType.SOLO) {
                        List<Player> playerList = Bukkit.getOnlinePlayers()
                                .stream()
                                .filter(p -> p.getName().toLowerCase().startsWith(args[2].toLowerCase()))
                                .collect(Collectors.toList());

                        List<String> toReturn = new ArrayList<>();
                        playerList.forEach(x -> toReturn.add(x.getName()));
                        return toReturn;
                    } else {

                        List<Team> teams = Wipeout.get().getTeamManager().getTeamList().stream().filter(team -> team.getId().toLowerCase().startsWith(args[2].toLowerCase())).collect(Collectors.toList());

                        List<String> toReturn = new ArrayList<>();
                        teams.forEach(x -> toReturn.add(x.getId()));

                        return toReturn;
                    }
                }
                if (args[1].equalsIgnoreCase("load")) {
                    return Arrays.asList("map1", "map2", "map3");
                }
            }
        }
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("team")) {
                if (args[1].equalsIgnoreCase("join")) {
                    List<Team> teams = Wipeout.get().getTeamManager().getTeamList().stream().filter(team -> team.getId().toLowerCase().startsWith(args[3].toLowerCase())).toList();

                    List<String> toReturn = new ArrayList<>();
                    teams.forEach(x -> toReturn.add(x.getId()));

                    return toReturn;
                }
                if (args[1].equalsIgnoreCase("unjoin")) {
                    List<Team> teams = Wipeout.get().getTeamManager().getTeamList().stream().filter(team -> team.getId().toLowerCase().startsWith(args[3].toLowerCase())).collect(Collectors.toList());

                    List<String> toReturn = new ArrayList<>();
                    teams.forEach(x -> toReturn.add(x.getId()));

                    return toReturn;
                }
            }
        }
        return new ArrayList<>();
    }
}
