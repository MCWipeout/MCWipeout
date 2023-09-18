package org.stormdev.mcwipeout.frame.obstacles.platforms.helpers;
/*
  Created by Stormbits at 9/11/2023
*/

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.stormdev.mcwipeout.frame.obstacles.patterns.FakePatternBlock;
import org.stormdev.mcwipeout.utils.WLocation;

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
    private List<FakePatternBlock> fakePatternBlocks;

    public MoveableSection(JsonPlatformSection jsonPlatformSection) {
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

    public void load() {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(entry.getValue());

            if (!entry.getKey().asLocation().getChunk().isLoaded()) {
                entry.getKey().asLocation().getChunk().load();
            }

            if (entry.getKey().getY() >= highestY) {
                fakePatternBlocks.add(new FakePatternBlock(entry.getKey().toCenter().asLocation(), entry.getValue(), true));
            }
        }
    }

    public void moveTo() {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(Material.AIR);

            entry.getKey().asLocation().getWorld().spawnParticle(Particle.CLOUD, entry.getKey().asLocation().add(0, 0.5, 0), 1, 0.1, 0.1, 0.1, 0);
        }

        for (FakePatternBlock fakePatternBlock : fakePatternBlocks) {
            fakePatternBlock.moveTo(xTranslation, 0, zTranslation, jsonSection.getSettings().getInterval(), true);
        }
    }

    public void reset() {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(Material.AIR);

            entry.getKey().asLocation().getWorld().spawnParticle(Particle.CLOUD, entry.getKey().asLocation().add(0, 0.5, 0), 1, 0.1, 0.1, 0.1, 0);
        }
    }
}
