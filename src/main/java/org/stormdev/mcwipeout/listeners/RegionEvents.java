package org.stormdev.mcwipeout.listeners;
/*
  Created by Stormbits at 2/12/2023
*/

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.stormdev.abstracts.StormListener;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.utils.worldguardhook.RegionEnteredEvent;

public class RegionEvents extends StormListener<Wipeout> {


    public RegionEvents(Wipeout plugin) {
        super(plugin);
    }

    @EventHandler
    public void onRegionEnter(RegionEnteredEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.SPECTATOR) {
            if (e.getRegion().getId().contains("map_one_oob")) {
                if (e.getPlayer().isOp()) return;
                if (plugin().getGameManager().getActiveMap() != null) {
                    if (plugin().getPlayer(e.getPlayer().getUniqueId()).isInRegion("dropper_conflict")) return;
                    plugin().getGameManager().getActiveMap().handleCheckPoint(e.getPlayer());
                }
            }
            if (e.getRegion().getId().contains("map_two_oob")) {
                if (e.getPlayer().isOp()) return;
                if (plugin().getGameManager().getActiveMap() != null) {
                    if (plugin().getPlayer(e.getPlayer().getUniqueId()).isInRegion("map_two_dropper")) return;
                    plugin().getGameManager().getActiveMap().handleCheckPoint(e.getPlayer());
                }
            }
            if (e.getRegion().getId().contains("map_three_oob")) {
                if (e.getPlayer().isOp()) return;
                if (plugin().getGameManager().getActiveMap() != null) {
                    if (plugin().getPlayer(e.getPlayer().getUniqueId()).isInRegion("map_three_dropper")) return;
                    plugin().getGameManager().getActiveMap().handleCheckPoint(e.getPlayer());
                }
            }

            if (e.getRegion().getId().contains("map_four_oob")) {
                if (e.getPlayer().isOp()) return;
                if (plugin().getGameManager().getActiveMap() != null) {
                    if (plugin().getPlayer(e.getPlayer().getUniqueId()).isInRegion("map_four_dropper") || plugin().getPlayer(e.getPlayer().getUniqueId()).isInRegion("map_four_lev_dropper"))
                        return;
                    plugin().getGameManager().getActiveMap().handleCheckPoint(e.getPlayer());
                }
            }
            if (e.getRegion().getId().contains("map_five_oob")) {
                if (e.getPlayer().isOp()) return;
                if (plugin().getGameManager().getActiveMap() != null) {
                    if (plugin().getPlayer(e.getPlayer().getUniqueId()).isInRegion("map_five_dropper") || plugin().getPlayer(e.getPlayer().getUniqueId()).isInRegion("map_five_lev_dropper"))
                        return;
                    plugin().getGameManager().getActiveMap().handleCheckPoint(e.getPlayer());
                }
            }
            if (e.getRegion().getId().contains("map_6_oob")) {
                if (e.getPlayer().isOp()) return;
                if (plugin().getGameManager().getActiveMap() != null) {
                    if (plugin().getPlayer(e.getPlayer().getUniqueId()).isInRegion("map_6_dropper") || plugin().getPlayer(e.getPlayer().getUniqueId()).isInRegion("map_6_lev_dropper"))
                        return;
                    plugin().getGameManager().getActiveMap().handleCheckPoint(e.getPlayer());
                }
            }

            if (e.getRegion().getId().contains("finish")) {
                if (plugin().getGameManager().getActiveMap() != null) {
                    plugin().getGameManager().finish(e.getRegion().getId().toLowerCase(), e.getPlayer());
                }
            }
        }

        if (e.getRegion().getId().contains("cp")) {
            if (plugin().getGameManager().getActiveMap() != null) {
                plugin().getGameManager().getActiveMap().moveCheckPoint(e.getRegion().getId(), e.getPlayer());
            }
        }
    }
}
