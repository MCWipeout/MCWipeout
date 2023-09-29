package org.stormdev.mcwipeout.frame.obstacles.patterns;
/*
  Created by Stormbits at 9/13/2023
*/

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.JsonPlatformSection;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PatternPlatforms extends Obstacle {

    private List<PatternHolder> patternPresetList;

    private List<PatternSection> allPatterns;

    private int totalDuration, runTime, downTime;

    private List<Material> materials;

    @SneakyThrows
    public PatternPlatforms(int runTime, int downTime, int totalDuration, List<Material> materials, String allPatternsFile, String... fileIds) {
        this.totalDuration = totalDuration;
        this.runTime = runTime;
        this.downTime = downTime;
        this.patternPresetList = new ArrayList<>();
        this.allPatterns = new ArrayList<>();
        this.materials = materials;

        File allPatternsFileF = new File(Wipeout.get().getDataFolder() + "/exported/", allPatternsFile + ".json");
        if (!allPatternsFileF.exists()) {
            return;
        }

        JsonPlatformSection[] jsonPlatformSections = Wipeout.getGson().fromJson(new FileReader(allPatternsFileF), JsonPlatformSection[].class);

        for (JsonPlatformSection fromList : jsonPlatformSections) {
            allPatterns.add(new PatternSection(fromList));
        }


        for (String fileId : fileIds) {
            List<PatternSection> sectionList = new ArrayList<>();
            File file = new File(Wipeout.get().getDataFolder() + "/exported/", fileId + ".json");

            if (!file.exists()) {
                return;
            }

            JsonPlatformSection[] genericLocationSet = Wipeout.getGson().fromJson(new FileReader(file), JsonPlatformSection[].class);

            for (JsonPlatformSection fromList : genericLocationSet) {
                sectionList.add(new PatternSection(fromList));
            }

            sectionList.forEach(patternSection -> patternSection.getJsonSection().getMap().keySet().forEach((wLocation) -> wLocation.asLocation().getChunk().addPluginChunkTicket(Wipeout.get())));

            patternPresetList.add(new PatternHolder(sectionList));
        }

        this.totalDuration = totalDuration * patternPresetList.size();
    }


    @Override
    public void handle(Event event) {
        if (event instanceof EntityChangeBlockEvent e) {
            if (e.getEntity() instanceof FallingBlock) {
                e.setCancelled(true);
            }
        }
    }

    @Override
    public void run() {
        new BukkitRunnable() {

            int timer = 0;
            int downTimeTimer = 0;
            int runTimeTimer = 0;
            int selection = 0;

            Material currentMaterial;
            PatternHolder selected = null;

            @Override
            public void run() {

                if (!isEnabled()) {
                    this.cancel();
                    return;
                }

                if (timer <= totalDuration) {

                    if (downTimeTimer < downTime) {
                        downTimeTimer++;
                    } else {

                        if (selected == null) {
                            selected = patternPresetList.get(selection);
                            selected.load(materials.get(selection));
                            selected.move();

                            currentMaterial = materials.get(selection);

                            runTimeTimer = 0;

                            if (selection < patternPresetList.size() - 1) {
                                selection++;
                            } else {
                                selection = 0;
                            }
                        } else {
                            if (timer % selected.getDelay() == 0) {
                                if (runTimeTimer < runTime / 2) {
                                    selected.move();
                                }
                                //TODO: TEST
                            }

                            if (runTimeTimer < runTime) {
                                runTimeTimer++;
                            } else {
                                downTimeTimer = 0;
                                selected.remove();
                                selected = null;
                            }
                        }
                    }

                    for (PatternSection patternSection : allPatterns) {
                        if (patternSection.getFakePatternBlocks().isEmpty()) {
                            patternSection.loadWithoutShulker();
                        } else {
                            patternSection.checkRemove();
                        }
                        patternSection.changeType(currentMaterial == null ? materials.get(0) : currentMaterial);
                    }

                    timer++;
                } else {
                    timer = 0;
                }
            }
        }.runTaskTimer(Wipeout.get(), 100L, 0L);
    }

    @Override
    public void reset() {
        patternPresetList.forEach(moveableSection -> {
            moveableSection.getSectionList().forEach(patternSection -> {
                patternSection.getJsonSection().getMap().forEach((wLocation, material) -> wLocation.asBlock().setType(material));
                patternSection.getFakePatternBlocks().forEach(FakePatternBlock::remove);
                patternSection.getFakePatternBlocks().clear();
            });
        });

        for (PatternSection allPattern : allPatterns) {
            allPattern.getJsonSection().getMap().forEach((wLocation, material) -> wLocation.asBlock().setType(material));
            allPattern.delete();
        }
    }

    @Override
    public void enable() {
        patternPresetList.forEach(patternHolder -> patternHolder.getSectionList().forEach(patternSection -> patternSection.getJsonSection().getMap().forEach((wLocation, material) -> wLocation.asLocation().getChunk().addPluginChunkTicket(Wipeout.get()))));
    }

    @Override
    public void load() {

    }
}
