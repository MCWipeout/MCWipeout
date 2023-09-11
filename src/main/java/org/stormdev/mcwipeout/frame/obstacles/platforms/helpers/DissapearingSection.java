package org.stormdev.mcwipeout.frame.obstacles.platforms.helpers;
/*
  Created by Stormbits at 9/12/2023
*/

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.stormdev.mcwipeout.utils.WLocation;

import java.util.Map;

public class DissapearingSection {

    @Getter
    private JsonPlatformSection jsonSection;

    public DissapearingSection(JsonPlatformSection jsonSection) {
        this.jsonSection = jsonSection;

        show();
    }

    public void hide() {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(Material.AIR);
            entry.getKey().asLocation().getWorld().spawnParticle(Particle.CLOUD, entry.getKey().asLocation().add(0, 0.5, 0), 1, 0.1, 0.1, 0.1, 0);
        }
    }

    public void show() {
        for (Map.Entry<WLocation, Material> entry : jsonSection.getMap().entrySet()) {
            entry.getKey().asBlock().setType(entry.getValue());
        }
    }
}
