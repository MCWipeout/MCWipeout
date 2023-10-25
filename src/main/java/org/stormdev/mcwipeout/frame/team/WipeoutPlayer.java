package org.stormdev.mcwipeout.frame.team;
/*
  Created by Stormbits at 4/13/2023
*/

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.stormdev.mcwipeout.frame.board.IBoard;
import org.stormdev.mcwipeout.frame.board.IBoardType;

import java.util.UUID;

public class WipeoutPlayer {

    @Getter
    @Setter
    private UUID uuid;
    @Getter
    @Setter
    private boolean visiblePlayers;

    @Getter
    @Setter
    private IBoard board;

    public WipeoutPlayer(UUID uuid, boolean visiblePlayers) {
        this.uuid = uuid;
        this.visiblePlayers = visiblePlayers;
        this.board = null;
    }

    public Player asPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
