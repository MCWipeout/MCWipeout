package org.stormdev.mcwipeout.maps.map2;
/*
  Created by Stormbits at 8/21/2023
*/

import org.stormdev.mcwipeout.frame.game.CheckPoint;
import org.stormdev.mcwipeout.frame.game.Map;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.extended.ExtendedBumperWall;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.extended.SlidingWall;
import org.stormdev.mcwipeout.frame.obstacles.clockarms.ClockArms;
import org.stormdev.mcwipeout.frame.obstacles.clockarms.ClockArmsHolder;
import org.stormdev.mcwipeout.frame.team.WPoint;
import org.stormdev.mcwipeout.utils.WLocation;

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


        ExtendedBumperWall extendedBumperWall = new ExtendedBumperWall(
                List.of(),
                SlidingWall.of(WLocation.from(2847, 208, 21), WLocation.from(2848, 210, 19), 0, 1, 0, 1.2f, 60));

        ClockArmsHolder clockArmsHolder = new ClockArmsHolder(
                new ClockArms(WLocation.from(2868, 209, 81), 6.3, 90),
                new ClockArms(WLocation.from(2886, 209, 81), 6.3, 90),
                new ClockArms(WLocation.from(2904, 209, 81), 6.3, 90),
                new ClockArms(WLocation.from(2922, 209, 81), 6.3, 90),
                new ClockArms(WLocation.from(2940, 209, 81), 6.3, 90)
        );

        obstacles.add(extendedBumperWall);
        obstacles.add(clockArmsHolder);

        obstacles.forEach(Obstacle::load);
    }
}
