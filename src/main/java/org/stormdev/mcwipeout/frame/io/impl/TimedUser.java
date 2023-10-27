package org.stormdev.mcwipeout.frame.io.impl;
/*
  Created by Stormbits at 10/27/2023
*/

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class TimedUser {

    private UUID uuid;
    private long map1;
    private long map2;
    private long map3;
    private long map4;
}
