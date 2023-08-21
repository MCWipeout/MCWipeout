package org.stormdev.mcwipeout.utils.worldguardhook;
/*
  Created by Stormbits at 1/24/2023
*/

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

/**
 * Thrown when a {@link Player} is about to enter a {@link ProtectedRegion}
 */
public class RegionEnteredEvent extends RegionEvent {

    public RegionEnteredEvent(ProtectedRegion region, Player player, MovementWay movement, PlayerEvent parent, Location from, Location to) {
        super(region, player, movement, parent, from, to);
    }

}
