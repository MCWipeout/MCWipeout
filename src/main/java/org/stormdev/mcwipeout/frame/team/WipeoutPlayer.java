package org.stormdev.mcwipeout.frame.team;
/*
  Created by Stormbits at 4/13/2023
*/

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class WipeoutPlayer {

    @Getter
    @Setter
    private UUID uuid;
    @Getter
    @Setter
    private boolean visiblePlayers;

    public WipeoutPlayer(UUID uuid, boolean visiblePlayers) {
        this.uuid = uuid;
        this.visiblePlayers = visiblePlayers;
    }
}
