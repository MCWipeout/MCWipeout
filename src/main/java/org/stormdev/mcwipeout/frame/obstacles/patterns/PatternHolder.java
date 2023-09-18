package org.stormdev.mcwipeout.frame.obstacles.patterns;
/*
  Created by Stormbits at 9/17/2023
*/

import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PatternHolder {

    private List<PatternSection> sectionList;
    @Getter
    private int delay;

    public PatternHolder(List<PatternSection> sectionList) {
        this.sectionList = sectionList;
        this.delay = sectionList.get(0).getJsonSection().getSettings().getDelay();
    }

    public void move() {
        sectionList.forEach(PatternSection::moveTo);
    }

    public void load(Material material) {
        sectionList.forEach(section -> section.load(material));
    }

    public void remove() {
        sectionList.forEach(PatternSection::delete);
    }

    public void changeType(Material material) {
        sectionList.forEach(section -> section.changeType(material));
    }
}
