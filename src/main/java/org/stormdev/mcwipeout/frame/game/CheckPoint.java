package org.stormdev.mcwipeout.frame.game;
/*
  Created by Stormbits at 2/12/2023
*/

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.stormdev.mcwipeout.frame.team.WPoint;

public class CheckPoint {

    @Getter
    private String region;

    @Getter
    private WPoint point;

    @Getter
    private float yaw;
    @Getter
    private float pitch;

    public CheckPoint(String region, WPoint point, float yaw, float pitch) {
        this.region = region.toLowerCase();
        this.point = point;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void reset(Player player) {
        Location location = point.toLocation();
        location.setPitch(pitch);
        location.setYaw(yaw);

        player.teleport(location);
    }
}
