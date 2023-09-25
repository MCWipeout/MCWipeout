package org.stormdev.mcwipeout.frame.obstacles.bumpers;
/*
  Created by Stormbits at 9/8/2023
*/

import lombok.Data;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor(staticName = "of")
@Data
public class BumperObject {

    private final int delayInCycle;
    private final WLocation blockLocation;
    private final WLocation entityLocation;

    private final float yawRotation;

    private final int xTranslation;
    private final int zTranslation;

    private int xMove;
    private int zMove;

    private ItemDisplay displayEntity = null;

    public void move() {
        if (displayEntity == null || displayEntity.isDead()) return;

        blockLocation.asBlock().setType(Material.REDSTONE_BLOCK);

        displayEntity.setInterpolationDelay(0);
        displayEntity.setInterpolationDuration(2);
        Transformation transformation = displayEntity.getTransformation();
        transformation.getTranslation().set(xMove, 0, zMove);

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
        if (!getEntityLocation().asLocation().getChunk().isLoaded()) {
            getEntityLocation().asLocation().getChunk().load();
        }

        double dx = 0;
        double dz = 0;

        if (xTranslation < 0) {
            dx = xTranslation + 0.2;
        }
        if (zTranslation < 0) {
            dz = zTranslation + 0.2;
        }

        if (xTranslation > 0) {
            dx = xTranslation - 0.2;
        }
        if (zTranslation > 0) {
            dz = zTranslation - 0.2;
        }

        displayEntity = (ItemDisplay) Wipeout.get().getWorld().spawnEntity(getEntityLocation().toCenter().asLocation(), EntityType.ITEM_DISPLAY);

        Location location = getEntityLocation().toCenter().asLocation();
        location.setPitch(0);
        location.setYaw(yawRotation);
        location.add(-dx, 0, -dz);
        displayEntity.teleport(location);

        displayEntity.setCustomNameVisible(false);
        displayEntity.setCustomName("wipeout-entity");

        displayEntity.setBrightness(new Display.Brightness(14, 14));

        ItemStack head = new ItemStack(Material.GHAST_TEAR, 1);
        ItemMeta itemMeta = head.getItemMeta();
        itemMeta.setCustomModelData(10000);
        head.setItemMeta(itemMeta);

        if (yawRotation == -90) {
            xMove = zTranslation;
            zMove = xTranslation;
        } else if (yawRotation == 90) {
            zMove = -xTranslation;
            xMove = -zTranslation;
        } else if (yawRotation == 180) {
            xMove = -zTranslation;
            zMove = -xTranslation;
        } else if (yawRotation == 0) {
            xMove = xTranslation;
            zMove = zTranslation;
        } else if (yawRotation == -180) {
            xMove = -xTranslation;
            zMove = -zTranslation;
        }

        displayEntity.setItemStack(head);
    }
}
