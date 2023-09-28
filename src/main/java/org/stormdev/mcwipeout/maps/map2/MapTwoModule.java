package org.stormdev.mcwipeout.maps.map2;
/*
  Created by Stormbits at 8/21/2023
*/

import org.bukkit.Material;
import org.stormdev.mcwipeout.frame.game.CheckPoint;
import org.stormdev.mcwipeout.frame.game.Map;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.BumperObject;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.BumperWall;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.extended.ExtendedBumperWall;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.extended.SlidingWall;
import org.stormdev.mcwipeout.frame.obstacles.clockarms.ClockArms;
import org.stormdev.mcwipeout.frame.obstacles.clockarms.ClockArmsHolder;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanObject;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanRotation;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanWall;
import org.stormdev.mcwipeout.frame.obstacles.redballs.RedBallsObstacle;
import org.stormdev.mcwipeout.frame.obstacles.snake.SnakeObstacle;
import org.stormdev.mcwipeout.frame.team.WPoint;
import org.stormdev.mcwipeout.utils.helpers.Cuboid;
import org.stormdev.mcwipeout.utils.helpers.WLocation;

import java.util.List;

public class MapTwoModule extends Map {

    public MapTwoModule() {
        super();

        //setSpawnPoint(new CheckPoint("", new WPoint(2993.5, 228, 31.5), 90.0F, 0F));
        setFinish(new CheckPoint("map_2_finish", new WPoint(2993.5, 228, 31.5), 90.0F, 0F));
    }

    @Override
    protected void setupCheckpoints() {

    }

    @Override
    protected void setupObstacles() {

        RedBallsObstacle redBallsObstacle = new RedBallsObstacle(120, 120);

        ExtendedBumperWall extendedBumperWall = new ExtendedBumperWall(60, List.of(), SlidingWall.of(WLocation.from(2847, 208, 21), WLocation.from(2848, 210, 19), 0, 1, 0, 1.2f, 50), SlidingWall.of(WLocation.from(2843, 208, 21), WLocation.from(2844, 209, 19), 0, 1, 0, 1.2f, 30), SlidingWall.of(WLocation.from(2839, 208, 21), WLocation.from(2840, 208, 19), 0, 1, 0, 1.2f, 10), SlidingWall.of(WLocation.from(2831, 208, 21), WLocation.from(2831, 212, 19), 0, 1, 0, 1.2f, 10), SlidingWall.of(WLocation.from(2829, 208, 21), WLocation.from(2829, 212, 19), 0, 1, 0, 1.2f, 20), SlidingWall.of(WLocation.from(2827, 208, 21), WLocation.from(2827, 212, 19), 0, 1, 0, 1.2f, 30), SlidingWall.of(WLocation.from(2825, 208, 21), WLocation.from(2825, 212, 19), 0, 1, 0, 1.2f, 40), SlidingWall.of(WLocation.from(2823, 208, 21), WLocation.from(2823, 212, 19), 0, 1, 0, 1.2f, 50), SlidingWall.of(WLocation.from(2821, 208, 21), WLocation.from(2821, 212, 19), 0, 1, 0, 1.2f, 50), SlidingWall.of(WLocation.from(2819, 208, 21), WLocation.from(2819, 212, 19), 0, 1, 0, 1.2f, 40), SlidingWall.of(WLocation.from(2817, 208, 21), WLocation.from(2817, 212, 19), 0, 1, 0, 1.2f, 30), SlidingWall.of(WLocation.from(2815, 208, 21), WLocation.from(2815, 212, 19), 0, 1, 0, 1.2f, 20), SlidingWall.of(WLocation.from(2813, 208, 21), WLocation.from(2813, 212, 19), 0, 1, 0, 1.2f, 10), SlidingWall.of(WLocation.from(2803, 208, 21), WLocation.from(2803, 208, 19), 0, 1, 0, 1.2f, 20), SlidingWall.of(WLocation.from(2801, 209, 21), WLocation.from(2801, 209, 19), 0, 1, 0, 1.2f, 30));

        ClockArmsHolder clockArmsHolder = new ClockArmsHolder(new ClockArms(WLocation.from(2868, 209, 81), 6.3, 90), new ClockArms(WLocation.from(2886, 209, 81), 6.3, 90), new ClockArms(WLocation.from(2904, 209, 81), 6.3, 90), new ClockArms(WLocation.from(2922, 209, 81), 6.3, 90), new ClockArms(WLocation.from(2940, 209, 81), 6.3, 90));

        FanWall fanWall = new FanWall(List.of(FanObject.of(Cuboid.from(2908, 209, 116, 2906, 211, 122), FanRotation.POS_Z, 0, 20, 1), FanObject.of(Cuboid.from(2900, 209, 116, 2902, 211, 122), FanRotation.POS_Z, 40, 60, 1), FanObject.of(Cuboid.from(2896, 210, 116, 2894, 212, 122), FanRotation.POS_Z, 0, 20, 1), FanObject.of(Cuboid.from(2890, 211, 116, 2888, 213, 122), FanRotation.POS_Z, 40, 60, 1), FanObject.of(Cuboid.from(2883, 211, 116, 2881, 209, 122), FanRotation.POS_Z, 0, 20, 1), FanObject.of(Cuboid.from(2877, 211, 116, 2875, 209, 122), FanRotation.POS_Z, 40, 60, 1), FanObject.of(Cuboid.from(2870, 211, 116, 2868, 209, 122), FanRotation.POS_Z, 0, 20, 1), FanObject.of(Cuboid.from(2864, 212, 116, 2862, 210, 122), FanRotation.POS_Z, 40, 60, 1), FanObject.of(Cuboid.from(2858, 213, 116, 2856, 211, 122), FanRotation.POS_Z, 0, 20, 1), FanObject.of(Cuboid.from(2852, 211, 116, 2850, 209, 122), FanRotation.POS_Z, 40, 60, 1)), 80);

        SnakeObstacle snakeObstacle = new SnakeObstacle("map-1-snake", Material.GREEN_CONCRETE);

        BumperWall bumperWall = new BumperWall(List.of(BumperObject.of(10, WLocation.from(2899, 211, 186), WLocation.from(2897, 211, 186), 90f, -1, 0), BumperObject.of(50, WLocation.from(2899, 212, 189), WLocation.from(2897, 212, 189), 90f, -1, 0), BumperObject.of(30, WLocation.from(2899, 213, 192), WLocation.from(2897, 213, 192), 90f, -1, 0), BumperObject.of(20, WLocation.from(2899, 212, 196), WLocation.from(2897, 212, 196), 90f, -1, 0), BumperObject.of(5, WLocation.from(2899, 212, 200), WLocation.from(2897, 212, 200), 90f, -1, 0), BumperObject.of(10, WLocation.from(2899, 212, 204), WLocation.from(2897, 212, 204), 90f, -1, 0), BumperObject.of(30, WLocation.from(2899, 212, 207), WLocation.from(2897, 212, 207), 90f, -1, 0), BumperObject.of(30, WLocation.from(2899, 212, 207), WLocation.from(2897, 212, 207), 90f, -1, 0)), 60);

        obstacles.add(extendedBumperWall);
        obstacles.add(clockArmsHolder);
        obstacles.add(redBallsObstacle);
        obstacles.add(fanWall);
        obstacles.add(snakeObstacle);
        obstacles.add(bumperWall);


        obstacles.forEach(Obstacle::load);
    }
}
