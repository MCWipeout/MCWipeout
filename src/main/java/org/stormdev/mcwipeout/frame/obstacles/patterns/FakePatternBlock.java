package org.stormdev.mcwipeout.frame.obstacles.patterns;
/*
  Created by Stormbits at 9/17/2023
*/

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraft.network.protocol.BundleDelimiterPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;

@Getter
public class FakePatternBlock {

    private ArmorStand shulkerArmorStand;
    private Shulker shulker;

    private ArmorStand blockArmorStand;

    @Getter
    private BlockDisplay displayEntity;

    public FakePatternBlock(Location location, Material material, boolean shouldShulkerSpawn, boolean useTypeMaterial) {
        Location loc = location.clone();
        shulkerArmorStand = location.getWorld().spawn(loc.clone().add(0, -1.5, 0), ArmorStand.class);
        shulkerArmorStand.setGravity(false);
        shulkerArmorStand.setCustomNameVisible(false);
        shulkerArmorStand.setCustomName("wipeout-entity");

        if (useTypeMaterial) {
            blockArmorStand = location.getWorld().spawn(loc.clone().add(-0.5, -1.5, -0.5), ArmorStand.class);
            blockArmorStand.setGravity(false);
            blockArmorStand.setCustomNameVisible(false);
            blockArmorStand.setCustomName("wipeout-entity");
        }


        if (shouldShulkerSpawn) {
            shulker = location.getWorld().spawn(loc, Shulker.class);
            shulker.setInvulnerable(true);
            shulker.setAI(false);
            shulker.setSilent(true);
            shulker.setInvisible(true);
            shulker.setCustomNameVisible(false);
            shulker.setCustomName("wipeout-entity");
        }

        if (useTypeMaterial) {

            displayEntity = location.getWorld().spawn(loc, BlockDisplay.class);
            displayEntity.setBlock(material.createBlockData());
            displayEntity.setCustomNameVisible(false);
            displayEntity.setCustomName("wipeout-entity");

            blockArmorStand.addPassenger(displayEntity);
        }

        if (shouldShulkerSpawn) {
            shulkerArmorStand.addPassenger(shulker);
        }
    }

    public void teleport(float x, float z) {
        CraftArmorStand craftArmorStand = (CraftArmorStand) shulkerArmorStand;
        net.minecraft.world.entity.decoration.ArmorStand armorStand = craftArmorStand.getHandle();

        armorStand.setPos(x, armorStand.getY(), z);

        CraftArmorStand craftArmorStand1;
        net.minecraft.world.entity.decoration.ArmorStand armorStand1 = null;

        if (blockArmorStand != null) {
            craftArmorStand1 = (CraftArmorStand) blockArmorStand;
            armorStand1 = craftArmorStand1.getHandle();

            armorStand1.setPos(x, armorStand1.getY(), z);
        }

        Packet armorStandPacket = new ClientboundTeleportEntityPacket(armorStand);
        Packet armorStand1Packet = new ClientboundTeleportEntityPacket(armorStand1);

        for (Player player : Bukkit.getOnlinePlayers()) {

            //if (player.getLocation().distanceSquared(armorStand.getBukkitEntity().getLocation()) > 900) continue;

            if (armorStand1 != null) {
                Wipeout.get().getPacketBundler().addPackets(player, armorStandPacket, armorStand1Packet);
            } else {
                Wipeout.get().getPacketBundler().addPackets(player, armorStandPacket);
            }
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

                    CraftArmorStand craftArmorStand1 = null;
                    net.minecraft.world.entity.decoration.ArmorStand armorStand1 = null;
                    if (blockArmorStand != null) {
                        craftArmorStand1 = (CraftArmorStand) blockArmorStand;
                        armorStand1 = craftArmorStand1.getHandle();

                        armorStand1.setPos(armorStand1.getX() + xTranslation, armorStand1.getY() + yTranslation, armorStand1.getZ() + zTranslation);
                    }

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        CraftPlayer craftPlayer = (CraftPlayer) player;
                        craftPlayer.getHandle().connection.send(new ClientboundTeleportEntityPacket(armorStand));
                        if (armorStand1 != null) {
                            craftPlayer.getHandle().connection.send(new ClientboundTeleportEntityPacket(armorStand1));
                        }
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
}
