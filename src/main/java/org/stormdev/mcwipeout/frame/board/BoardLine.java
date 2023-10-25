package org.stormdev.mcwipeout.frame.board;
/*
  Created by Stormbits at 10/24/2023
*/

import lombok.Getter;
import lombok.Setter;
import org.bukkit.scoreboard.Team;

public class BoardLine {

    @Getter @Setter private String prefix, suffix;

    @Getter @Setter private Team team;

    public BoardLine(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }
}
