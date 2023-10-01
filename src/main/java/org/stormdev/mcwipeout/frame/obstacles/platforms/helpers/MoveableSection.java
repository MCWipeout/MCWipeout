package org.stormdev.mcwipeout.frame.obstacles.platforms.helpers;
/*
  Created by Stormbits at 9/11/2023
*/

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.patterns.FakePatternBlock;
import org.stormdev.mcwipeout.utils.helpers.LocationUtil;
import org.stormdev.mcwipeout.utils.helpers.WLocation;
import org.stormdev.utils.SyncScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoveableSection {

    @Getter
    private JsonPlatformSection jsonSection;

    private float xTranslation;
    private float zTranslation;

    private int highestY;

    private boolean flip;

    @Getter
    private List<FakePatternBlock> fakePatternBlocks;

    @Getter
    private ItemDisplay displayEntity;

    public MoveableSection(JsonPlatformSection jsonPlatformSection) {
        this.jsonSection = jsonPlatformSection;

        this.xTranslation = jsonSection.getSettings().getXOffset();
        this.zTranslation = jsonSection.getSettings().getZOffset();

        this.fakePatternBlocks = new ArrayList<>();

        highestY = 0;

        this.flip = false;

        jsonSection.getMap().keySet().forEach(loc -> {
            if (loc.getY() > highestY) {
                highestY = (int) loc.getY();
            }
        });

        jsonSection.getMap().keySet().forEach(wLocation -> wLocation.asBlock().setType(Material.AIR));
    }

    public void load() {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(entry.getValue());

            if (!entry.getKey().asLocation().getChunk().isLoaded()) {
                entry.getKey().asLocation().getChunk().load();
            }

            if (entry.getKey().getY() >= highestY) {
                fakePatternBlocks.add(new FakePatternBlock(entry.getKey().toCenter().asLocation(), entry.getValue(), true, false));
            }
        }
        Location center = LocationUtil.getCenterBlock(jsonSection.getMap().keySet().stream().toList()).add(0.5, 0, 0.5);

        center.setY(highestY + 1.2);

        displayEntity = (ItemDisplay) Wipeout.get().getWorld().spawnEntity(center, EntityType.ITEM_DISPLAY);

        displayEntity.setCustomNameVisible(false);
        displayEntity.setCustomName("wipeout-entity");

        displayEntity.setBrightness(new Display.Brightness(14, 14));

        ItemStack head = new ItemStack(Material.GHAST_TEAR, 1);
        ItemMeta itemMeta = head.getItemMeta();
        itemMeta.setCustomModelData(10013);
        head.setItemMeta(itemMeta);

        displayEntity.setItemStack(head);

        displayEntity.teleport(center);
    }

    public void moveTo() {
        moveDisplay();

        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(Material.AIR);

            entry.getKey().asLocation().getWorld().spawnParticle(Particle.CLOUD, entry.getKey().asLocation().add(0, 0.5, 0), 1, 0.1, 0.1, 0.1, 0);
        }

        for (FakePatternBlock fakePatternBlock : fakePatternBlocks) {
            if (flip) {
                fakePatternBlock.moveTo(-xTranslation, 0, -zTranslation, jsonSection.getSettings().getInterval(), jsonSection.getSettings().isMirror());
            } else {
                fakePatternBlock.moveTo(xTranslation, 0, zTranslation, jsonSection.getSettings().getInterval(), jsonSection.getSettings().isMirror());
            }
        }
        flip = !flip;
    }

    public void moveDisplay() {
        displayEntity.setInterpolationDelay(0);
        displayEntity.setInterpolationDuration(jsonSection.getSettings().getInterval() / 2);
        Transformation transformation = displayEntity.getTransformation();
        if (flip) {
            transformation.getTranslation().set(-xTranslation, 0, -zTranslation);
        } else {
            transformation.getTranslation().set(xTranslation, 0, zTranslation);
        }

        displayEntity.setTransformation(transformation);

        SyncScheduler.get().runLater(() -> {
            if (displayEntity == null || displayEntity.isDead()) return;

            displayEntity.setInterpolationDuration(jsonSection.getSettings().getInterval() / 2);
            displayEntity.setInterpolationDelay(0);
            Transformation newTransformation = displayEntity.getTransformation();
            newTransformation.getTranslation().set(0, 0, 0);

            displayEntity.setTransformation(newTransformation);
        }, jsonSection.getSettings().getInterval() / 2);
    }

    public void reset() {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(Material.AIR);

            entry.getKey().asLocation().getWorld().spawnParticle(Particle.CLOUD, entry.getKey().asLocation().add(0, 0.5, 0), 1, 0.1, 0.1, 0.1, 0);
        }
    }
}
