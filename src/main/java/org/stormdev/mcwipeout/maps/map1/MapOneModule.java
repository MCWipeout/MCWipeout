package org.stormdev.mcwipeout.maps.map1;
/*
  Created by Stormbits at 8/21/2023
*/

import org.stormdev.mcwipeout.frame.game.CheckPoint;
import org.stormdev.mcwipeout.frame.game.Map;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.BumperObject;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.BumperWall;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanObject;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanRotation;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanWall;
import org.stormdev.mcwipeout.frame.obstacles.platforms.DissapearingPlatforms;
import org.stormdev.mcwipeout.frame.team.WPoint;
import org.stormdev.mcwipeout.utils.Cuboid;
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
                BumperObject.of(20, WLocation.from(1144, 117, 18), WLocation.from(1144, 117.5, 16), -180, 0, -1, null),
                BumperObject.of(20, WLocation.from(1147, 118, 18), WLocation.from(1147, 118.5, 16), -180, 0, -1, null),
                BumperObject.of(20, WLocation.from(1151, 117, 18), WLocation.from(1151, 117.5, 16), -180, 0, -1, null),
                BumperObject.of(20, WLocation.from(1155, 117, 18), WLocation.from(1155, 117.5, 16), -180, 0, -1, null),
                BumperObject.of(20, WLocation.from(1159, 117, 18), WLocation.from(1159, 117.5, 16), -180, 0, -1, null),
                BumperObject.of(20, WLocation.from(1162, 118, 18), WLocation.from(1162, 118.5, 16), -180, 0, -1, null),
                BumperObject.of(20, WLocation.from(1165, 119, 18), WLocation.from(1165, 119.5, 16), -180, 0, -1, null),
                BumperObject.of(20, WLocation.from(1168, 120, 18), WLocation.from(1168, 120.5, 16), -180, 0, -1, null),
                BumperObject.of(20, WLocation.from(1171, 120, 18), WLocation.from(1171, 120.5, 16), -180, 0, -1, null),
                BumperObject.of(20, WLocation.from(1175, 119, 18), WLocation.from(1175, 119.5, 16), -180, 0, -1, null),
                BumperObject.of(20, WLocation.from(1179, 119, 18), WLocation.from(1179, 119.5, 16), -180, 0, -1, null),
                BumperObject.of(20, WLocation.from(1182, 120, 18), WLocation.from(1182, 120.5, 16), -180, 0, -1, null),
                BumperObject.of(20, WLocation.from(1186, 120, 18), WLocation.from(1186, 120.5, 16), -180, 0, -1, null),
                BumperObject.of(20, WLocation.from(1190, 120, 18), WLocation.from(1190, 120.5, 16), -180, 0, -1, null)
        ), 60);


        FanWall fanWall = new FanWall(Arrays.asList(
                FanObject.of(Cuboid.from(1169, 114, -40, 1162, 116, -38), FanRotation.NEG_X, 0, 20, 1),
                FanObject.of(Cuboid.from(1169, 114, -46, 1162, 116, -44), FanRotation.NEG_X, 40, 60, 1),
                FanObject.of(Cuboid.from(1169, 114, -52, 1162, 116, -50), FanRotation.NEG_X, 0, 20, 1),
                FanObject.of(Cuboid.from(1169, 114, -58, 1162, 116, -56), FanRotation.NEG_X, 40, 60, 1),
                FanObject.of(Cuboid.from(1169, 114, -64, 1162, 116, -62), FanRotation.NEG_X, 0, 20, 1),
                FanObject.of(Cuboid.from(1169, 114, -70, 1162, 116, -68), FanRotation.NEG_X, 40, 60, 1),
                FanObject.of(Cuboid.from(1169, 114, -76, 1162, 116, -74), FanRotation.NEG_X, 0, 20, 1)
        ), 80);

        DissapearingPlatforms dissapearingPlatforms = new DissapearingPlatforms("map-1-dissapearing-platforms", 65);

        obstacles.add(dissapearingPlatforms);
        obstacles.add(bumperWall);
        obstacles.add(fanWall);

        obstacles.forEach(Obstacle::load);
    }
}
