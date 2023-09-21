package org.stormdev.mcwipeout.frame.obstacles.patterns;
/*
  Created by Stormbits at 9/11/2023
*/

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
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
    private List<FakePatternBlock> fakePatternBlocks;

    public PatternSection(JsonPlatformSection jsonPlatformSection) {
        this.jsonSection = jsonPlatformSection;

        this.xTranslation = jsonSection.getSettings().getXOffset();
        this.zTranslation = jsonSection.getSettings().getZOffset();

        this.fakePatternBlocks = new ArrayList<>();

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
                fakePatternBlocks.add(new FakePatternBlock(entry.getKey().toCenter().asLocation(), material == null ? entry.getValue() : material, true));
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
                Collection<Entity> collection = world.getNearbyEntities(entry.getKey().asLocation(), 2, 1, 2);
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
                Collection<Entity> collection = world.getNearbyEntities(entry.getKey().asLocation(), 2, 2, 2);
                AtomicBoolean enable = new AtomicBoolean(true);
                collection.forEach(entity -> {
                    if (entity.getType().name().contains("SHULKER")) {
                        enable.set(false);
                    }
                });

                if (enable.get()) {
                    fakePatternBlocks.add(new FakePatternBlock(entry.getKey().toCenter().asLocation(), entry.getValue(), false));
                }
            }
        }
    }


    public void moveTo() {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asLocation().getWorld().spawnParticle(Particle.CLOUD, entry.getKey().asLocation().add(0, 0.5, 0), 1, 0.1, 0.1, 0.1, 0);
        }

        for (FakePatternBlock fakePatternBlock : fakePatternBlocks) {
            fakePatternBlock.moveTo(xTranslation, 0, zTranslation, jsonSection.getSettings().getInterval(), true);
        }
    }

    public void delete() {
        for (FakePatternBlock fakePatternBlock : fakePatternBlocks) {
            fakePatternBlock.remove();
        }

        fakePatternBlocks.clear();
    }

    public void changeType(Material material) {
        for (FakePatternBlock fakePatternBlock : fakePatternBlocks) {
            if (fakePatternBlock.getDisplayEntity() == null) continue;
            fakePatternBlock.getDisplayEntity().setBlock(material.createBlockData());
        }
    }
}
