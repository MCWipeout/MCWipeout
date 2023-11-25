package org.stormdev.mcwipeout.frame.io.impl;/*
  Created by Stormbits at 11/25/2023
*/

import lombok.Getter;

@Getter
public enum Credentials {

    GSU_MACHINE("jdbc:mysql://node.gsumc.live:3306/s28_mcwipeout", "wipeout_players", "u28_NJOG2Nkj9C", "Di!Bjccn@4wp@QD!C44I+Uks"),
    MCPROHOSTING("jdbc:mysql://66.85.144.162:3306/server_1091109_7c220a92", "wipeout_players", "server_1091109", "Y94c6qF2aK96uTsW2cG");

    private String host, database, user, password;

    Credentials(String host, String database, String user, String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
    }
}
