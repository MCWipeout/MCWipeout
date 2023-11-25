package org.stormdev.mcwipeout.maps;
/*
  Created by Stormbits at 11/2/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.util.Vector;
import org.stormdev.mcwipeout.frame.game.CheckPoint;
import org.stormdev.mcwipeout.frame.game.Map;
import org.stormdev.mcwipeout.frame.obstacles.ObstacleRegion;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.BumperObject;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.BumperWall;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.extended.ExtendedBumperWall;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.extended.SlidingWall;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.extended.WallSize;
import org.stormdev.mcwipeout.frame.obstacles.clockarms.ClockArms;
import org.stormdev.mcwipeout.frame.obstacles.clockarms.ClockArmsHolder;
import org.stormdev.mcwipeout.frame.obstacles.redballs.RedBallsObstacle;
import org.stormdev.mcwipeout.frame.obstacles.sweeper.SweeperObstacle;
import org.stormdev.mcwipeout.frame.team.WPoint;
import org.stormdev.mcwipeout.utils.helpers.Direction;
import org.stormdev.mcwipeout.utils.helpers.WLocation;

import java.util.List;

public class MapFourModule extends Map {

    public MapFourModule() {
        super("Map 4");

        setSpawnPoint(new CheckPoint("", new WPoint(4804.5, 106, 111.5), -90.0F, 0F, ObstacleRegion.RED_BALLS));
        setFinish(new CheckPoint("map_4_finish", new WPoint(4804.5, 106, 111.5), -90.0F, 0F, null));

        setObstacleRegions(List.of(
                ObstacleRegion.CLOCK_ARMS,
                ObstacleRegion.EXTENDED_BUMPER_WALL,
                ObstacleRegion.BUMPER_WALL,
                ObstacleRegion.SWEEPER,
                ObstacleRegion.DISAPPEARING_PLATFORM,
                ObstacleRegion.PATTERN_MEMORIZATION,
                ObstacleRegion.SWEEPER2,
                ObstacleRegion.WATER_STAIRS,
                ObstacleRegion.SPIRAL_BUMPER,
                ObstacleRegion.MOVING_PLATFORMS,
                ObstacleRegion.FANS,
                ObstacleRegion.RED_BALLS2
        ));
    }

    @Override
    protected void setupCheckpoints() {
        checkPoints.add(new CheckPoint("map_4_cp1", new WPoint(4932.5, 102, 111.5), -90.0F, 0F, ObstacleRegion.CLOCK_ARMS));
        checkPoints.add(new CheckPoint("map_4_cp2", new WPoint(5027.5, 102, 96.5), -140.0F, 0F, ObstacleRegion.EXTENDED_BUMPER_WALL));
        checkPoints.add(new CheckPoint("map_4_cp3", new WPoint(5028.5, 102, 22.5), 105.0F, 0F, ObstacleRegion.BUMPER_WALL));
        checkPoints.add(new CheckPoint("map_4_cp4", new WPoint(4943.5, 102, 2.5), 90.0F, 0F, ObstacleRegion.SWEEPER));
        checkPoints.add(new CheckPoint("map_4_cp5", new WPoint(4839.5, 102, -10.5), 90.0F, 0F, ObstacleRegion.DISAPPEARING_PLATFORM));
        checkPoints.add(new CheckPoint("map_4_cp6", new WPoint(4771.5, 102, -40.5), 90.0F, 0F, ObstacleRegion.PATTERN_MEMORIZATION));
        checkPoints.add(new CheckPoint("map_4_cp7", new WPoint(4733, 102, 38), 0.0F, 0F, ObstacleRegion.SWEEPER2));
        checkPoints.add(new CheckPoint("map_4_cp8", new WPoint(4733.5, 102, 202.5), 0.0F, 0F, ObstacleRegion.WATER_STAIRS));
        checkPoints.add(new CheckPoint("map_4_cp9", new WPoint(4838.5, 102, 229.5), -90.0F, 0F, ObstacleRegion.SPIRAL_BUMPER));
        checkPoints.add(new CheckPoint("map_4_cp10", new WPoint(4914, 121, 229), -90.0F, 0F, ObstacleRegion.MOVING_PLATFORMS));
        checkPoints.add(new CheckPoint("map_4_cp11", new WPoint(5030.5, 121, 229.5), -90.0F, 0F, ObstacleRegion.FANS));
        checkPoints.add(new CheckPoint("map_4_cp12", new WPoint(5005.5, 121, 169.5), 90.0F, 0F, ObstacleRegion.RED_BALLS2));
        checkPoints.add(new CheckPoint("map_4_cp13", new WPoint(4931.5, 103, 169.5), 90.0F, 0F, null));
    }

    @Override
    protected void setupObstacles() {
        RedBallsObstacle redBallsObstacle = new RedBallsObstacle(240, 240);

        ClockArmsHolder clockArmsHolder = new ClockArmsHolder(new ClockArms(WLocation.from(4945, 102, 111), 6.3, 90),
                new ClockArms(WLocation.from(4963, 102, 111), 6.3, 90), new ClockArms(WLocation.from(4981, 102, 111), 6.3, 90));

        ExtendedBumperWall extendedBumperWall = new ExtendedBumperWall(60,
                List.of(BumperObject.of(30, WLocation.from(5041, 108, 83), WLocation.from(5040.8, 108.5, 83), 90, -1, 0).setScale(new Vector(3, 3, 3)),
                        BumperObject.of(20, WLocation.from(5041, 108, 64), WLocation.from(5040.8, 108.5, 64), 90, -1, 0).setScale(new Vector(3, 3, 3)),
                        BumperObject.of(40, WLocation.from(5041, 108, 59), WLocation.from(5040.8, 108.5, 59), 90, -1, 0).setScale(new Vector(3, 3, 3)),
                        BumperObject.of(10, WLocation.from(5041, 108, 45), WLocation.from(5040.8, 108.5, 45), 90, -1, 0).setScale(new Vector(3, 3, 3)),
                        BumperObject.of(30, WLocation.from(5041, 108, 36), WLocation.from(5040.8, 108.5, 36), 90, -1, 0).setScale(new Vector(3, 3, 3)),
                        BumperObject.of(10, WLocation.from(5040, 101.5, 84), WLocation.from(5038, 101.5, 84), 90, -1, 0),
                        BumperObject.of(30, WLocation.from(5040, 101.5, 82), WLocation.from(5038, 101.5, 82), 90, -1, 0),
                        BumperObject.of(40, WLocation.from(5040, 101.5, 80), WLocation.from(5038, 101.5, 80), 90, -1, 0),
                        BumperObject.of(0, WLocation.from(5040, 107.5, 55), WLocation.from(5038, 107.5, 55), 90, -1, 0),
                        BumperObject.of(20, WLocation.from(5040, 107.5, 52), WLocation.from(5038, 107.5, 52), 90, -1, 0),
                        BumperObject.of(60, WLocation.from(5040, 107.5, 50), WLocation.from(5038, 107.5, 50), 90, -1, 0),
                        BumperObject.of(50, WLocation.from(5040, 107.5, 33), WLocation.from(5038, 107.5, 33), 90, -1, 0)),

                SlidingWall.of(WLocation.from(5037, 100, 76), WLocation.from(5039, 102, 77), 0, 0, 0.8f, 0, 50).setDirection(Direction.WEST).setSize(WallSize.TWO_BY_THREE),
                SlidingWall.of(WLocation.from(5037, 100, 72), WLocation.from(5039, 101, 73), 0, 0, -0.8f, 0, 30).setDirection(Direction.WEST).setSize(WallSize.TWO_BY_TWO),
                SlidingWall.of(WLocation.from(5037, 100, 69), WLocation.from(5039, 100, 69), 0, 0, -0.8f, 0, 10).setDirection(Direction.WEST).setSize(WallSize.ONE_BY_ONE));

        BumperWall newBumperWall = new BumperWall(
                List.of(
                        BumperObject.of(20, WLocation.from(5023, 103, 17), WLocation.from(5023, 103.5, 19), 0f, 0, 1),
                        BumperObject.of(40, WLocation.from(5020, 104, 17), WLocation.from(5020, 104.5, 19), 0f, 0, 1),
                        BumperObject.of(30, WLocation.from(5017, 105, 17), WLocation.from(5017, 105.5, 19), 0f, 0, 1),
                        BumperObject.of(10, WLocation.from(5013, 104, 17), WLocation.from(5013, 104.5, 19), 0f, 0, 1),
                        BumperObject.of(0, WLocation.from(5009, 104, 17), WLocation.from(5009, 104.5, 19), 0f, 0, 1),
                        BumperObject.of(35, WLocation.from(5005, 104, 17), WLocation.from(5005, 104.5, 19), 0f, 0, 1),
                        BumperObject.of(50, WLocation.from(5002, 105, 17), WLocation.from(5002, 105.5, 19), 0f, 0, 1),
                        BumperObject.of(40, WLocation.from(4999, 106, 17), WLocation.from(4999, 106.5, 19), 0f, 0, 1),
                        BumperObject.of(30, WLocation.from(4996, 107, 17), WLocation.from(4996, 107.5, 19), 0f, 0, 1),
                        BumperObject.of(5, WLocation.from(4993, 106, 17), WLocation.from(4993, 106.5, 19), 0f, 0, 1),
                        BumperObject.of(20, WLocation.from(4989, 106, 17), WLocation.from(4989, 106.5, 19), 0f, 0, 1),
                        BumperObject.of(20, WLocation.from(4982, 107, 17), WLocation.from(4982, 107.5, 19), 0f, 0, 1)
                )
                , 60);

        SweeperObstacle sweeperObstacle = new SweeperObstacle(WLocation.from(4926, 102, 2).toCenter());


        obstacles.add(redBallsObstacle);
        obstacles.add(clockArmsHolder);
        obstacles.add(extendedBumperWall);
        obstacles.add(newBumperWall);
        obstacles.add(sweeperObstacle);
    }
}
