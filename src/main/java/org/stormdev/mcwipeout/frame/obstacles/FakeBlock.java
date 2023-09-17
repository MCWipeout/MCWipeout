package org.stormdev.mcwipeout.frame.obstacles;
/*
  Created by Stormbits at 9/17/2023
*/

import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.utils.helpers.FallingBlockFactory;

public class FakeBlock {

    private ArmorStand armorStand;
    private Shulker shulker;

    private FallingBlock fallingBlock;

    public FakeBlock(Location location, Material material) {
        armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setGravity(false);

        shulker = location.getWorld().spawn(location, Shulker.class);

        shulker.setAI(false);

        armorStand.setHelmet(new ItemStack(Material.STONE));

        shulker.setInvulnerable(true);

        fallingBlock = FallingBlockFactory.buildFallingBlock(location, material);

        shulker.setSilent(true);

        armorStand.addPassenger(shulker);
        armorStand.teleport(location);
    }

    public void moveTo(final float x, final float y, final float z, int duration, final boolean mirror) {

        new BukkitRunnable() {

            int timer = 0;
            float xTranslation = x / ((float) duration /2);
            float yTranslation = y / ((float) duration /2);
            float zTranslation = z / ((float) duration /2);

            boolean toReset = mirror;

            @Override
            public void run() {
                timer++;
                if (timer <= duration/2) {

                    fallingBlock.teleport(fallingBlock.getLocation().add(xTranslation, yTranslation, zTranslation));

                    CraftArmorStand craftArmorStand = (CraftArmorStand) armorStand;
                    net.minecraft.world.entity.decoration.ArmorStand armorStand = craftArmorStand.getHandle();

                    armorStand.setPos(armorStand.getX() + xTranslation, armorStand.getY() + yTranslation, armorStand.getZ() + zTranslation);

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        CraftPlayer craftPlayer = (CraftPlayer) player;
                        craftPlayer.getHandle().connection.send(new ClientboundTeleportEntityPacket(armorStand));
                    }


                } else {
                    if (toReset) {
                        xTranslation = -x / ((float) duration /2);
                        yTranslation = -y / ((float) duration /2);
                        zTranslation = -z / ((float) duration /2);

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
        if (armorStand != null) armorStand.remove();
        if (shulker != null) shulker.remove();
    }
}
