package org.stormdev.mcwipeout.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stormdev.commands.CommandContext;
import org.stormdev.commands.StormSubCommand;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.team.Team;
import org.stormdev.utils.Color;
import org.stormdev.utils.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeamCommand extends StormSubCommand {

    private Wipeout plugin;
    private Set<String> aliases;

    public TeamCommand(Wipeout plugin) {
        super(1, "&cInvalid Args!");
        this.plugin = plugin;
        this.aliases = new HashSet<>();
        aliases.add("team");
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
            if (args[0].equalsIgnoreCase("clear")) {
                plugin.getTeamManager().getTeamList().clear();
                sender.sendMessage(ChatColor.RED + "Team List cleared!");
                return;
            }
            if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(Color.colorize("&8&m-----------------------------------"));
                for (Team team : plugin.getTeamManager().getTeamList()) {
                    String message = "";

                    for (UUID member : team.getUUIDMembers()) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(member);

                        if (!target.isOnline()) {
                            message += ChatColor.RED + target.getName() + " ";
                        } else {
                            message += ChatColor.GREEN + target.getName() + " ";
                        }
                    }
                    sender.sendMessage(StringUtils.hex(team.getColor() + team.getId() + "&7: " + message));
                }
                sender.sendMessage(Color.colorize("&8&m-----------------------------------"));
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                if (!plugin.getTeamManager().exists(args[1])) {
                    plugin.getTeamManager().addTeam(args[1]);
                    sender.sendMessage(ChatColor.GREEN + "Team succesfully created!");
                    return;

                } else {
                    sender.sendMessage(ChatColor.RED + "A team with that name already exists!");

                    return;
                }
            }
            if (args[0].equalsIgnoreCase("delete")) {
                if (plugin.getTeamManager().exists(args[1])) {
                    plugin.getTeamManager().getTeamList().remove(plugin.getTeamManager().getTeam(args[1]).get());
                    sender.sendMessage(ChatColor.GREEN + "Team succesfully removed!");
                    return;

                } else {
                    sender.sendMessage(ChatColor.RED + "A team with that name does not exist!");

                    return;
                }
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setcolor")) {
                if (!plugin.getTeamManager().exists(args[1])) {
                    sender.sendMessage(ChatColor.RED + "Team does not exist!");
                    return;

                } else {
                    plugin.getTeamManager().getTeam(args[1]).get().setColor(args[2]);

                    sender.sendMessage(ChatColor.GREEN + "Succesfully changed the color!");

                    return;
                }
            }
            if (args[0].equalsIgnoreCase("rename")) {
                if (!plugin.getTeamManager().exists(args[1])) {
                    sender.sendMessage(ChatColor.RED + "Team does not exist!");
                    return;

                } else {
                    plugin.getTeamManager().getTeam(args[1]).get().setId(args[2]);

                    sender.sendMessage(ChatColor.GREEN + "Team has been renamed!");

                    return;
                }
            }
            if (args[0].equalsIgnoreCase("join")) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player does not exist!");

                    return;
                }
                if (plugin.getTeamManager().exists(args[2])) {
                    Team team = plugin.getTeamManager().getTeam(args[2]).get();
                    if (team.containsPlayer(target)) {
                        sender.sendMessage(ChatColor.RED + "Player is already a member of that team!");
                        return;
                    }
                    team.add(target);

                    sender.sendMessage(ChatColor.GREEN + "Player succesfully added!");
                    if (target.isOnline()) {
                        target.getPlayer().sendMessage(StringUtils.hex("&aYou have been added to team: " + team.getColor() + team.getId()));
                    }
                    return;

                } else {
                    sender.sendMessage(ChatColor.RED + "A team with that name does not eixst!");

                    return;
                }
            }
            if (args[0].equalsIgnoreCase("unjoin")) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player does not exist!");

                    return;
                }
                if (plugin.getTeamManager().exists(args[2])) {
                    Team team = plugin.getTeamManager().getTeam(args[2]).get();
                    if (!team.containsPlayer(target)) {
                        sender.sendMessage(ChatColor.RED + "Player is not a member of that team!");
                        return;
                    }
                    team.remove(target);

                    sender.sendMessage(ChatColor.GREEN + "Player succesfully removed!");

                    if (target.isOnline()) {
                        target.getPlayer().sendMessage(StringUtils.hex("&aYou have been removed from team: " + team.getColor() + team.getId()));
                    }
                    return;

                } else {
                    sender.sendMessage(ChatColor.RED + "A team with that name does not eixst!");

                    return;
                }
            }
        }
    }
}
