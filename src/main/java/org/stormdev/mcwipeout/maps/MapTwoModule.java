package org.stormdev.mcwipeout.maps;
/*
  Created by Stormbits at 8/21/2023
*/

import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.stormdev.mcwipeout.frame.game.CheckPoint;
import org.stormdev.mcwipeout.frame.game.Map;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.ObstacleRegion;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.BumperObject;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.BumperWall;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.extended.ExtendedBumperWall;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.extended.SlidingWall;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.extended.WallSize;
import org.stormdev.mcwipeout.frame.obstacles.clockarms.ClockArms;
import org.stormdev.mcwipeout.frame.obstacles.clockarms.ClockArmsHolder;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanObject;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanRotation;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanWall;
import org.stormdev.mcwipeout.frame.obstacles.ice.FreezingIce;
import org.stormdev.mcwipeout.frame.obstacles.patterns.PatternPlatforms;
import org.stormdev.mcwipeout.frame.obstacles.platforms.MovingPlatforms;
import org.stormdev.mcwipeout.frame.obstacles.redballs.RedBallsObstacle;
import org.stormdev.mcwipeout.frame.obstacles.snake.SnakeObstacle;
import org.stormdev.mcwipeout.frame.obstacles.water.WaterSlide;
import org.stormdev.mcwipeout.frame.team.WPoint;
import org.stormdev.mcwipeout.utils.helpers.Cuboid;
import org.stormdev.mcwipeout.utils.helpers.Direction;
import org.stormdev.mcwipeout.utils.helpers.WLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapTwoModule extends Map {

    public MapTwoModule() {
        super("Emerald Haven");

        setSpawnPoint(new CheckPoint("", new WPoint(2993.5, 228, 31.5), 90.0F, 0F, ObstacleRegion.RED_BALLS));
        setFinish(new CheckPoint("map_2_finish", new WPoint(2993.5, 228, 31.5), 90.0F, 0F, null));

        setObstacleRegions(List.of(
                ObstacleRegion.RED_BALLS,
                ObstacleRegion.WATER_STAIRS,
                ObstacleRegion.EXTENDED_BUMPER_WALL,
                ObstacleRegion.BUMPER_WALL,
                ObstacleRegion.CLOCK_ARMS,
                ObstacleRegion.FANS,
                ObstacleRegion.SNAKE,
                ObstacleRegion.PATTERN_MEMORIZATION,
                ObstacleRegion.RED_BALLS,
                ObstacleRegion.BUMPER_WALL,
                ObstacleRegion.MAZE,
                ObstacleRegion.MOVING_PLATFORMS
        ));
    }

    @Override
    protected void setupCheckpoints() {
        checkPoints.add(new CheckPoint("map_2_cp1", WPoint.from(2931.5, 209, 31.5), 90.0F, -10F, null));
        checkPoints.add(new CheckPoint("map_2_cp2", WPoint.from(2868.5, 209, 31.5), 125.0f, 0F, null));
        checkPoints.add(new CheckPoint("map_2_cp3", WPoint.from(2793.5, 210, 30.5), 19.0f, 0F, null));
        checkPoints.add(new CheckPoint("map_2_cp4", WPoint.from(2852.5, 209, 81.5), -90.0F, 0F, null));
        checkPoints.add(new CheckPoint("map_2_cp5", WPoint.from(2918.5, 209, 120.5), 90.0F, 0F, null));
        checkPoints.add(new CheckPoint("map_2_cp6", WPoint.from(2842.5, 209, 120.5), 90.0F, 0F, null));
        checkPoints.add(new CheckPoint("map_2_cp7", WPoint.from(2742.5, 209, 105.5), 0.0F, 0F, null));
        checkPoints.add(new CheckPoint("map_2_cp9", WPoint.from(2871.5, 209, 181.5), -55.0F, 0F, null));
        checkPoints.add(new CheckPoint("map_2_cp10", WPoint.from(2840.5, 209, 240.5), 90F, 0F, null));
        checkPoints.add(new CheckPoint("map_2_cp11", WPoint.from(2749.5, 209, 279.5), -90F, 0F, null));

    }

    @Override
    protected void setupObstacles() {

        RedBallsObstacle redBallsObstacle = new RedBallsObstacle(120, 120);

        ExtendedBumperWall extendedBumperWall = new ExtendedBumperWall(60,
                List.of(BumperObject.of(30, WLocation.from(2854, 216, 17), WLocation.from(2854, 216.5, 17.2), 0, 0, 1).setScale(new Vector(3, 3, 3)),
                        BumperObject.of(20, WLocation.from(2835, 216, 17), WLocation.from(2835, 216.5, 17.2), 0, 0, 1).setScale(new Vector(3, 3, 3)),
                        BumperObject.of(40, WLocation.from(2830, 216, 17), WLocation.from(2830, 216.5, 17.2), 0, 0, 1).setScale(new Vector(3, 3, 3)),
                        BumperObject.of(10, WLocation.from(2816, 216, 17), WLocation.from(2816, 216.5, 17.2), 0, 0, 1).setScale(new Vector(3, 3, 3)),
                        BumperObject.of(30, WLocation.from(2807, 216, 17), WLocation.from(2807, 216.5, 17.2), 0, 0, 1).setScale(new Vector(3, 3, 3)),
                        BumperObject.of(20, WLocation.from(2807, 210, 17), WLocation.from(2807, 210.5, 17.2), 0, 0, 1).setScale(new Vector(3, 3, 3)),
                        BumperObject.of(10, WLocation.from(2855, 209, 18), WLocation.from(2855, 209.5, 20), 0, 0, 1),
                        BumperObject.of(30, WLocation.from(2853, 209, 18), WLocation.from(2853, 209.5, 20), 0, 0, 1),
                        BumperObject.of(40, WLocation.from(2851, 209, 18), WLocation.from(2851, 209.5, 20), 0, 0, 1),
                        BumperObject.of(0, WLocation.from(2826, 215, 18), WLocation.from(2826, 215.5, 20), 0, 0, 1),
                        BumperObject.of(20, WLocation.from(2823, 215, 18), WLocation.from(2823, 215.5, 20), 0, 0, 1),
                        BumperObject.of(60, WLocation.from(2820, 215, 18), WLocation.from(2820, 215.5, 20), 0, 0, 1),
                        BumperObject.of(50, WLocation.from(2804, 215, 18), WLocation.from(2804, 215.5, 20), 0, 0, 1),
                        BumperObject.of(60, WLocation.from(2804, 209, 18), WLocation.from(2804, 209.5, 20), 0, 0, 1),
                        BumperObject.of(40, WLocation.from(2810, 209, 18), WLocation.from(2810, 209.5, 20), 0, 0, 1)),

                SlidingWall.of(WLocation.from(2847, 208, 21), WLocation.from(2848, 210, 19), 0, 1, 0, 1.2f, 50).setDirection(Direction.SOUTH).setSize(WallSize.TWO_BY_THREE),
                SlidingWall.of(WLocation.from(2843, 208, 21), WLocation.from(2844, 209, 19), 0, 1, 0, 1.2f, 30).setDirection(Direction.SOUTH).setSize(WallSize.TWO_BY_TWO),
                SlidingWall.of(WLocation.from(2840, 208, 21), WLocation.from(2840, 208, 19), 0, 1, 0, 1.2f, 10).setDirection(Direction.SOUTH).setSize(WallSize.ONE_BY_ONE),
                SlidingWall.of(WLocation.from(2831, 208, 21), WLocation.from(2831, 210, 19), 0, 1, 0, 1.2f, 10).setDirection(Direction.SOUTH).setSize(WallSize.ONE_BY_THREE),
                SlidingWall.of(WLocation.from(2829, 208, 21), WLocation.from(2829, 210, 19), 0, 1, 0, 1.2f, 20).setDirection(Direction.SOUTH).setSize(WallSize.ONE_BY_THREE),
                SlidingWall.of(WLocation.from(2827, 208, 21), WLocation.from(2827, 210, 19), 0, 1, 0, 1.2f, 30).setDirection(Direction.SOUTH).setSize(WallSize.ONE_BY_THREE),
                SlidingWall.of(WLocation.from(2825, 208, 21), WLocation.from(2825, 210, 19), 0, 1, 0, 1.2f, 40).setDirection(Direction.SOUTH).setSize(WallSize.ONE_BY_THREE),
                SlidingWall.of(WLocation.from(2823, 208, 21), WLocation.from(2823, 210, 19), 0, 1, 0, 1.2f, 50).setDirection(Direction.SOUTH).setSize(WallSize.ONE_BY_THREE),
                SlidingWall.of(WLocation.from(2821, 208, 21), WLocation.from(2821, 210, 19), 0, 1, 0, 1.2f, 50).setDirection(Direction.SOUTH).setSize(WallSize.ONE_BY_THREE),
                SlidingWall.of(WLocation.from(2819, 208, 21), WLocation.from(2819, 210, 19), 0, 1, 0, 1.2f, 40).setDirection(Direction.SOUTH).setSize(WallSize.ONE_BY_THREE),
                SlidingWall.of(WLocation.from(2817, 208, 21), WLocation.from(2817, 210, 19), 0, 1, 0, 1.2f, 30).setDirection(Direction.SOUTH).setSize(WallSize.ONE_BY_THREE),
                SlidingWall.of(WLocation.from(2815, 208, 21), WLocation.from(2815, 210, 19), 0, 1, 0, 1.2f, 20).setDirection(Direction.SOUTH).setSize(WallSize.ONE_BY_THREE),
                SlidingWall.of(WLocation.from(2813, 208, 21), WLocation.from(2813, 210, 19), 0, 1, 0, 1.2f, 10).setDirection(Direction.SOUTH).setSize(WallSize.ONE_BY_THREE),
                SlidingWall.of(WLocation.from(2803, 208, 21), WLocation.from(2803, 208, 19), 0, 1, 0, 1.2f, 20).setDirection(Direction.SOUTH).setSize(WallSize.ONE_BY_ONE));

        BumperWall newBumperWall = new BumperWall(
                List.of(
                        BumperObject.of(20, WLocation.from(2788, 211, 35), WLocation.from(2790, 211.5, 35), -90f, 1, 0),
                        BumperObject.of(40, WLocation.from(2788, 212, 38), WLocation.from(2790, 212.5, 38), -90f, 1, 0),
                        BumperObject.of(30, WLocation.from(2788, 213, 41), WLocation.from(2790, 213.5, 41), -90f, 1, 0),
                        BumperObject.of(10, WLocation.from(2788, 212, 45), WLocation.from(2790, 212.5, 45), -90f, 1, 0),
                        BumperObject.of(0, WLocation.from(2788, 212, 49), WLocation.from(2790, 212.5, 49), -90f, 1, 0),
                        BumperObject.of(5, WLocation.from(2788, 212, 53), WLocation.from(2790, 212.5, 53), -90f, 1, 0),
                        BumperObject.of(35, WLocation.from(2788, 213, 56), WLocation.from(2790, 213.5, 56), -90f, 1, 0),
                        BumperObject.of(50, WLocation.from(2788, 214, 59), WLocation.from(2790, 214.5, 59), -90f, 1, 0),
                        BumperObject.of(40, WLocation.from(2788, 215, 62), WLocation.from(2790, 215.5, 62), -90f, 1, 0),
                        BumperObject.of(30, WLocation.from(2788, 214, 65), WLocation.from(2790, 214.5, 65), -90f, 1, 0),
                        BumperObject.of(5, WLocation.from(2788, 214, 69), WLocation.from(2790, 214.5, 69), -90f, 1, 0),
                        BumperObject.of(20, WLocation.from(2788, 215, 76), WLocation.from(2790, 215.5, 76), -90f, 1, 0),
                        BumperObject.of(20, WLocation.from(2788, 215, 62), WLocation.from(2790, 215.5, 62), -90f, 1, 0)
                )
                , 60);

        ClockArmsHolder clockArmsHolder = new ClockArmsHolder(new ClockArms(WLocation.from(2868, 209, 81), 6.3, 90), new ClockArms(WLocation.from(2886, 209, 81), 6.3, 90), new ClockArms(WLocation.from(2904, 209, 81), 6.3, 90), new ClockArms(WLocation.from(2922, 209, 81), 6.3, 90), new ClockArms(WLocation.from(2940, 209, 81), 6.3, 90));

        FanWall fanWall = new FanWall(List.of(FanObject.of(Cuboid.from(2908, 209, 116, 2906, 211, 122), FanRotation.POS_Z, 0, 20, 1), FanObject.of(Cuboid.from(2900, 209, 116, 2902, 211, 122), FanRotation.POS_Z, 40, 60, 1), FanObject.of(Cuboid.from(2896, 210, 116, 2894, 212, 122), FanRotation.POS_Z, 0, 20, 1), FanObject.of(Cuboid.from(2890, 211, 116, 2888, 213, 122), FanRotation.POS_Z, 40, 60, 1), FanObject.of(Cuboid.from(2883, 211, 116, 2881, 209, 122), FanRotation.POS_Z, 0, 20, 1), FanObject.of(Cuboid.from(2877, 211, 116, 2875, 209, 122), FanRotation.POS_Z, 40, 60, 1), FanObject.of(Cuboid.from(2870, 211, 116, 2868, 209, 122), FanRotation.POS_Z, 0, 20, 1), FanObject.of(Cuboid.from(2864, 212, 116, 2862, 210, 122), FanRotation.POS_Z, 40, 60, 1), FanObject.of(Cuboid.from(2858, 213, 116, 2856, 211, 122), FanRotation.POS_Z, 0, 20, 1), FanObject.of(Cuboid.from(2852, 211, 116, 2850, 211.5, 122), FanRotation.POS_Z, 40, 60, 1)), 80);

        SnakeObstacle snakeObstacle = new SnakeObstacle("map-1-snake", Material.GREEN_CONCRETE);

        PatternPlatforms patternPlatforms = new PatternPlatforms(200, 20, 240, Arrays.asList(Material.GREEN_CONCRETE, Material.RED_CONCRETE, Material.BLUE_CONCRETE), "map-2-all-patterns", "map-2-pattern-1", "map-2-pattern-2", "map-2-pattern-3");

        List<BumperObject> bumperObjects = new ArrayList<>(List.of(BumperObject.of(10, WLocation.from(2899, 211, 186), WLocation.from(2897, 211.5, 186), 90f, -1, 0), BumperObject.of(50, WLocation.from(2899, 212, 189), WLocation.from(2897, 212.5, 189), 90f, -1, 0), BumperObject.of(30, WLocation.from(2899, 213, 192), WLocation.from(2897, 213.5, 192), 90f, -1, 0), BumperObject.of(20, WLocation.from(2899, 212, 196), WLocation.from(2897, 212.5, 196), 90f, -1, 0), BumperObject.of(5, WLocation.from(2899, 212, 200), WLocation.from(2897, 212.5, 200), 90f, -1, 0), BumperObject.of(10, WLocation.from(2899, 212, 204), WLocation.from(2897, 212.5, 204), 90f, -1, 0), BumperObject.of(30, WLocation.from(2899, 212, 207), WLocation.from(2897, 212.5, 207), 90f, -1, 0), BumperObject.of(30, WLocation.from(2899, 212, 207), WLocation.from(2897, 212.5, 207), 90f, -1, 0)));
        bumperObjects.add(BumperObject.of(10, WLocation.from(2899, 214, 207), WLocation.from(2897, 214.5, 207), 90f, -1, 0));
        bumperObjects.add(BumperObject.of(5, WLocation.from(2899, 215, 213), WLocation.from(2897, 215.5, 213), 90f, -1, 0));
        bumperObjects.add(BumperObject.of(20, WLocation.from(2899, 215, 216), WLocation.from(2897, 215.5, 216), 90f, -1, 0));
        bumperObjects.add(BumperObject.of(40, WLocation.from(2899, 214, 220), WLocation.from(2897, 214.5, 220), 90f, -1, 0));
        bumperObjects.add(BumperObject.of(55, WLocation.from(2899, 214, 224), WLocation.from(2897, 214.5, 224), 90f, -1, 0));
        bumperObjects.add(BumperObject.of(10, WLocation.from(2899, 215, 227), WLocation.from(2897, 215.5, 227), 90f, -1, 0));
        bumperObjects.add(BumperObject.of(30, WLocation.from(2899, 215, 231), WLocation.from(2897, 215.5, 231), 90f, -1, 0));
        bumperObjects.add(BumperObject.of(50, WLocation.from(2899, 215, 235), WLocation.from(2897, 215.5, 235), 90f, -1, 0));
        BumperWall bumperWall = new BumperWall(bumperObjects, 60);

        MovingPlatforms movingPlatforms = new MovingPlatforms("map-2-moving-platforms", 102);

        FreezingIce freezingIce = new FreezingIce("map-2-freezing-ice");

        WaterSlide waterSlide = new WaterSlide("map-2-slide", 1, 20);

        obstacles.add(extendedBumperWall);
        obstacles.add(clockArmsHolder);
        obstacles.add(redBallsObstacle);
        obstacles.add(fanWall);
        obstacles.add(snakeObstacle);
        obstacles.add(bumperWall);
        obstacles.add(patternPlatforms);
        obstacles.add(movingPlatforms);
        obstacles.add(freezingIce);
        obstacles.add(waterSlide);
        obstacles.add(newBumperWall);


        obstacles.forEach(Obstacle::load);
    }
}
