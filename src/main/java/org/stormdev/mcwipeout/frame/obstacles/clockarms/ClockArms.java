package org.stormdev.mcwipeout.frame.obstacles.clockarms;
/*
  Created by Stormbits at 9/21/2023
*/

import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.utils.WLocation;

public class ClockArms extends Obstacle {

    private ItemDisplay itemDisplay;

    public ClockArms() {

    }

    @Override
    public void handle(Event event) {

    }

    @Override
    public void run() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void enable() {
        itemDisplay = Wipeout.get().getWorld().spawn(WLocation.from(2868, 209, 81).toCenter().asLocation(), ItemDisplay.class);
        ItemStack head = new ItemStack(Material.GHAST_TEAR, 1);
        ItemMeta itemMeta = head.getItemMeta();
        itemMeta.setCustomModelData(10020);
        head.setItemMeta(itemMeta);

        itemDisplay.setBrightness(new Display.Brightness(14, 14));

        itemDisplay.setItemStack(head);
        Transformation transformation = itemDisplay.getTransformation();
        transformation.getTranslation().set(-3.9, 0, 0);
        transformation.getScale().set(3.7f, 3.7f, 3.7f);
        itemDisplay.setTransformation(transformation);

        ArmorStand armorStand = Wipeout.get().getWorld().spawn(WLocation.from(2868, 209 - 1.5, 81).toCenter().asLocation(), ArmorStand.class);
        armorStand.addPassenger(itemDisplay);
        armorStand.setHelmet(new ItemStack(Material.STONE));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (itemDisplay == null || itemDisplay.isDead()) return;

                CraftArmorStand craftArmorStand = (CraftArmorStand) armorStand;
                net.minecraft.world.entity.decoration.ArmorStand armorStandHandle = craftArmorStand.getHandle();

                armorStandHandle.setYRot((float) (armorStandHandle.getYRot() + 0.5));


                for (Player player : Bukkit.getOnlinePlayers()) {
                    CraftPlayer craftPlayer = (CraftPlayer) player;
                    craftPlayer.getHandle().connection.send(new ClientboundTeleportEntityPacket(armorStandHandle));
                }
//                transformationLeft(itemDisplay, new Vector(0, 1, 0), (float) Math.toRadians(90));
            }
        }.runTaskTimer(Wipeout.get(), 20L, 0L);
    }

    @Override
    public void load() {
    }

    public void transformationLeft(Display entity, Vector axis, float angle) {
        entity.setInterpolationDuration(40);
        entity.setInterpolationDelay(-1);
        Transformation transformation = entity.getTransformation();
        transformation.getRightRotation().set(new AxisAngle4f(angle, (float) axis.getX(), (float) axis.getY(), (float) axis.getZ()));
        entity.setTransformation(transformation);
    }
}
