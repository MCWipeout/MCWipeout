package org.stormdev.mcwipeout.frame.io.impl;
/*
  Created by Stormbits at 10/25/2023
*/

import org.stormdev.mcwipeout.Wipeout;

import java.util.logging.Level;

public class Error {

    public static void execute(Wipeout plugin, Exception ex) {
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }

    public static void close(Wipeout plugin, Exception ex) {
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
