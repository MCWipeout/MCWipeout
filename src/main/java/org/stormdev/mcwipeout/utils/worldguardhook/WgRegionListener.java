package org.stormdev.mcwipeout.utils.worldguardhook;
/*
  Created by Stormbits at 1/24/2023
*/

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.stormdev.mcwipeout.Wipeout;

public class WgRegionListener implements Listener {

    private final Wipeout plugin;

    public WgRegionListener(Wipeout plugin) {
        this.plugin = plugin;
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGHEST
    )
    public void onLogin(PlayerLoginEvent e) {
        if(e.getResult() != PlayerLoginEvent.Result.ALLOWED) return;
        plugin.getPlayerCache().remove(e.getPlayer().getUniqueId());
        plugin.getPlayerCache().put(e.getPlayer().getUniqueId(), new WgPlayer(e.getPlayer()));
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onJoin(PlayerJoinEvent e) {
        final WgPlayer wp = plugin.getPlayer(e.getPlayer().getUniqueId());
        if(wp == null) return;

        wp.updateRegions(MovementWay.SPAWN, e.getPlayer().getLocation(), e.getPlayer().getLocation(), e);

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGHEST
    )
    public void onKick(PlayerKickEvent e) {
        final WgPlayer wp = plugin.getPlayer(e.getPlayer().getUniqueId());
        if(wp == null) return;

        for (ProtectedRegion region : wp.getRegions()) {
            final RegionLeftEvent leftEvent = new RegionLeftEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e, e.getPlayer().getLocation(), e.getPlayer().getLocation());
            Bukkit.getPluginManager().callEvent(leftEvent);
        }
        wp.getRegions().clear();
        plugin.getPlayerCache().remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onQuit(PlayerQuitEvent e) {
        final WgPlayer wp = plugin.getPlayer(e.getPlayer().getUniqueId());
        if(wp == null) return;

        for (ProtectedRegion region : wp.getRegions()) {
            final RegionLeftEvent leftEvent = new RegionLeftEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e, e.getPlayer().getLocation(), e.getPlayer().getLocation());
            Bukkit.getPluginManager().callEvent(leftEvent);

        }
        wp.getRegions().clear();
        plugin.getPlayerCache().remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGHEST
    )
    public void onMove(PlayerMoveEvent e) {
        final WgPlayer wp = plugin.getPlayer(e.getPlayer().getUniqueId());
        if(wp == null) return;

        e.setCancelled(wp.updateRegions(MovementWay.MOVE, e.getTo(), e.getFrom(), e));
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGHEST
    )
    public void onMove(PlayerTeleportEvent e) {
        final WgPlayer wp = plugin.getPlayer(e.getPlayer().getUniqueId());
        if(wp == null) return;

        e.setCancelled(wp.updateRegions(MovementWay.TELEPORT, e.getTo(), e.getFrom(), e));
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onRespawn(PlayerRespawnEvent e) {
        final WgPlayer wp = plugin.getPlayer(e.getPlayer().getUniqueId());
        if(wp == null) return;

        wp.updateRegions(MovementWay.SPAWN, e.getRespawnLocation(), e.getPlayer().getLocation(), e);
    }

}
