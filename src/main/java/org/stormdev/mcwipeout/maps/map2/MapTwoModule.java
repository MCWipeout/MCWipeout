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
import org.stormdev.mcwipeout.frame.team.WPoint;
import org.stormdev.mcwipeout.utils.WLocation;

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
        ExtendedBumperWall extendedBumperWall = new ExtendedBumperWall(SlidingWall.of(WLocation.from(2847, 208, 21), WLocation.from(2848, 210, 19), 0, 1, 0, 1.2f, 60));

        ClockArms clockArms = new ClockArms();

        obstacles.add(extendedBumperWall);
        obstacles.add(clockArms);

        obstacles.forEach(Obstacle::load);
    }
}
