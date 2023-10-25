package org.stormdev.mcwipeout;
/*
  Created by Stormbits at 9/13/2023
*/

public class WipeoutAPI {

    private static WipeoutAPI api;
    private Wipeout plugin;

    public static WipeoutAPI getInstance() {
        return api;
    }

    public WipeoutAPI(Wipeout wipeout) {
        api = this;
        plugin = wipeout;
    }

    public boolean isMapRunning() {
        if (plugin.getGameManager().getActiveMap() != null) {
            return plugin.getGameManager().getActiveMap().getObstacles().get(0).isEnabled();
        }

        return false;
    }
}
