package org.stormdev.mcwipeout.maps;
/*
  Created by Stormbits at 11/2/2023
*/

import org.bukkit.Bukkit;
import org.stormdev.mcwipeout.frame.game.CheckPoint;
import org.stormdev.mcwipeout.frame.game.Map;
import org.stormdev.mcwipeout.frame.obstacles.redballs.RedBallsObstacle;
import org.stormdev.mcwipeout.frame.team.WPoint;

public class MapFourModule extends Map {

    public MapFourModule() {
        super("Map 4");

        //setSpawnPoint(new CheckPoint("", new WPoint(4804.5, 106, 111.5), -90.0F, 0F, ObstacleRegion.RED_BALLS));
        setFinish(new CheckPoint("map_4_finish", new WPoint(4804.5, 106, 111.5), -90.0F, 0F, null));
    }

    @Override
    protected void setupCheckpoints() {

    }

    @Override
    protected void setupObstacles() {
        RedBallsObstacle redBallsObstacle = new RedBallsObstacle(240, 240);


        obstacles.add(redBallsObstacle);
    }
}
