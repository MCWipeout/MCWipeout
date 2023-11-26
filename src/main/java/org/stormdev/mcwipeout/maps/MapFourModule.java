package org.stormdev.mcwipeout.maps;
/*
  Created by Stormbits at 11/2/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.stormdev.mcwipeout.frame.obstacles.fans.FanObject;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanRotation;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanWall;
import org.stormdev.mcwipeout.frame.obstacles.ice.FreezingIce;
import org.stormdev.mcwipeout.frame.obstacles.patterns.PatternPlatforms;
import org.stormdev.mcwipeout.frame.obstacles.platforms.DissapearingPlatforms;
import org.stormdev.mcwipeout.frame.obstacles.platforms.MovingPlatforms;
import org.stormdev.mcwipeout.frame.obstacles.redballs.RedBallsObstacle;
import org.stormdev.mcwipeout.frame.obstacles.sweeper.SweeperObstacle;
import org.stormdev.mcwipeout.frame.obstacles.water.WaterSlide;
import org.stormdev.mcwipeout.frame.team.WPoint;
import org.stormdev.mcwipeout.utils.helpers.Cuboid;
import org.stormdev.mcwipeout.utils.helpers.Direction;
import org.stormdev.mcwipeout.utils.helpers.WLocation;

import java.util.Arrays;
import java.util.List;

public class MapFourModule extends Map {

    public MapFourModule() {
        super("Turbulent Tides");

        setSpawnPoint(new CheckPoint("", new WPoint(4804.5, 106, 111.5), -90.0F, 0F, ObstacleRegion.RED_BALLS));
        setFinish(new CheckPoint("map_4_finish", new WPoint(4804.5, 106, 111.5), -90.0F, 0F, null));

        setObstacleRegions(List.of(
                ObstacleRegion.RED_BALLS,
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
        checkPoints.add(new CheckPoint("map_4_cp5", new WPoint(4893.5, 102, -10.5), 90.0F, 0F, ObstacleRegion.DISAPPEARING_PLATFORM));
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

        ClockArmsHolder clockArmsHolder = new ClockArmsHolder(ClockArms.of(WLocation.from(4945, 102, 111), 6.3, 90),
                ClockArms.of(WLocation.from(4963, 102, 111), 6.3, 90), ClockArms.of(WLocation.from(4981, 102, 111), 6.3, 90));

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
                        BumperObject.of(60, WLocation.from(5040, 107.5, 49), WLocation.from(5038, 107.5, 49), 90, -1, 0),
                        BumperObject.of(50, WLocation.from(5040, 107.5, 33), WLocation.from(5038, 107.5, 33), 90, -1, 0)),

                SlidingWall.of(WLocation.from(5038, 100, 76), WLocation.from(5039, 102, 77), 0, 0, 0.8f, 0, 50).setDirection(Direction.WEST).setSize(WallSize.TWO_BY_THREE),
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

        DissapearingPlatforms dissapearingPlatforms = new DissapearingPlatforms("map-4-disappearing", 65);

        PatternPlatforms patternPlatforms = new PatternPlatforms(200, 20, 240, Arrays.asList(Material.GREEN_CONCRETE, Material.RED_CONCRETE, Material.BLUE_CONCRETE), "map-4-all-platforms", "map-4-pattern-1", "map-4-pattern-2", "map-4-pattern-3");

        SweeperObstacle sweeperObstacle2 = new SweeperObstacle(WLocation.from(4733, 102, 55).toCenter());

        BumperWall secondBumperWall = new BumperWall(
                List.of(
                        BumperObject.of(20, WLocation.from(4775, 104, -23), WLocation.from(4773, 104.5, -23), 90f, -1, 0),
                        BumperObject.of(40, WLocation.from(4775, 105, -20), WLocation.from(4773, 105.5, -20), 90f, -1, 0),
                        BumperObject.of(30, WLocation.from(4775, 106, -17), WLocation.from(4773, 106.5, -17), 90f, -1, 0),
                        BumperObject.of(10, WLocation.from(4775, 105, -13), WLocation.from(4773, 105.5, -13), 90f, -1, 0),
                        BumperObject.of(0, WLocation.from(4775, 105, -9), WLocation.from(4773, 105.5, -9), 90f, -1, 0),
                        BumperObject.of(35, WLocation.from(4775, 105, -5), WLocation.from(4773, 105.5, -5), 90f, -1, 0),
                        BumperObject.of(50, WLocation.from(4775, 106, -2), WLocation.from(4773, 106.5, -2), 90f, -1, 0),
                        BumperObject.of(40, WLocation.from(4775, 107, 1), WLocation.from(4773, 107.5, 1), 90f, -1, 0),
                        BumperObject.of(30, WLocation.from(4775, 108, 4), WLocation.from(4773, 108.5, 4), 90f, -1, 0),
                        BumperObject.of(5, WLocation.from(4775, 107, 7), WLocation.from(4773, 107.5, 7), 90f, -1, 0),
                        BumperObject.of(20, WLocation.from(4775, 107, 11), WLocation.from(4773, 107.5, 11), 90f, -1, 0),
                        BumperObject.of(20, WLocation.from(4775, 108, 18), WLocation.from(4773, 108.5, 18), 90f, -1, 0)
                )
                , 60);

        FreezingIce freezingIce = new FreezingIce("map-4-freezing-ice");

        WaterSlide waterSlide = new WaterSlide("map-4-slide", 1, 20);

        MovingPlatforms movingPlatforms = new MovingPlatforms("map-4-moving-platforms", 102);

        BumperWall tower = new BumperWall(
                List.of(
                        BumperObject.of(20, WLocation.from(4852, 104, 237), WLocation.from(4852, 104.5, 235), -180, 0, -1),
                        BumperObject.of(40, WLocation.from(4858, 106, 233), WLocation.from(4856, 106.5, 233), 90, -1, 0),
                        BumperObject.of(30, WLocation.from(4859, 107, 231), WLocation.from(4857, 107.5, 231), 90, -1, 0),
                        BumperObject.of(10, WLocation.from(4859, 108, 228), WLocation.from(4857, 108.5, 228), 90, -1, 0),
                        BumperObject.of(0, WLocation.from(4855, 110, 222), WLocation.from(4855, 110.5, 224), 0, 0, 1),
                        BumperObject.of(35, WLocation.from(4853, 111, 221), WLocation.from(4853, 111.5, 223), 0, 0, 1),
                        BumperObject.of(50, WLocation.from(4850, 112, 221), WLocation.from(4850, 112.5, 223), 0, 0, 1),
                        BumperObject.of(40, WLocation.from(4844, 114, 225), WLocation.from(4846, 114.5, 225), -90, 1, 0),
                        BumperObject.of(30, WLocation.from(4843, 115, 227), WLocation.from(4845, 115.5, 227), -90, 1, 0),
                        BumperObject.of(5, WLocation.from(4843, 116, 230), WLocation.from(4845, 116.5, 230), -90, 1, 0),
                        BumperObject.of(20, WLocation.from(4847, 118, 236), WLocation.from(4847, 118.5, 234), -180, 0, -1),
                        BumperObject.of(35, WLocation.from(4849, 119, 237), WLocation.from(4849, 119.5, 235), -180, 0, -1)
                ),
                65);

        FanWall fanWall = new FanWall(List.of(
                FanObject.of(Cuboid.from(5037, 123, 227, 5039, 121, 234), FanRotation.NEG_Z, 0, 20, 1),
                FanObject.of(Cuboid.from(5045, 121, 234, 5043, 123, 227), FanRotation.NEG_Z, 40, 60, 1),
                FanObject.of(Cuboid.from(5051, 121, 234, 5049, 123, 227), FanRotation.NEG_Z, 0, 20, 1),
                FanObject.of(Cuboid.from(5057, 121, 234, 5055, 123, 227), FanRotation.NEG_Z, 40, 60, 1),
                FanObject.of(Cuboid.from(5067, 121, 222, 5060, 123, 224), FanRotation.NEG_X, 0, 20, 1),
                FanObject.of(Cuboid.from(5067, 121, 216, 5060, 123, 218), FanRotation.NEG_X, 40, 60, 1),
                FanObject.of(Cuboid.from(5067, 121, 210, 5060, 123, 212), FanRotation.NEG_X, 0, 20, 1),
                FanObject.of(Cuboid.from(5067, 121, 204, 5060, 123, 206), FanRotation.NEG_X, 40, 60, 1),
                FanObject.of(Cuboid.from(5067, 121, 198, 5060, 123, 200), FanRotation.NEG_X, 0, 20, 1),
                FanObject.of(Cuboid.from(5067, 121, 192, 5060, 123, 194), FanRotation.NEG_X, 40, 60, 1),
                FanObject.of(Cuboid.from(5067, 121, 186, 5060, 123, 188), FanRotation.NEG_X, 0, 20, 1),
                FanObject.of(Cuboid.from(5067, 121, 180, 5060, 123, 182), FanRotation.NEG_X, 40, 60, 1),
                FanObject.of(Cuboid.from(5067, 121, 174, 5060, 123, 176), FanRotation.NEG_X, 40, 60, 1)
        ), 80);

        obstacles.add(redBallsObstacle);
        obstacles.add(clockArmsHolder);
        obstacles.add(extendedBumperWall);
        obstacles.add(newBumperWall);
        obstacles.add(sweeperObstacle);
        obstacles.add(dissapearingPlatforms);
        obstacles.add(secondBumperWall);
        obstacles.add(patternPlatforms);
        obstacles.add(sweeperObstacle2);
        obstacles.add(freezingIce);
        obstacles.add(waterSlide);
        obstacles.add(movingPlatforms);
        obstacles.add(tower);
        obstacles.add(fanWall);
    }
}
