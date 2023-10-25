package org.stormdev.mcwipeout.listeners;
/*
  Created by Stormbits at 2/12/2023
*/

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

            if (plugin().getMapManager().isOOB(e)) {
                if (!e.getPlayer().hasPermission("wipeout.play")) return;
                if (plugin().getGameManager().getActiveMap() != null) {
                    plugin().getGameManager().getActiveMap().handleCheckPoint(e.getPlayer());
                }
            }

            if (e.getRegion().getId().contains("finish")) {
                if (plugin().getGameManager().getActiveMap() != null) {
                    if (!e.getPlayer().hasPotionEffect(PotionEffectType.ABSORPTION)) {
                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20, 1));
                        plugin().getGameManager().finish(e.getRegion().getId().toLowerCase(), e.getPlayer());
                        return;
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
}
