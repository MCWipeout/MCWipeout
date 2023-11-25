package org.stormdev.mcwipeout.frame.obstacles.sweeper;
/*
  Created by Stormbits at 10/1/2023
*/

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.utils.helpers.WLocation;
import org.stormdev.mcwipeout.utils.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class SweeperObstacle extends Obstacle {

    private WLocation center;

    private List<SweeperBlockObject> blockObjectList;

    private ItemDisplay itemDisplay1;
    private ItemDisplay itemDisplay2;

    public SweeperObstacle(WLocation wLocation) {
        this.blockObjectList = new ArrayList<>();

        center = wLocation;
    }

    @Override
    public void handle(Event event) {

    }

    @Override
    public void run() {
        new BukkitRunnable() {

            private float angle = 0;

            private float displayAngle = 59;

            @Override
            public void run() {
                if (!isEnabled()) {
                    this.cancel();
                    return;
                }

                if (displayAngle == 60) {
                    transformation(itemDisplay1, 120);
                    transformation(itemDisplay2, 120);
                }
                if (displayAngle == 120) {
                    transformation(itemDisplay1, 240);
                    transformation(itemDisplay2, 240);
                }

                if (displayAngle == 180) {
                    transformation(itemDisplay1, 360);
                    transformation(itemDisplay2, 360);
                }

                if (displayAngle < 180) {
                    displayAngle++;
                } else if (displayAngle == 180) {
                    displayAngle = 0;
                }

                if (angle < 360) {
                    angle += 2F;
                } else {
                    angle = 0;
                }

                if (angle > 356) {
                    for (SweeperBlockObject fakePatternBlock : blockObjectList) {
                        fakePatternBlock.teleport((float) (center.getX()), (float) (center.getZ()), 4 - (360 - angle));

                        fakePatternBlock.getFakePatternBlock().getShulkerArmorStand().getLocation().getWorld().getNearbyEntities(fakePatternBlock.getFakePatternBlock().getShulker().getLocation(), 1, 0.2, 1).forEach(entity -> {

                            Location loc1 = fakePatternBlock.getFakePatternBlock().getShulker().getLocation();

                            double radians = 0;

                            if (fakePatternBlock.isInverse()) {
                                radians = MathUtils.getFixedYaw(angle + 180 + 4) + 4;
                            } else {
                                radians = angle + 4;
                            }

                            radians = Math.toRadians(radians);

                            double xNew = (fakePatternBlock.getRadius() * Math.sin(radians));
                            double zNew = (fakePatternBlock.getRadius() * Math.cos(radians));

                            Vector newVelocity = loc1.toVector().normalize().crossProduct(new Vector(xNew, 0, zNew).normalize());

                            if (entity instanceof Player player) {

                                if (!player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                                    Wipeout.get().getAdventure().player(player).playSound(Sound.sound(Key.key("wipeout:mcw.sfx.fans"), Sound.Source.MASTER, 1.0f, 1.0f));
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20, 1));
                                    player.setVelocity(newVelocity.multiply(50).setY(0.5));
                                    Bukkit.broadcastMessage("Velocity: " + newVelocity);
                                }
                            }
                        });
                    }

                } else {
                    for (SweeperBlockObject fakePatternBlock : blockObjectList) {
                        fakePatternBlock.teleport((float) (center.getX()), (float) (center.getZ()), 4 + angle);

                        fakePatternBlock.getFakePatternBlock().getShulkerArmorStand().getLocation().getWorld().getNearbyEntities(fakePatternBlock.getFakePatternBlock().getShulker().getLocation(), 1.5, 1.5, 1.5).forEach(entity -> {

                            Location loc1 = fakePatternBlock.getFakePatternBlock().getShulker().getLocation();

                            double radians = 0;

                            if (fakePatternBlock.isInverse()) {
                                radians = MathUtils.getFixedYaw(angle + 180 + 4) + 4;
                            } else {
                                radians = angle + 4;
                            }

                            radians = Math.toRadians(radians);

                            double xNew = (fakePatternBlock.getRadius() * Math.sin(radians));
                            double zNew = (fakePatternBlock.getRadius() * Math.cos(radians));

                            Vector newVelocity = loc1.toVector().normalize().crossProduct(new Vector(xNew, 0, zNew).normalize());

                            if (entity instanceof Player player) {

                                if (!player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                                    Wipeout.get().getAdventure().player(player).playSound(Sound.sound(Key.key("wipeout:mcw.sfx.fans"), Sound.Source.MASTER, 1.0f, 1.0f));
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20, 1));
                                    player.setVelocity(newVelocity.multiply(50).setY(0.5));
                                }
                            }
                        });
                    }
                }
            }
        }.runTaskTimer(Wipeout.get(), 20L, 0L);
    }

    @Override
    public void reset() {
        for (SweeperBlockObject fakePatternBlock : blockObjectList) {
            fakePatternBlock.remove();
        }

        blockObjectList.clear();

        if (itemDisplay1 != null)
            itemDisplay1.remove();
        if (itemDisplay2 != null)
            itemDisplay2.remove();

        itemDisplay1 = null;
        itemDisplay2 = null;
    }

    @Override
    public void enable() {
        blockObjectList.clear();

        blockObjectList.add(new SweeperBlockObject(center, 0, 0, false, 9));
        blockObjectList.add(new SweeperBlockObject(center, 0, 0, false, 8));
        blockObjectList.add(new SweeperBlockObject(center, 0, 0, false, 7));
        blockObjectList.add(new SweeperBlockObject(center, 0, 0, false, 6));

        blockObjectList.add(new SweeperBlockObject(center, 0, 0, true, 6));
        blockObjectList.add(new SweeperBlockObject(center, 0, 0, true, 7));
        blockObjectList.add(new SweeperBlockObject(center, 0, 0, true, 8));
        blockObjectList.add(new SweeperBlockObject(center, 0, 0, true, 9));

        WLocation display = center.toCenter();
        display.setY(display.getY() + 4.5);

        itemDisplay1 = Wipeout.get().getWorld().spawn(display.asLocation(), ItemDisplay.class);
        ItemStack head = new ItemStack(Material.GHAST_TEAR, 1);
        ItemMeta itemMeta = head.getItemMeta();
        itemMeta.setCustomModelData(10027);
        head.setItemMeta(itemMeta);

        itemDisplay1.setBrightness(new Display.Brightness(14, 14));
        itemDisplay1.setItemStack(head);

        Transformation transformation = itemDisplay1.getTransformation();
        transformation.getTranslation().set(-0.5, 3, -0.5);
        transformation.getScale().set(15f, 15f, 15f);
        itemDisplay1.setTransformation(transformation);

        itemDisplay2 = Wipeout.get().getWorld().spawn(display.asLocation(), ItemDisplay.class);

        Location location = display.asLocation();
        location.setPitch(0);
        location.setYaw(-180F);
        itemDisplay2.teleport(location);

        itemDisplay2.setBrightness(new Display.Brightness(14, 14));
        itemDisplay2.setItemStack(head);

        Transformation transformation1 = itemDisplay2.getTransformation();
        transformation1.getTranslation().set(0.5, 3, 0.5);
        transformation1.getScale().set(15f, 15f, 15f);

        itemDisplay2.setTransformation(transformation1);
    }

    @Override
    public void load() {

    }

    public void transformation(Display entity, double angle) {
        AxisAngle4f axisAngleRotMat = new AxisAngle4f((float) Math.toRadians(angle), new Vector3f(0, 1, 0));

        Transformation transformation = entity.getTransformation();
        transformation.getRightRotation().set(axisAngleRotMat);

        entity.setTransformation(transformation);
        entity.setInterpolationDuration(60);
        entity.setInterpolationDelay(0);
    }
}
