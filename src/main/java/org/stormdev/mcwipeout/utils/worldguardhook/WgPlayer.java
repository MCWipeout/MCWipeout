package org.stormdev.mcwipeout.utils.worldguardhook;
/*
  Created by Stormbits at 1/24/2023
*/

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.stormdev.mcwipeout.Wipeout;

import java.util.*;

public class WgPlayer {

    private final Player player;
    private final List<ProtectedRegion> regions;

    public WgPlayer(Player player) {
        this.player = player;
        regions = new ArrayList<>();
    }

    public boolean updateRegions(MovementWay way, Location to, Location from, PlayerEvent parent) {
        Objects.requireNonNull(way, "MovementWay 'way' can't be null");
        Objects.requireNonNull(to, "Location 'to' can't be null");
        Objects.requireNonNull(from, "Location 'from' can't be null");

        final ApplicableRegionSet toRegions = Wipeout.get().getSimpleWorldGuardAPI().getRegions(to);
        final ApplicableRegionSet fromRegions = Wipeout.get().getSimpleWorldGuardAPI().getRegions(from);
        if (!toRegions.getRegions().isEmpty()) {
            for (ProtectedRegion region : toRegions) {
                if (!regions.contains(region)) {
                    regions.add(region);
                    Bukkit.getScheduler().runTaskLater(Wipeout.get(), () -> Bukkit.getPluginManager().callEvent(new RegionEnteredEvent(region, player, way, parent, from, to)), 1);
                }

            }

            final Set<ProtectedRegion> toRemove = new HashSet<>();

            for (ProtectedRegion oldRegion : fromRegions) {
                if (!toRegions.getRegions().contains(oldRegion)) {
                    Bukkit.getScheduler().runTaskLater(Wipeout.get(), () -> Bukkit.getPluginManager().callEvent(new RegionLeftEvent(oldRegion, player, way, parent, from, to)), 1);
                    toRemove.add(oldRegion);
                }
            }
            regions.removeAll(toRemove);

        } else {
            for (ProtectedRegion region : regions) {
                Bukkit.getScheduler().runTaskLater(Wipeout.get(), () -> Bukkit.getPluginManager().callEvent(new RegionLeftEvent(region, player, way, parent, from, to)), 1);
            }
            regions.clear();
        }

        return false;
    }

    public List<ProtectedRegion> getRegions() {
        return regions;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isInRegion(String id) {
        for (ProtectedRegion region : getRegions()) {
            if (region.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }
}
