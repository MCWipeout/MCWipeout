package org.stormdev.mcwipeout.frame.obstacles.bumpers.extended;
/*
  Created by Stormbits at 9/18/2023
*/

import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.utils.helpers.WLocation;

public class SlidingBlock {

    private WLocation wLocation;
    private Location location;

    private ArmorStand shulkerArmorStand;

    private Shulker shulker;

    private float dx, dz;


    public SlidingBlock(WLocation location, float xTranslation, float zTranslation) {
        this.wLocation = location;
        this.location = location.asLocation();

        this.dx = xTranslation;
        this.dz = zTranslation;
    }

    public void load() {
        shulkerArmorStand = location.getWorld().spawn(location.clone().add(0.5, -1.5, 0.5 - dz), ArmorStand.class);
        shulkerArmorStand.setGravity(false);

        shulker = location.getWorld().spawn(location.clone(), Shulker.class);
        shulker.setInvulnerable(true);
        shulker.setAI(false);
        shulker.setSilent(true);
        shulker.setInvisible(true);

        shulkerArmorStand.addPassenger(shulker);
    }

    public void moveTo(final float x, final float y, final float z, int duration, final boolean mirror) {

        new BukkitRunnable() {

            int timer = 0;
            float xTranslation = x / ((float) duration / 2);
            float yTranslation = y / ((float) duration / 2);
            float zTranslation = z / ((float) duration / 2);

            boolean toReset = mirror;

            @Override
            public void run() {
                if (shulkerArmorStand == null) {
                    this.cancel();
                    return;
                }

                timer++;
                if (timer <= duration / 2) {

                    CraftArmorStand craftArmorStand = (CraftArmorStand) shulkerArmorStand;
                    net.minecraft.world.entity.decoration.ArmorStand armorStand = craftArmorStand.getHandle();

                    armorStand.setPos(armorStand.getX() + xTranslation, armorStand.getY() + yTranslation, armorStand.getZ() + zTranslation);

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        CraftPlayer craftPlayer = (CraftPlayer) player;
                        craftPlayer.getHandle().connection.send(new ClientboundTeleportEntityPacket(armorStand));
                    }


                } else {
                    if (toReset) {
                        xTranslation = -x / ((float) duration / 2);
                        yTranslation = -y / ((float) duration / 2);
                        zTranslation = -z / ((float) duration / 2);

                        timer = 0;
                        toReset = false;
                        return;
                    } else {
                        this.cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(Wipeout.get(), 0L, 0L);
    }

    public void remove() {
        if (shulkerArmorStand != null) {
            shulkerArmorStand.remove();
            shulkerArmorStand = null;
        }
        if (shulker != null) {
            shulker.remove();
            shulker = null;
        }
    }
}
