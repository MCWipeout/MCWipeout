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
    BUMPER_WALL2("Bumper Wall &f\uE012", "\uE012", "\uE013"),
    DISAPPEARING_PLATFORM("Disappearing Platforms &f\uE014", "\uE014", "\uE015"),
    FANS("Fans &f\uE016", "\uE016", "\uE017"),
    FANS2("Fans &f\uE016", "\uE016", "\uE017"),
    ICE("Disappearing Ice &f\uE018", "\uE018", "\uE019"),
    MOVING_PLATFORMS("Moving Platforms &f\uE01A", "\uE01A", "\uE01B"),
    SPIRAL_BUMPER("Bumper Spiral &f\uE01E", "\uE01E", "\uE01F"),
    WATER_STAIRS("Water Stairs &f\uE020", "\uE020", "\uE021"),
    PATTERN_MEMORIZATION("Pattern Memorization &f\uE023", "\uE023", "\uE024"),
    CLOCK_ARMS("Clock Arms &f\uE025", "\uE025", "\uE026"),
    MAZE("Maze &f\uE02B", "\uE02B", "\uE02C"),
    SWEEPER("Sweeper &f\uE02D", "\uE02D", "\uE02E"),
    EXTENDED_BUMPER_WALL("Extended Bumper Wall &f\uE027", "\uE027", "\uE028"),
    SNAKE("Snake &f\uE02F", "\uE02F", "\uE030"),

    RED_BALLS2("Red Balls &f\uE01C", "\uE01C", "\uE01D"),
    FINISH("None", "", ""), SKIP("None", "", "");

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
