package org.stormdev.mcwipeout.frame.obstacles;
/*
  Created by Stormbits at 9/17/2023
*/

import lombok.Getter;
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

public class FakeBlock {

    private ArmorStand shulkerArmorStand;
    private Shulker shulker;

    private ArmorStand blockArmorStand;

    @Getter
    private BlockDisplay displayEntity;

    public FakeBlock(Location location, Material material, boolean shouldShulkerSpawn) {
        Location loc = location.clone();
        shulkerArmorStand = location.getWorld().spawn(loc.clone().add(0, -1.5, 0), ArmorStand.class);
        shulkerArmorStand.setGravity(false);

        blockArmorStand = location.getWorld().spawn(loc.clone().add(-0.5, -1.5, -0.5), ArmorStand.class);
        blockArmorStand.setGravity(false);

        if (shouldShulkerSpawn) {
            shulker = location.getWorld().spawn(loc, Shulker.class);
            shulker.setInvulnerable(true);
            shulker.setAI(false);
            shulker.setSilent(true);
            shulker.setInvisible(true);
        }

        //ENABLE FOR DEBUG:
        //shulkerArmorStand.setHelmet(new ItemStack(Material.STONE));


        displayEntity = location.getWorld().spawn(loc, BlockDisplay.class);
        displayEntity.setBlock(material.createBlockData());

        blockArmorStand.addPassenger(displayEntity);

        if (shouldShulkerSpawn) {
            shulkerArmorStand.addPassenger(shulker);
        }
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
                timer++;
                if (timer <= duration / 2) {

                    CraftArmorStand craftArmorStand = (CraftArmorStand) shulkerArmorStand;
                    net.minecraft.world.entity.decoration.ArmorStand armorStand = craftArmorStand.getHandle();

                    armorStand.setPos(armorStand.getX() + xTranslation, armorStand.getY() + yTranslation, armorStand.getZ() + zTranslation);

                    CraftArmorStand craftArmorStand1 = (CraftArmorStand) blockArmorStand;
                    net.minecraft.world.entity.decoration.ArmorStand armorStand1 = craftArmorStand1.getHandle();

                    armorStand1.setPos(armorStand1.getX() + xTranslation, armorStand1.getY() + yTranslation, armorStand1.getZ() + zTranslation);

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        CraftPlayer craftPlayer = (CraftPlayer) player;
                        craftPlayer.getHandle().connection.send(new ClientboundTeleportEntityPacket(armorStand));
                        craftPlayer.getHandle().connection.send(new ClientboundTeleportEntityPacket(armorStand1));
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
        if (shulkerArmorStand != null) shulkerArmorStand.remove();
        if (shulker != null) shulker.remove();
        if (blockArmorStand != null) blockArmorStand.remove();
        if (displayEntity != null) displayEntity.remove();
    }

    public void removeShulker() {
        if (shulker != null) shulker.remove();
    }
}
