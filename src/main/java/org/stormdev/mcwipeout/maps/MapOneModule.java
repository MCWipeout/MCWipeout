package org.stormdev.mcwipeout.maps;
/*
  Created by Stormbits at 8/21/2023
*/

import org.bukkit.Material;
import org.stormdev.mcwipeout.frame.game.CheckPoint;
import org.stormdev.mcwipeout.frame.game.Map;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.BumperObject;
import org.stormdev.mcwipeout.frame.obstacles.bumpers.BumperWall;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanObject;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanRotation;
import org.stormdev.mcwipeout.frame.obstacles.fans.FanWall;
import org.stormdev.mcwipeout.frame.obstacles.ice.FreezingIce;
import org.stormdev.mcwipeout.frame.obstacles.patterns.PatternPlatforms;
import org.stormdev.mcwipeout.frame.obstacles.platforms.DissapearingPlatforms;
import org.stormdev.mcwipeout.frame.obstacles.redballs.RedBallsObstacle;
import org.stormdev.mcwipeout.frame.obstacles.water.WaterSlide;
import org.stormdev.mcwipeout.frame.team.WPoint;
import org.stormdev.mcwipeout.utils.helpers.Cuboid;
import org.stormdev.mcwipeout.utils.helpers.WLocation;

import java.util.Arrays;
import java.util.List;

public class MapOneModule extends Map {

    public MapOneModule() {
        super();

        setSpawnPoint(new CheckPoint("", new WPoint(1000.5, 128, 0.5), -90.0F, 0F));
        setFinish(new CheckPoint("map_1_finish", new WPoint(1000.5, 128, 0.5), -90.0F, 0F));
    }


    @Override
    protected void setupCheckpoints() {
        checkPoints.add(new CheckPoint("map_1_cp1", WPoint.from(1092.5, 114, 0.5), -90.0F, 0F));
        checkPoints.add(new CheckPoint("map_1_cp2", WPoint.from(1136.5, 115, 12.5), -70.0F, -8F));
        checkPoints.add(new CheckPoint("map_1_cp3", WPoint.from(1189.5, 114, -32.5), 90f, 0F));
        checkPoints.add(new CheckPoint("map_1_cp4", WPoint.from(1119.5, 114, -92.5), 90.0F, 0F));
        checkPoints.add(new CheckPoint("map_1_cp5", WPoint.from(1039.5, 114, -70.5), 180.0F, 0F));
        checkPoints.add(new CheckPoint("map_1_cp6", WPoint.from(1156.5, 114, -135.5), -90.0F, 0F));
        checkPoints.add(new CheckPoint("map_1_cp7", WPoint.from(1204.5, 115, -143.5), 180.0F, 0F));
        checkPoints.add(new CheckPoint("map_1_cp8", WPoint.from(1184.5, 134, -156.5), 90F, 0F));
    }

    @Override
    protected void setupObstacles() {
        BumperWall bumperWall = new BumperWall(List.of(
                BumperObject.of(10, WLocation.from(1141, 116, 18), WLocation.from(1141, 116.5, 16), -180, 0, -1),
                BumperObject.of(30, WLocation.from(1144, 117, 18), WLocation.from(1144, 117.5, 16), -180, 0, -1),
                BumperObject.of(20, WLocation.from(1147, 118, 18), WLocation.from(1147, 118.5, 16), -180, 0, -1),
                BumperObject.of(50, WLocation.from(1151, 117, 18), WLocation.from(1151, 117.5, 16), -180, 0, -1),
                BumperObject.of(60, WLocation.from(1155, 117, 18), WLocation.from(1155, 117.5, 16), -180, 0, -1),
                BumperObject.of(45, WLocation.from(1159, 117, 18), WLocation.from(1159, 117.5, 16), -180, 0, -1),
                BumperObject.of(35, WLocation.from(1162, 118, 18), WLocation.from(1162, 118.5, 16), -180, 0, -1),
                BumperObject.of(20, WLocation.from(1165, 119, 18), WLocation.from(1165, 119.5, 16), -180, 0, -1),
                BumperObject.of(10, WLocation.from(1168, 120, 18), WLocation.from(1168, 120.5, 16), -180, 0, -1),
                BumperObject.of(0, WLocation.from(1171, 120, 18), WLocation.from(1171, 120.5, 16), -180, 0, -1),
                BumperObject.of(0, WLocation.from(1175, 119, 18), WLocation.from(1175, 119.5, 16), -180, 0, -1),
                BumperObject.of(10, WLocation.from(1179, 119, 18), WLocation.from(1179, 119.5, 16), -180, 0, -1),
                BumperObject.of(15, WLocation.from(1182, 120, 18), WLocation.from(1182, 120.5, 16), -180, 0, -1),
                BumperObject.of(25, WLocation.from(1186, 120, 18), WLocation.from(1186, 120.5, 16), -180, 0, -1),
                BumperObject.of(40, WLocation.from(1190, 120, 18), WLocation.from(1190, 120.5, 16), -180, 0, -1)
        ), 65);


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

        WaterSlide waterSlide = new WaterSlide("map-1-slide", 1, 20);

        RedBallsObstacle redBallsObstacle = new RedBallsObstacle(240, 240);

        FreezingIce freezingIce = new FreezingIce("map-1-ice");

        BumperWall bumperSpiral = new BumperWall(Arrays.asList(
                BumperObject.of(10, WLocation.from(1212, 117, -158), WLocation.from(1210, 117.5, -158), 90, -1, 0),
                BumperObject.of(30, WLocation.from(1208, 119, -164), WLocation.from(1208, 119.5, -162), 0, 0, 1),
                BumperObject.of(20, WLocation.from(1206, 120, -165), WLocation.from(1206, 120.5, -163), 0, 0, 1),
                BumperObject.of(50, WLocation.from(1203, 121, -165), WLocation.from(1203, 121.5, -163), 0, 0, 1),
                BumperObject.of(60, WLocation.from(1197, 123, -161), WLocation.from(1199, 123.5, -161), -90, 1, 0),
                BumperObject.of(35, WLocation.from(1196, 124, -159), WLocation.from(1198, 124.5, -159), -90, 1, 0),
                BumperObject.of(20, WLocation.from(1196, 125, -156), WLocation.from(1198, 125.5, -156), -90, 1, 0),
                BumperObject.of(10, WLocation.from(1200, 127, -150), WLocation.from(1200, 127.5, -152), -180, 0, -1),
                BumperObject.of(0, WLocation.from(1202, 128, -149), WLocation.from(1202, 128.5, -151), -180, 0, -1),
                BumperObject.of(0, WLocation.from(1205, 129, -149), WLocation.from(1205, 129.5, -151), -180, 0, -1),
                BumperObject.of(15, WLocation.from(1211, 131, -153), WLocation.from(1209, 131.5, -153), 90, -1, 0),
                BumperObject.of(25, WLocation.from(1212, 132, -155), WLocation.from(1210, 132.5, -155), 90, -1, 0)
        ), 65);


        PatternPlatforms patternPlatforms = new PatternPlatforms(200, 20, 240, Arrays.asList(Material.GREEN_CONCRETE, Material.RED_CONCRETE, Material.BLUE_CONCRETE), "map-1-all-patterns", "map-1-moving", "map-1-pattern-1", "map-1-pattern-2");

        obstacles.add(dissapearingPlatforms);
        obstacles.add(bumperWall);
        obstacles.add(fanWall);
        obstacles.add(waterSlide);
        obstacles.add(redBallsObstacle);
        obstacles.add(bumperSpiral);
        obstacles.add(freezingIce);
        obstacles.add(patternPlatforms);

        obstacles.forEach(Obstacle::load);
    }
}
