package org.stormdev.mcwipeout.frame.io.impl;
/*
  Created by Stormbits at 10/30/2023
*/

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TimedTeam {

    private String teamId;
    private long map_1;
    private long map_2;
    private long map_3;
    private long map_4;
}
