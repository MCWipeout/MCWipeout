package org.stormdev.mcwipeout.frame.obstacles;
/*
  Created by Stormbits at 10/10/2023
*/

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.stormdev.mcwipeout.frame.obstacles.patterns.PatternPlatforms;

public enum ObstacleRegion {

    RED_BALLS("Red Balls &f\uE01C", "\uE01C", "\uE01D"),
    BUMPER_WALL("Bumper Wall &f\uE012", "\uE012", "\uE013"),
    DISAPPEARING_PLATFORM("Disappearing Platforms &f\uE014", "\uE014", "\uE015"),
    FANS("Fans &f\uE016", "\uE016", "\uE017"),
    ICE("Disappearing Ice &f\uE018", "\uE018", "\uE019"),
    MOVING_PLATFORM("Moving Platforms &f\uE01A", "\uE01A", "\uE01B"),
    SPIRAL_BUMPER("Bumper Spiral &f\uE01E", "\uE01E", "\uE01F"),
    WATER_STAIRS("Water Stairs &f\uE020", "\uE020", "\uE021"),
    PATTERN_MEMORIZATION("Pattern Memorization &f\uE023", "\uE023", "\uE024"),
    FINISH("None", "", "");

    @Getter
    private String text;

    @Getter
    private String colorIcon, grayIcon;

    ObstacleRegion(String text, String colorIcon, String grayIcon) {
        this.text = text;
        this.colorIcon = colorIcon;
        this.grayIcon = grayIcon;
    }
}