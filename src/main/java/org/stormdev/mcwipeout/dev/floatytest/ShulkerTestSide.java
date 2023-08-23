package org.stormdev.mcwipeout.dev.floatytest;
/*
  Created by Stormbits at 8/21/2023
*/

import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;

import java.util.ArrayList;
import java.util.List;

public class ShulkerTestSide {

    private static List<Shulker> stands;

    private static List<ArmorStand> riders;

    public void setup() {
        stands = new ArrayList<>();
        riders = new ArrayList<>();

        World world = Bukkit.getWorld("maps");
        List<Location> locations = new ArrayList<>();

        locations.add(new Location(world, 1104, 109, -181));
        locations.add(new Location(world, 1104, 109, -182));
        locations.add(new Location(world, 1104, 109, -183));

        locations.add(new Location(world, 1103, 109, -181));
        locations.add(new Location(world, 1103, 109, -182));
        locations.add(new Location(world, 1103, 109, -183));

        locations.add(new Location(world, 1102, 109, -181));
        locations.add(new Location(world, 1102, 109, -182));
        locations.add(new Location(world, 1102, 109, -183));

        for (Location location : locations) {
            ArmorStand rider = world.spawn(location, ArmorStand.class);
            rider.setGravity(false);

            Shulker shulker = world.spawn(location, Shulker.class);
            shulker.setAI(false);

            rider.setHelmet(new ItemStack(Material.STONE));

            shulker.setInvulnerable(true);

            shulker.setSilent(true);

            stands.add(shulker);
            riders.add(rider);

            rider.teleport(location);
            rider.addPassenger(shulker);
        }

        new BukkitRunnable() {

            int timer = 0;
            int elapsed = 0;

            @Override
            public void run() {
                if (timer < 400) {
                    timer++;
                } else {
                    disable();
                    cancel();
                }

                elapsed++;

                if (elapsed < 10) {
                    for (ArmorStand rider : riders) {
                        CraftArmorStand craftArmorStand = (CraftArmorStand) rider;
                        net.minecraft.world.entity.decoration.ArmorStand armorStand = craftArmorStand.getHandle();

                        rider.getLocation().setX(armorStand.getX() + 0.1);
                        armorStand.setPos(armorStand.getX() + 0.1, armorStand.getY(), armorStand.getZ());

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            CraftPlayer craftPlayer = (CraftPlayer) player;
                            craftPlayer.getHandle().connection.send(new ClientboundTeleportEntityPacket(armorStand));
                        }
                    }
                } else if (elapsed > 30 && elapsed < 40) {
                    for (ArmorStand rider : riders) {
                        CraftArmorStand craftArmorStand = (CraftArmorStand) rider;
                        net.minecraft.world.entity.decoration.ArmorStand armorStand = craftArmorStand.getHandle();
                        rider.getLocation().setX(armorStand.getX() - 0.1);

                        armorStand.setPos(armorStand.getX() - 0.1, armorStand.getY(), armorStand.getZ());


                        for (Player player : Bukkit.getOnlinePlayers()) {
                            CraftPlayer craftPlayer = (CraftPlayer) player;
                            craftPlayer.getHandle().connection.send(new ClientboundTeleportEntityPacket(armorStand));
                        }
                    }
                } else if (elapsed == 50) {
                    elapsed = 0;
                }
            }
        }.runTaskTimer(Wipeout.get(), 20L, 0L);
    }

    public static void disable() {
        for (ArmorStand rider : riders) {
            rider.remove();
        }
        for (Shulker stand : stands) {
            stand.remove();
        }
    }
}
