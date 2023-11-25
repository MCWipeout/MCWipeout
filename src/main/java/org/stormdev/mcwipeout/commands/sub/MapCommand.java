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
                    plugin.getGameManager().getActiveMap().getTeamsPlaying().clear();

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!player.hasPermission("wipeout.play")) continue;
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
            if (args[0].equalsIgnoreCase("forcestop") || args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("end")) {
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
                plugin.getGameManager().setActiveMap(null);
                sender.sendMessage(ChatColor.GREEN + "Unloaded map!");
                return;
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                if (plugin.getGameManager().getActiveMap() != null) {
                    if (plugin.getGameManager().getType() == GameType.TEAMS) {
                        Optional<Team> team = plugin.getTeamManager().getTeam(args[1]);
                        if (team.isEmpty()) {
                            sender.sendMessage(ChatColor.RED + "Team does not exist!");
                            return;
                        }

                        if (plugin.getGameManager().getActiveMap().getTeamsPlaying().contains(team.get())) {
                            sender.sendMessage(ChatColor.RED + "Team is already playing!");
                            return;
                        } else {
                            plugin.getGameManager().getActiveMap().getTeamsPlaying().add(team.get());

                            sender.sendMessage(ChatColor.GREEN + "Team added!");
                            team.get().sendTeamMessage(ChatColor.GREEN + "You have been added to the playing teams!");

                        }
                    } else {
                        Player player = Bukkit.getPlayer(args[1]);
                        if (player == null) {
                            sender.sendMessage(ChatColor.RED + "Player not found!");
                            return;
                        }

                        Team team = new Team(player.getName(), new ArrayList<>());
                        team.add(player);
                        plugin.getGameManager().getActiveMap().getTeamsPlaying().add(team);

                        sender.sendMessage(ChatColor.GREEN + "Player added!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "There is no active map!");
                    return;
                }
            }

        }
        if (args[0].equalsIgnoreCase("load")) {
            if (plugin.getGameManager().getActiveMap() != null) {
                sender.sendMessage(ChatColor.GREEN + "There already is a map loaded! Either unload or start it.");
                return;

            } else {
                if (args[1].equalsIgnoreCase("map1") || args[1].equalsIgnoreCase("mapone")) {
                    plugin.getGameManager().setActiveMap(plugin.getMapManager().getMaps().get("map1"));
                    sender.sendMessage(ChatColor.GREEN + "Map loaded!");
                    return;
                }
                if (args[1].equalsIgnoreCase("map2") || args[1].equalsIgnoreCase("maptwo")) {
                    plugin.getGameManager().setActiveMap(plugin.getMapManager().getMaps().get("map2"));
                    sender.sendMessage(ChatColor.GREEN + "Map loaded!");
                    return;
                }
                if (args[1].equalsIgnoreCase("map3") || args[1].equalsIgnoreCase("mapthree")) {
                    plugin.getGameManager().setActiveMap(plugin.getMapManager().getMaps().get("map3"));
                    sender.sendMessage(ChatColor.GREEN + "Map loaded!");
                    return;
                }
                if (args[1].equalsIgnoreCase("map4") || args[1].equalsIgnoreCase("mapfour")) {
                    plugin.getGameManager().setActiveMap(plugin.getMapManager().getMaps().get("map4"));
                    sender.sendMessage(ChatColor.GREEN + "Map loaded!");
                    return;
                }
            }
        }

        if (args[0].equals("endtimer")) {
            if (plugin.getGameManager().getActiveMap() != null) {
                plugin.getGameManager().endOfGameTimer();
                sender.sendMessage(ChatColor.GREEN + "Started end timer");
                return;
            }

        }
    }
}

