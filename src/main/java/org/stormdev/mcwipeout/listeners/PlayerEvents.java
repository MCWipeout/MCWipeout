package org.stormdev.mcwipeout.listeners;
/*
  Created by Stormbits at 2/12/2023
*/

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.stormdev.abstracts.StormListener;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.commands.sub.ExportSnakeCommand;
import org.stormdev.mcwipeout.frame.game.GameType;
import org.stormdev.mcwipeout.frame.team.Team;
import org.stormdev.mcwipeout.frame.team.WipeoutPlayer;
import org.stormdev.mcwipeout.utils.Utils;
import org.stormdev.mcwipeout.utils.helpers.WLocation;
import org.stormdev.utils.Color;

import java.util.UUID;


public class PlayerEvents extends StormListener<Wipeout> {


    public PlayerEvents(Wipeout plugin) {
        super(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        org.bukkit.scoreboard.Team teamScoreboard = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("players");
        teamScoreboard.addPlayer(event.getPlayer());

        plugin().getTeamManager().getWipeoutPlayers().add(new WipeoutPlayer(event.getPlayer().getUniqueId(), false));
        if (event.getPlayer().isOp()) return;

        if (plugin().getGameManager().getActiveMap() == null) {
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999999, 1, false, false));

            event.getPlayer().teleport(new Location(Bukkit.getWorld("maps"), -94.5, 35, -361.5, 90F, 0.0F));

            /*

            ItemStack helmet = new ItemStack(Material.GHAST_TEAR);
            ItemMeta helmetMeta = helmet.getItemMeta();
            helmetMeta.setCustomModelData(10008);
            helmetMeta.setDisplayName(Color.colorize("&aHelmet"));
            helmet.setItemMeta(helmetMeta);

            event.getPlayer().getInventory().setHelmet(helmet);

             */

        }

        if (plugin().getGameManager().getActiveMap() != null) {
            if (plugin().getGameManager().getActiveMap().getTeamsPlaying() != null && !plugin().getGameManager().getActiveMap().getTeamsPlaying().isEmpty()) {
                boolean isFound = false;
                for (Team team : plugin().getGameManager().getActiveMap().getTeamsPlaying()) {
                    if (team.getUUIDMembers().contains(event.getPlayer().getUniqueId())) {
                        isFound = true;
                    }
                }

                if (!isFound) {
                    event.getPlayer().setGameMode(GameMode.SPECTATOR);
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.hidePlayer(plugin(), event.getPlayer());
                    }
                }
            }
            if (plugin().getGameManager().getFinishedTeams() != null && !plugin().getGameManager().getFinishedTeams().isEmpty())
                for (Team team : plugin().getGameManager().getFinishedTeams()) {
                    for (UUID member : team.getUUIDMembers()) {
                        if (member.equals(event.getPlayer().getUniqueId())) {
                            event.getPlayer().setGameMode(GameMode.SPECTATOR);
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                onlinePlayer.hidePlayer(plugin(), event.getPlayer());
                            }
                            return;
                        }
                    }
                }
        }
    }

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        if (event.getWhoClicked().isOp()) return;
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFreeze(PlayerMoveEvent event) {
        if (!event.getPlayer().hasPermission("wipeout.play")) return;

        if (event.getPlayer().isOp()) return;

        if (plugin().getGameManager().isFrozen()) {
            if (Utils.isSimilar(event.getFrom(), event.getTo())) {
                return;
            }

            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (event.getItemDrop().getItemStack().getType() == Material.GHAST_TEAR) {
            event.setCancelled(true);
        }
        if (event.getItemDrop().getItemStack().getType() == Material.EMERALD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamagePlayer(EntityDamageByEntityEvent event) {

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null || event.getItem().getType() == Material.AIR) return;

        if (event.getClickedBlock() != null && ExportSnakeCommand.enabled) {
            if (event.getItem() != null && event.getItem().getType() == Material.STICK) {
                ExportSnakeCommand.wLocations.add(WLocation.from(event.getClickedBlock()));
                player.sendMessage(ChatColor.YELLOW + "Exported snake location. " + ChatColor.GRAY + "(" + ExportSnakeCommand.wLocations.size() + ")");
            }
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack itemStack = event.getItem();
        if (itemStack.getType() != Material.EMERALD) return;

        if (plugin().getGameManager().getActiveMap() == null) return;

        WipeoutPlayer wipeoutPlayer = plugin().getTeamManager().fromUUID(player.getUniqueId());

        if (wipeoutPlayer.isVisiblePlayers()) {
            wipeoutPlayer.setVisiblePlayers(false);

            player.sendMessage(ChatColor.RED + "Disabled player visibility!");

            if (plugin().getGameManager().getType() == GameType.SOLO) {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.getUniqueId().equals(player.getUniqueId())) continue;
                    player.hidePlayer(plugin(), pl);
                }
                return;
            } else {
                Team team = plugin().getTeamManager().getTeamFromUUID(player.getUniqueId());
                if (team == null) return;

                plugin().getGameManager().getPlayersExcludeTeamMembers(team.getUUIDMembers(), player).forEach(pl -> player.hidePlayer(plugin(), pl));
            }
        } else {
            wipeoutPlayer.setVisiblePlayers(true);

            player.sendMessage(ChatColor.GREEN + "Enabled player visibility!");

            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.getUniqueId().equals(player.getUniqueId())) continue;
                player.showPlayer(plugin(), pl);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        WipeoutPlayer wipeoutPlayer = plugin().getTeamManager().fromUUID(event.getPlayer().getUniqueId());
        plugin().getTeamManager().getWipeoutPlayers().remove(wipeoutPlayer);


    }
}
