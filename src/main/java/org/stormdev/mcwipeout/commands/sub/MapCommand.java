package org.stormdev.mcwipeout.commands.sub;
/*
  Created by Stormbits at 2/12/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.commands.CommandContext;
import org.stormdev.commands.StormSubCommand;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.game.GameType;
import org.stormdev.mcwipeout.frame.team.Team;

import java.util.*;

public class MapCommand extends StormSubCommand {

    private Wipeout plugin;
    private Set<String> aliases;

    public MapCommand(Wipeout plugin) {
        super(1, "&cInvalid Args!");
        this.plugin = plugin;
        this.aliases = new HashSet<>();
        aliases.add("map");
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

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("type")) {
                if (plugin.getGameManager().getType() == GameType.SOLO) {
                    plugin.getGameManager().setType(GameType.TEAMS);
                    sender.sendMessage(ChatColor.GREEN + "Succesfully changed game type to TEAMS");
                } else {
                    plugin.getGameManager().setType(GameType.SOLO);
                    sender.sendMessage(ChatColor.GREEN + "Succesfully changed game type to SOLO");
                }
            }
            if (args[0].equalsIgnoreCase("autoteam")) {

                if (plugin.getGameManager().getActiveMap() == null) {
                    sender.sendMessage(ChatColor.RED + "There is no map loaded!");
                    return;
                }

                if (plugin.getGameManager().getType() == GameType.SOLO) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("wipeout.admin") || player.isOp()) continue;
                        Team team = new Team(player.getName(), new ArrayList<>());
                        team.add(player);
                        plugin.getGameManager().getActiveMap().getTeamsPlaying().add(team);
                    }
                } else {
                    if (plugin.getTeamManager().getTeamList().isEmpty()) {
                        sender.sendMessage(ChatColor.RED + "There are no teams!");
                        return;
                    }

                    for (Team team : plugin.getTeamManager().getTeamList()) {
                        boolean addTeam = false;
                        for (UUID uuid : team.getUUIDMembers()) {
                            if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                                addTeam = true;
                            }
                        }

                        if (addTeam) {
                            plugin.getGameManager().getActiveMap().getTeamsPlaying().add(team);
                            sender.sendMessage(ChatColor.GREEN + "Team: " + team.getId() + " has been added to the list!");
                        }
                    }
                }

                sender.sendMessage(ChatColor.GREEN + "All teams/players have been added!");
                return;
            }
            if (args[0].equalsIgnoreCase("start")) {

                if (plugin.getGameManager().getActiveMap() != null) {

                    plugin.getGameManager().start();

                    sender.sendMessage(ChatColor.GREEN + "Map started!");

                } else {
                    sender.sendMessage(ChatColor.RED + "There is no active map!");
                }

                return;
            }
            if (args[0].equalsIgnoreCase("forcestop")) {
                if (plugin.getGameManager().getActiveMap() == null) {
                    sender.sendMessage(ChatColor.RED + "There is no map running!");
                    return;
                }

                if (plugin.getGameManager().getTask() != null) {
                    plugin.getGameManager().getTask().cancel();
                }

                plugin.getGameManager().stopActiveMap();

                sender.sendMessage(ChatColor.RED + "Map stopped!");
            }
            if (args[0].equalsIgnoreCase("unload")) {
                if (plugin.getGameManager().getActiveMap() == null) {
                    sender.sendMessage(ChatColor.GREEN + "There is no map loaded!");

                } else {
                    plugin.getGameManager().stopActiveMap();

                    sender.sendMessage(ChatColor.GREEN + "Succesfully disabled the map!");
                }
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("addteam")) {
                Optional<Team> team = plugin.getTeamManager().getTeam(args[1]);
                if (team.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Team does not exist!");

                    return;
                }
                if (plugin.getGameManager().getActiveMap() != null) {
                    if (plugin.getGameManager().getActiveMap().getTeamsPlaying().contains(team.get())) {
                        sender.sendMessage(ChatColor.RED + "Team is already playing!");
                        return;
                    } else {
                        plugin.getGameManager().getActiveMap().getTeamsPlaying().add(team.get());

                        sender.sendMessage(ChatColor.GREEN + "Team added!");
                        team.get().sendTeamMessage(ChatColor.GREEN + "You have been added to the playing teams!");

                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "There is no active map!");
                }

            }
            if (args[0].equalsIgnoreCase("load")) {
                if (plugin.getGameManager().getActiveMap() != null) {
                    sender.sendMessage(ChatColor.GREEN + "There already is a map loaded! Either unload or start it.");

                } else {
                    if (args[1].equalsIgnoreCase("map1") || args[1].equalsIgnoreCase("mapone")) {
                        plugin.getGameManager().setActiveMap(plugin.getMapManager().getMaps().get("map1"));
                        sender.sendMessage(ChatColor.GREEN + "Map loaded!");
                    }
                }
            }
        }
    }
}
