package org.stormdev.mcwipeout.listeners;
/*
  Created by Stormbits at 2/12/2023
*/

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.stormdev.abstracts.StormListener;
import org.stormdev.mcwipeout.Wipeout;

public class ObstacleEvents extends StormListener<Wipeout> {


    public ObstacleEvents(Wipeout plugin) {
        super(plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        if (plugin().getGameManager().getActiveMap() != null) {
            plugin().getGameManager().getActiveMap().getObstacles().forEach(obstacle -> obstacle.handle(event));
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (plugin().getGameManager().getActiveMap() != null) {
            plugin().getGameManager().getActiveMap().getObstacles().forEach(obstacle -> obstacle.handle(event));
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (plugin().getGameManager().getActiveMap() != null) {
            plugin().getGameManager().getActiveMap().getObstacles().forEach(obstacle -> obstacle.handle(event));
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (plugin().getGameManager().getActiveMap() != null) {
            plugin().getGameManager().getActiveMap().getObstacles().forEach(obstacle -> obstacle.handle(event));
        }
    }
}
