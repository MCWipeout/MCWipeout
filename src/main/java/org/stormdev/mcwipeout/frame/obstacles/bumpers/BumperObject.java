package org.stormdev.mcwipeout.frame.obstacles.bumpers;
/*
  Created by Stormbits at 9/8/2023
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.utils.WLocation;
import org.stormdev.utils.SyncScheduler;

@AllArgsConstructor(staticName = "of")
@Data
public class BumperObject {

    private final int delayInCycle;
    private final WLocation blockLocation;
    private final WLocation entityLocation;

    private final float yawRotation;

    private int xTranslation;
    private int zTranslation;

    private ItemDisplay displayEntity = null;

    public void move() {
        if (displayEntity == null || displayEntity.isDead()) return;

        blockLocation.asBlock().setType(Material.REDSTONE_BLOCK);

        displayEntity.setInterpolationDelay(0);
        displayEntity.setInterpolationDuration(2);
        Transformation transformation = displayEntity.getTransformation();
        transformation.getTranslation().set(xTranslation, 0, zTranslation);

        displayEntity.setTransformation(transformation);

        SyncScheduler.get().runLater(() -> {

            if (displayEntity == null || displayEntity.isDead()) return;

            displayEntity.setInterpolationDuration(2);
            displayEntity.setInterpolationDelay(0);
            Transformation newTransformation = displayEntity.getTransformation();
            newTransformation.getTranslation().set(0, 0, 0);

            displayEntity.setTransformation(newTransformation);

            blockLocation.asBlock().setType(Material.AIR);

        }, 10L);
    }

    public void setupDisplayEntity() {
        displayEntity = (ItemDisplay) Wipeout.get().getWorld().spawnEntity(getEntityLocation().toCenter().asLocation(), EntityType.ITEM_DISPLAY);
        Location location = getEntityLocation().asLocation();
        location.setPitch(0);
        location.setYaw(yawRotation);
        location.add(-xTranslation, 0, -zTranslation);
        displayEntity.teleport(location);

        displayEntity.setBrightness(new Display.Brightness(14, 14));

        ItemStack head = new ItemStack(Material.GHAST_TEAR, 1);
        ItemMeta itemMeta = head.getItemMeta();
        itemMeta.setCustomModelData(10000);
        head.setItemMeta(itemMeta);

        if (yawRotation < 0) {
            xTranslation = -xTranslation;
            zTranslation = -zTranslation;
        }
        if (yawRotation == 90) {
            zTranslation = -xTranslation;
            xTranslation = 0;
        }
        if (yawRotation == 180) {
            xTranslation = -zTranslation;
            zTranslation = 0;
        }

        displayEntity.setItemStack(head);
    }
}
