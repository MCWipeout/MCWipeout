package org.stormdev.mcwipeout.frame.obstacles.platforms.helpers;
/*
  Created by Stormbits at 9/11/2023
*/

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.stormdev.mcwipeout.frame.obstacles.patterns.FakePatternBlock;
import org.stormdev.mcwipeout.utils.helpers.WLocation;

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
                fakePatternBlocks.add(new FakePatternBlock(entry.getKey().toCenter().asLocation(), entry.getValue(), true, true));
            }
        }
    }

    public void moveTo() {
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

    public void reset() {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(Material.AIR);

            entry.getKey().asLocation().getWorld().spawnParticle(Particle.CLOUD, entry.getKey().asLocation().add(0, 0.5, 0), 1, 0.1, 0.1, 0.1, 0);
        }
    }
}
