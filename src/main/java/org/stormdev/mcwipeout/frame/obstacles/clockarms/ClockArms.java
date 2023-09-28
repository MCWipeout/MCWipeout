package org.stormdev.mcwipeout.frame.obstacles.clockarms;
/*
  Created by Stormbits at 9/21/2023
*/

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.utils.helpers.WLocation;

import java.util.ArrayList;
import java.util.List;

public class ClockArms extends Obstacle {

    private ItemDisplay itemDisplay1;
    private ItemDisplay itemDisplay2;

    private WLocation center;

    private float startingAngle;
    private double radius;

    private List<ClockArmsPlatformObject> list;

    public ClockArms(WLocation center, double radius, float startingAngle) {
        this.center = center.toCenter();
        this.radius = radius;
        this.startingAngle = startingAngle;
    }

    @Override
    public void handle(Event event) {

    }

    @Override
    public void run() {

    }

    @Override
    public void reset() {
        itemDisplay1.remove();
        itemDisplay2.remove();

        itemDisplay1 = null;
        itemDisplay2 = null;

        for (ClockArmsPlatformObject fakePatternBlock : list) {
            fakePatternBlock.remove();
        }

        list.clear();
    }

    @Override
    public void enable() {
        list = new ArrayList<>();

        list.add(new ClockArmsPlatformObject(center, 0.9f, 0.9f, false, radius));
        list.add(new ClockArmsPlatformObject(center, -0.9f, -0.9f, false, radius));
        list.add(new ClockArmsPlatformObject(center, -0.9f, 0, false, radius));
        list.add(new ClockArmsPlatformObject(center, 0.9f, 0, false, radius));
        list.add(new ClockArmsPlatformObject(center, 0F, 0.9f, false, radius));
        list.add(new ClockArmsPlatformObject(center, 0F, -0.9f, false, radius));
        list.add(new ClockArmsPlatformObject(center, 0, 0, false, radius));

        list.add(new ClockArmsPlatformObject(center, 0.9f, 0.9f, true, radius));
        list.add(new ClockArmsPlatformObject(center, -0.9f, -0.9f, true, radius));
        list.add(new ClockArmsPlatformObject(center, -0.9f, 0, true, radius));
        list.add(new ClockArmsPlatformObject(center, 0.9f, 0, true, radius));
        list.add(new ClockArmsPlatformObject(center, 0F, 0.9f, true, radius));
        list.add(new ClockArmsPlatformObject(center, 0F, -0.9f, true, radius));
        list.add(new ClockArmsPlatformObject(center, 0, 0, true, radius));

        itemDisplay1 = Wipeout.get().getWorld().spawn(center.asLocation(), ItemDisplay.class);
        ItemStack head = new ItemStack(Material.GHAST_TEAR, 1);
        ItemMeta itemMeta = head.getItemMeta();
        itemMeta.setCustomModelData(10020);
        head.setItemMeta(itemMeta);

        itemDisplay1.setBrightness(new Display.Brightness(14, 14));
        itemDisplay1.setItemStack(head);

        Transformation transformation = itemDisplay1.getTransformation();
        transformation.getTranslation().set(0, 3, 0);
        transformation.getScale().set(8.5f, 8.5f, 8.5f);
        itemDisplay1.setTransformation(transformation);

        itemDisplay2 = Wipeout.get().getWorld().spawn(center.asLocation(), ItemDisplay.class);

        Location location = center.toCenter().asLocation();
        location.setPitch(0);
        location.setYaw(-180);
        itemDisplay2.teleport(location);

        itemDisplay2.setBrightness(new Display.Brightness(14, 14));
        itemDisplay2.setItemStack(head);
        itemDisplay2.setTransformation(transformation);

        new BukkitRunnable() {

            private float angle = startingAngle;

            @Override
            public void run() {
                if (itemDisplay1 == null || itemDisplay1.isDead() || itemDisplay2 == null || itemDisplay2.isDead())
                    return;

                if (angle == startingAngle) {
                    transformation(itemDisplay1);
                    transformation(itemDisplay2);
                }

                if (angle < 360) {
                    angle += 2;
                } else {
                    angle = 0;
                }

                if (angle > 356) {
                    for (ClockArmsPlatformObject fakePatternBlock : list) {
                        fakePatternBlock.teleport((float) (center.getX()), (float) (center.getZ()), 4 - (360 - angle));
                    }
                } else {
                    for (ClockArmsPlatformObject fakePatternBlock : list) {
                        fakePatternBlock.teleport((float) (center.getX()), (float) (center.getZ()), 4 + angle);
                    }
                }
            }
        }.runTaskTimer(Wipeout.get(), 20L, 0L);
    }

    @Override
    public void load() {
    }

    public void transformation(Display entity) {
        AxisAngle4f axisAngleRotMat = new AxisAngle4f((float) Math.PI, new Vector3f(0, 1, 0));


        Transformation transformation = new Transformation(
                new Vector3f(0, 3, 0),
                axisAngleRotMat,
                new Vector3f(8.5f, 8.5f, 8.5f),
                axisAngleRotMat
        );

        entity.setTransformation(transformation);
        entity.setInterpolationDuration(180);
        entity.setInterpolationDelay(-1);
    }

    public void sendTeleportPacket() {

    }
}
