package org.stormdev.mcwipeout.frame.obstacles.platforms.helpers;
/*
  Created by Stormbits at 9/11/2023
*/

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.FakeBlock;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.JsonPlatformSection;
import org.stormdev.mcwipeout.utils.WLocation;
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

    @Getter
    private List<FakeBlock> fakeBlocks;

    public MoveableSection(JsonPlatformSection jsonPlatformSection) {
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

    public void load() {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(entry.getValue());

            if (!entry.getKey().asLocation().getChunk().isLoaded()) {
                entry.getKey().asLocation().getChunk().load();
            }

            if (entry.getKey().getY() >= highestY) {
                fakeBlocks.add(new FakeBlock(entry.getKey().toCenter().asLocation(), entry.getValue(), true));
            }
        }
    }

    public void moveTo() {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(Material.AIR);

            entry.getKey().asLocation().getWorld().spawnParticle(Particle.CLOUD, entry.getKey().asLocation().add(0, 0.5, 0), 1, 0.1, 0.1, 0.1, 0);
        }

        for (FakeBlock fakeBlock : fakeBlocks) {
            fakeBlock.moveTo(xTranslation, 0, zTranslation, jsonSection.getSettings().getInterval(), true);
        }
    }

    public void reset() {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(Material.AIR);

            entry.getKey().asLocation().getWorld().spawnParticle(Particle.CLOUD, entry.getKey().asLocation().add(0, 0.5, 0), 1, 0.1, 0.1, 0.1, 0);
        }
    }
}
