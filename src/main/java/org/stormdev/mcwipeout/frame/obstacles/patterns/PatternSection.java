package org.stormdev.mcwipeout.frame.obstacles.patterns;
/*
  Created by Stormbits at 9/11/2023
*/

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.stormdev.mcwipeout.frame.obstacles.FakeBlock;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.JsonPlatformSection;
import org.stormdev.mcwipeout.utils.WLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class PatternSection {

    @Getter
    private JsonPlatformSection jsonSection;

    private float xTranslation;
    private float zTranslation;

    private int highestY;

    @Getter
    private List<FakeBlock> fakeBlocks;

    public PatternSection(JsonPlatformSection jsonPlatformSection) {
        this.jsonSection = jsonPlatformSection;

        this.xTranslation = jsonSection.getSettings().getXOffset();
        this.zTranslation = jsonSection.getSettings().getZOffset();

        this.fakeBlocks = new ArrayList<>();

        highestY = 0;

        jsonSection.getMap().keySet().forEach(loc -> {
            if (loc.getY() > highestY) {
                highestY = (int) loc.getY();
            }
        });
    }

    public void load(Material material) {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {

            if (!entry.getKey().asLocation().getChunk().isLoaded()) {
                entry.getKey().asLocation().getChunk().load();
            }

            if (entry.getKey().getY() >= highestY) {
                fakeBlocks.add(new FakeBlock(entry.getKey().toCenter().asLocation(), material == null ? entry.getValue() : material, true));
            }
        }
    }

    public void checkRemove() {
        World world = jsonSection.getMap().keySet().stream().findFirst().get().asLocation().getWorld();

        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(Material.AIR);

            if (!entry.getKey().asLocation().getChunk().isLoaded()) {
                entry.getKey().asLocation().getChunk().load();
            }

            if (entry.getKey().getY() >= highestY) {
                Collection<Entity> collection = world.getNearbyEntities(entry.getKey().asLocation(), 1, 1, 1);
                collection.forEach(entity -> {
                    if (entity.getType().name().contains("SHULKER")) {
                        this.delete();
                    }
                });
            }
        }
    }

    public void loadWithoutShulker() {
        World world = jsonSection.getMap().keySet().stream().findFirst().get().asLocation().getWorld();

        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(Material.AIR);

            if (!entry.getKey().asLocation().getChunk().isLoaded()) {
                entry.getKey().asLocation().getChunk().load();
            }

            if (entry.getKey().getY() >= highestY) {
                Collection<Entity> collection = world.getNearbyEntities(entry.getKey().asLocation(), 1, 1, 1);
                AtomicBoolean enable = new AtomicBoolean(true);
                collection.forEach(entity -> {
                    if (entity.getType().name().contains("SHULKER")) {
                        enable.set(false);
                    }
                });

                if (enable.get()) {
                    fakeBlocks.add(new FakeBlock(entry.getKey().toCenter().asLocation(), entry.getValue(), false));
                }
            }
        }
    }


    public void moveTo() {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asLocation().getWorld().spawnParticle(Particle.CLOUD, entry.getKey().asLocation().add(0, 0.5, 0), 1, 0.1, 0.1, 0.1, 0);
        }

        for (FakeBlock fakeBlock : fakeBlocks) {
            fakeBlock.moveTo(xTranslation, 0, zTranslation, jsonSection.getSettings().getInterval(), true);
        }
    }

    public void delete() {
        for (FakeBlock fakeBlock : fakeBlocks) {
            fakeBlock.remove();
        }

        fakeBlocks.clear();
    }

    public void changeType(Material material) {
        for (FakeBlock fakeBlock : fakeBlocks) {
            if (fakeBlock.getDisplayEntity() == null) continue;
            fakeBlock.getDisplayEntity().setBlock(material.createBlockData());
        }
    }
}
