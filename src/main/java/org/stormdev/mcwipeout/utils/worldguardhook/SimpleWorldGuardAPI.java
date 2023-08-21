package org.stormdev.mcwipeout.utils.worldguardhook;
/*
  Created by Stormbits at 1/24/2023
*/

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.stormdev.mcwipeout.Wipeout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleWorldGuardAPI {

    public SimpleWorldGuardAPI() {
    }

    public ProtectedRegion getRegion(String regionId) {
        for (World world : Bukkit.getWorlds()) {
            final ProtectedRegion rg = getRegion(regionId, world);
            if (rg != null) {
                return rg;
            }
        }
        return null;
    }

    public boolean isInRegion(Location loc, String regionId) {
        for (ProtectedRegion region : getRegions(loc)) {
            if (region.getId().equalsIgnoreCase(regionId)) {
                return true;
            }
        }
        return false;
    }


    public ProtectedRegion getRegion(String regionId, World world) {
        final RegionManager regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        if (regions != null) {
            return regions.getRegion(regionId);
        }
        return null;
    }


    public Map<String, ProtectedRegion> getRegions() {
        final Map<String, ProtectedRegion> regions = new HashMap<>();
        final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        for (World world : Bukkit.getWorlds()) {
            regions.putAll(container.get(BukkitAdapter.adapt(world)).getRegions());
        }
        return regions;
    }

    /**
     * Receives all {@link ProtectedRegion ProtectedRegions} that exists in a {@link World}
     *
     * @param world {@link World} to be checked
     * @return A {@link Map} of all {@link ProtectedRegion ProtectedRegions} that exists in a {@link World}.
     */
    public Map<String, ProtectedRegion> getRegions(World world) {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world)).getRegions();
    }


    public ApplicableRegionSet getRegions(Location loc) {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(BukkitAdapter.adapt(loc));
    }

    public List<Player> getPlayersInRegion(String regionId) {
        final List<Player> players = new ArrayList<>();
        for (WgPlayer wgPlayer : Wipeout.get().getPlayerCache().values()) {
            wgPlayer.getRegions().forEach(protectedRegion -> {
                if (regionId.equals(protectedRegion.getId())) players.add(wgPlayer.getPlayer());
            });
        }
        return players;
    }

    /**
     * Receives a {@link List} of players that are in a specific region via {@link ProtectedRegion}.
     *
     * @param protectedRegion {@link ProtectedRegion} to be checked
     * @return List of {@link Player Players}
     */
    public List<Player> getPlayersInRegion(ProtectedRegion protectedRegion) {
        final List<Player> players = new ArrayList<>();
        for (WgPlayer wgPlayer : Wipeout.get().getPlayerCache().values()) {
            if (wgPlayer.getRegions().contains(protectedRegion)) {
                players.add(wgPlayer.getPlayer());
            }
        }
        return players;
    }
}
