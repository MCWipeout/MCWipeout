package org.stormdev.mcwipeout.frame.obstacles.redballs;
/*
  Created by Stormbits at 9/12/2023
*/

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;

public class RedBallsObstacle extends Obstacle {

    private int speedDuration, jumpDuration;

    public RedBallsObstacle(int speedDuration, int jumpDuration) {
        this.speedDuration = speedDuration;
        this.jumpDuration = jumpDuration;
    }

    @Override
    public void handle(Event event) {
        if (!isEnabled()) return;
        if (event instanceof PlayerMoveEvent) {
            PlayerMoveEvent e = (PlayerMoveEvent) event;

            Location to = e.getTo();
            Block steppedOn = to.clone().add(0.0, -1.0, 0.0).getBlock();

            if (steppedOn.getType() == Material.EMERALD_BLOCK) {
                if (!e.getPlayer().hasPotionEffect(PotionEffectType.SPEED) && !e.getPlayer().hasPotionEffect(PotionEffectType.JUMP)) {
                    Wipeout.get().getAdventure().player(e.getPlayer()).playSound(Sound.sound(Key.key("wipeout:mcw.sfx.jump"), Sound.Source.MASTER, 1.0f, 1.0f));
                }

                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedDuration, 6));
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, jumpDuration, 10));
            }
        }
    }

    @Override
    public void run() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void enable() {

    }

    @Override
    public void load() {

    }
}
