package org.stormdev.mcwipeout.listeners;
/*
  Created by Stormbits at 2/12/2023
*/

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.stormdev.abstracts.StormListener;
import org.stormdev.mcwipeout.Wipeout;

import java.util.concurrent.CompletableFuture;

public class ObstacleEvents extends StormListener<Wipeout> {


    public ObstacleEvents(Wipeout plugin) {
        super(plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        this.handleObstacles(event);
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        this.handleObstacles(event);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        this.handleObstacles(event);

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        this.handleObstacles(event);
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        this.handleObstacles(event);
    }


    private void handleObstacles(Event e){
        CompletableFuture.supplyAsync(
                () -> plugin().getGameManager().getActiveMap()).thenAccept(activeMap -> {
            if (activeMap != null) {
                activeMap.getObstacles().forEach(obstacle -> obstacle.handle(e));
            }
        });
    }
}
