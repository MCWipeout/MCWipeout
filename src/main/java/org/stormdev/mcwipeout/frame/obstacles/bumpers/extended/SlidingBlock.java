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
import org.stormdev.mcwipeout.utils.WLocation;

public class SlidingBlock {

    private WLocation wLocation;
    private Location location;
    private ArmorStand blockArmorStand;

    private ArmorStand shulkerArmorStand;

    private Shulker shulker;

    private BlockDisplay display;

    private float dx, dz;


    public SlidingBlock(WLocation location, float xTranslation, float zTranslation) {
        this.wLocation = location;
        this.location = location.asLocation();

        this.dx = xTranslation;
        this.dz = zTranslation;
    }

    public void load() {
        blockArmorStand = location.getWorld().spawn(location.clone().add(0, -1.5, 0 - dz), ArmorStand.class);
        blockArmorStand.setGravity(false);

        shulkerArmorStand = location.getWorld().spawn(location.clone().add(0.5, -1.5, 0.5 - dz), ArmorStand.class);
        shulkerArmorStand.setGravity(false);

        shulker = location.getWorld().spawn(location.clone(), Shulker.class);
        shulker.setInvulnerable(true);
        shulker.setAI(false);
        shulker.setSilent(true);
        shulker.setInvisible(true);

        display = location.getWorld().spawn(location.clone(), BlockDisplay.class);
        display.setBlock(Material.BLUE_CONCRETE.createBlockData());
        display.setBrightness(new Display.Brightness(14, 14));

        blockArmorStand.addPassenger(display);
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
        if (display != null) display.remove();
    }
}
