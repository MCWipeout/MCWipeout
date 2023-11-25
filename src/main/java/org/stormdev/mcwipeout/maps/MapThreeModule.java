package org.stormdev.mcwipeout.maps;
/*
  Created by Stormbits at 9/29/2023
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
import org.stormdev.mcwipeout.frame.obstacles.platforms.DissapearingPlatforms;
import org.stormdev.mcwipeout.frame.obstacles.platforms.MovingPlatforms;
import org.stormdev.mcwipeout.frame.obstacles.redballs.RedBallsObstacle;
import org.stormdev.mcwipeout.frame.obstacles.snake.SnakeObstacle;
import org.stormdev.mcwipeout.frame.obstacles.sweeper.SweeperObstacle;
import org.stormdev.mcwipeout.frame.team.WPoint;
import org.stormdev.mcwipeout.utils.helpers.Cuboid;
import org.stormdev.mcwipeout.utils.helpers.Direction;
import org.stormdev.mcwipeout.utils.helpers.WLocation;

import java.util.List;

public class MapThreeModule extends Map {

    public MapThreeModule() {
        super("Cityscape");

        setSpawnPoint(new CheckPoint("", new WPoint(3804.5, 205, 110.5), 90.0F, 0F, ObstacleRegion.RED_BALLS));
        setFinish(new CheckPoint("map_3_finish", new WPoint(3804.5, 205, 110.5), 90.0F, 0F, null));

        setObstacleRegions(List.of(
                ObstacleRegion.RED_BALLS,
                ObstacleRegion.EXTENDED_BUMPER_WALL,
                ObstacleRegion.FANS,
                ObstacleRegion.CLOCK_ARMS,
                ObstacleRegion.DISAPPEARING_PLATFORM,
                ObstacleRegion.MOVING_PLATFORMS,
                ObstacleRegion.SNAKE,
                ObstacleRegion.ICE,
                ObstacleRegion.FANS2,
                ObstacleRegion.RED_BALLS2,
                ObstacleRegion.SWEEPER
        ));
    }

    @Override
    protected void setupCheckpoints() {
        checkPoints.add(new CheckPoint("map_3_cp1", WPoint.from(3717.5, 196, 110.5), 90.0F, 0F, ObstacleRegion.BUMPER_WALL));
        checkPoints.add(new CheckPoint("map_3_cp2", WPoint.from(3555.5, 196, 161.5), -90.0F, 0F, ObstacleRegion.FANS));
        checkPoints.add(new CheckPoint("map_3_cp3", WPoint.from(3643.5, 196, 161.5), -90.0F, 0F, ObstacleRegion.CLOCK_ARMS));
        checkPoints.add(new CheckPoint("map_3_cp4", WPoint.from(3755.5, 196, 161.5), -90.0F, 0F, ObstacleRegion.DISAPPEARING_PLATFORM));
        checkPoints.add(new CheckPoint("map_3_cp5", WPoint.from(3856.5, 196, 120.5), 180F, 0F, ObstacleRegion.MOVING_PLATFORMS));
        checkPoints.add(new CheckPoint("map_3_cp6", WPoint.from(3817.5, 206, 31.5), 90F, 0F, ObstacleRegion.SNAKE));
        checkPoints.add(new CheckPoint("map_3_cp7", WPoint.from(3720.5, 206, 15.5), 135f, 0F, ObstacleRegion.ICE));
        checkPoints.add(new CheckPoint("map_3_cp8", WPoint.from(3624.5, 206, 5.5), 90f, 0F, ObstacleRegion.FANS2));
        checkPoints.add(new CheckPoint("map_3_cp9", WPoint.from(3568.5, 206, -62.5), -90f, 0F, ObstacleRegion.RED_BALLS));
        checkPoints.add(new CheckPoint("map_3_cp10", WPoint.from(3686.5, 199, -62.5), -90f, 0F, ObstacleRegion.SWEEPER));
        checkPoints.add(new CheckPoint("map_3_cp11", WPoint.from(3686.5, 199, -62.5), -90f, 0F, null));
    }

    @Override
    protected void setupObstacles() {
        RedBallsObstacle redBallsObstacle = new RedBallsObstacle(240, 240);

        BumperWall bumperWall = new BumperWall(
                List.of(
                        BumperObject.of(10, WLocation.from(3671, 198, 80), WLocation.from(3671, 198.5, 82), 0, 0, 1),
                        BumperObject.of(30, WLocation.from(3668, 199, 80), WLocation.from(3668, 199.5, 82), 0, 0, 1),
                        BumperObject.of(50, WLocation.from(3665, 200, 80), WLocation.from(3665, 200.5, 82), 0, 0, 1),
                        BumperObject.of(0, WLocation.from(3661, 199, 80), WLocation.from(3661, 199.5, 82), 0, 0, 1),
                        BumperObject.of(40, WLocation.from(3657, 199, 80), WLocation.from(3657, 199.5, 82), 0, 0, 1),
                        BumperObject.of(15, WLocation.from(3653, 199, 80), WLocation.from(3653, 199.5, 82), 0, 0, 1),
                        BumperObject.of(35, WLocation.from(3650, 200, 80), WLocation.from(3650, 200.5, 82), 0, 0, 1),
                        BumperObject.of(55, WLocation.from(3647, 201, 80), WLocation.from(3647, 201.5, 82), 0, 0, 1),
                        BumperObject.of(0, WLocation.from(3644, 202, 80), WLocation.from(3644, 202.5, 82), 0, 0, 1),
                        BumperObject.of(20, WLocation.from(3641, 202, 80), WLocation.from(3641, 202.5, 82), 0, 0, 1),
                        BumperObject.of(15, WLocation.from(3637, 201, 80), WLocation.from(3637, 201.5, 82), 0, 0, 1),
                        BumperObject.of(30, WLocation.from(3633, 201, 80), WLocation.from(3633, 201.5, 82), 0, 0, 1),
                        BumperObject.of(0, WLocation.from(3630, 202, 80), WLocation.from(3630, 202.5, 82), 0, 0, 1),
                        BumperObject.of(50, WLocation.from(3626, 202, 80), WLocation.from(3626, 202.5, 82), 0, 0, 1),
                        BumperObject.of(10, WLocation.from(3622, 202, 80), WLocation.from(3622, 202.5, 82), 0, 0, 1)
                ), 60
        );

        FanWall fanWall = new FanWall(List.of(
                FanObject.of(Cuboid.from(3567, 196, 164, 3565, 198, 158), FanRotation.NEG_Z, 0, 20, 1),
                FanObject.of(Cuboid.from(3572, 196, 164, 3570, 198, 158), FanRotation.NEG_Z, 40, 60, 1),
                FanObject.of(Cuboid.from(3577, 196, 164, 3575, 198, 158), FanRotation.NEG_Z, 0, 20, 1),
                FanObject.of(Cuboid.from(3580, 196, 164, 3582, 198, 158), FanRotation.NEG_Z, 40, 60, 1),
                FanObject.of(Cuboid.from(3587, 196, 164, 3585, 198, 158), FanRotation.NEG_Z, 0, 20, 1),
                FanObject.of(Cuboid.from(3590, 196, 164, 3592, 198, 158), FanRotation.NEG_Z, 40, 60, 1),
                FanObject.of(Cuboid.from(3597, 196, 164, 3595, 198, 158), FanRotation.NEG_Z, 0, 20, 1),
                FanObject.of(Cuboid.from(3600, 196, 164, 3602, 198, 158), FanRotation.NEG_Z, 40, 60, 1)
        ),
                80);

        FanWall fanWallFinal = new FanWall(List.of(
                FanObject.of(Cuboid.from(3615, 208, 8, 3613, 206, 3), FanRotation.NEG_Z, 0, 20, 1),
                FanObject.of(Cuboid.from(3609, 208, 8, 3607, 206, 3), FanRotation.NEG_Z, 40, 60, 1),
                FanObject.of(Cuboid.from(3603, 208, 8, 3601, 206, 3), FanRotation.NEG_Z, 0, 20, 1),
                FanObject.of(Cuboid.from(3597, 208, 8, 3595, 206, 3), FanRotation.NEG_Z, 40, 60, 1),
                FanObject.of(Cuboid.from(3591, 208, 8, 3589, 206, 3), FanRotation.NEG_Z, 0, 20, 1),
                FanObject.of(Cuboid.from(3585, 208, 8, 3583, 206, 3), FanRotation.NEG_Z, 40, 60, 1),
                FanObject.of(Cuboid.from(3579, 208, 8, 3577, 206, 3), FanRotation.NEG_Z, 0, 20, 1),
                FanObject.of(Cuboid.from(3573, 208, 8, 3571, 206, 3), FanRotation.NEG_Z, 40, 60, 1),
                FanObject.of(Cuboid.from(3567, 208, 8, 3565, 206, 3), FanRotation.NEG_Z, 0, 20, 1),
                FanObject.of(Cuboid.from(3561, 208, 8, 3559, 206, 3), FanRotation.NEG_Z, 40, 60, 1)
        ),
                80);


        ExtendedBumperWall extendedBumperWall = new ExtendedBumperWall(60,
                List.of(BumperObject.of(10, WLocation.from(3669, 203, 140), WLocation.from(3669, 203.5, 138), -180, 0, -1),
                        BumperObject.of(10, WLocation.from(3666, 198, 140), WLocation.from(3666, 198.5, 138), -180, 0, -1),
                        BumperObject.of(20, WLocation.from(3664, 198, 140), WLocation.from(3664, 198.5, 138), -180, 0, -1),
                        BumperObject.of(50, WLocation.from(3662, 198, 140), WLocation.from(3662, 198.5, 138), -180, 0, -1),
                        BumperObject.of(10, WLocation.from(3656, 202, 140), WLocation.from(3656, 203.5, 140.8), -180, 0, -1).setScale(new Vector(3, 3, 3)),
                        BumperObject.of(50, WLocation.from(3650, 202, 140), WLocation.from(3650, 203.5, 140.8), -180, 0, -1).setScale(new Vector(3, 3, 3))),

                SlidingWall.of(WLocation.from(3673, 197, 138), WLocation.from(3673, 197, 140), 0, 0, 0, -1.2f, 10).setDirection(Direction.NORTH).setSize(WallSize.ONE_BY_ONE),
                SlidingWall.of(WLocation.from(3673, 199, 138), WLocation.from(3673, 199, 140), 0, 0, 0, -1.2f, 50).setDirection(Direction.NORTH).setSize(WallSize.ONE_BY_ONE),
                SlidingWall.of(WLocation.from(3673, 201, 138), WLocation.from(3673, 201, 140), 0, 0, 0, -1.2f, 10).setDirection(Direction.NORTH).setSize(WallSize.ONE_BY_ONE),
                SlidingWall.of(WLocation.from(3671, 198, 138), WLocation.from(3671, 198, 140), 0, 0, 0, -1.2f, 20).setDirection(Direction.NORTH).setSize(WallSize.ONE_BY_ONE),
                SlidingWall.of(WLocation.from(3660, 197, 138), WLocation.from(3660, 197, 140), 0, 0, 0, -1.2f, 20).setDirection(Direction.NORTH).setSize(WallSize.ONE_BY_ONE),
                SlidingWall.of(WLocation.from(3658, 198, 138), WLocation.from(3658, 199, 140), 0, 0, 0, -1.2f, 10).setDirection(Direction.NORTH).setSize(WallSize.ONE_BY_ONE),
                SlidingWall.of(WLocation.from(3653, 201, 138), WLocation.from(3653, 203, 140), 0, 0, 0, -1.2f, 40).setDirection(Direction.NORTH).setSize(WallSize.ONE_BY_THREE)
        );

//        ClockArmsHolder clockArms = new ClockArmsHolder(
//                ClockArms.of(WLocation.from(3659, 196, 161), 6.3, 90),
//                ClockArms.of(WLocation.from(3677, 196, 161), 6.3, 90),
//                ClockArms.of(WLocation.from(3695, 196, 161), 6.3, 90)
//        );


        DissapearingPlatforms dissapearingPlatforms = new DissapearingPlatforms("map-3-dissapearing", 65);

        MovingPlatforms movingPlatforms = new MovingPlatforms("map-3-exported", 102);

        SnakeObstacle snakeObstacle = new SnakeObstacle("map-3-snake", Material.GREEN_CONCRETE);

        FreezingIce freezingIce = new FreezingIce("map-3-ice");

        SweeperObstacle sweeperObstacle = new SweeperObstacle(WLocation.from(3705, 199.5, -63).toCenter());

        obstacles.add(fanWall);
        obstacles.add(sweeperObstacle);
        obstacles.add(bumperWall);
        obstacles.add(fanWallFinal);
        //obstacles.add(clockArms);
        obstacles.add(redBallsObstacle);
        obstacles.add(dissapearingPlatforms);
        obstacles.add(movingPlatforms);
        obstacles.add(snakeObstacle);
        obstacles.add(freezingIce);
        obstacles.add(extendedBumperWall);

        obstacles.forEach(Obstacle::load);
    }
}
