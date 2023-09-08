package org.stormdev.mcwipeout.maps.map1;
/*
  Created by Stormbits at 8/21/2023
*/

import org.stormdev.mcwipeout.frame.game.CheckPoint;
import org.stormdev.mcwipeout.frame.game.Map;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.BumperObject;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.BumperWall;
import org.stormdev.mcwipeout.frame.team.WPoint;
import org.stormdev.mcwipeout.utils.WLocation;

import java.util.Arrays;

public class MapOneModule extends Map {

    public MapOneModule() {
        super();

        setSpawnPoint(new CheckPoint("", new WPoint(1000.5, 128, 0.5), -90.0F, 0F));
        setFinish(new CheckPoint("map_1_finish", new WPoint(1000.5, 128, 0.5), -90.0F, 0F));
    }


    @Override
    protected void setupCheckpoints() {

    }

    @Override
    protected void setupObstacles() {

        BumperWall bumperWall = new BumperWall(Arrays.asList(
                BumperObject.of(20, WLocation.from(1141, 116, 18), WLocation.from(1141, 116.5, 16), -180, 0, -1, null),
                BumperObject.of(40, WLocation.from(1144, 117, 18), WLocation.from(1144, 117.5, 16), -180, 0, -1, null)), 60);

        obstacles.add(bumperWall);
    }
}
