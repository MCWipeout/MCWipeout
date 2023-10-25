package org.stormdev.mcwipeout.frame.io.impl;
/*
  Created by Stormbits at 10/25/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.utils.Utils;

import java.util.UUID;

public class WipeoutResult {

    public static void sendPlayerTimeMessage(CommandSender sender, UUID uuid, int map) {
        Result result = new Result() {
            @Override
            public void done(long time) {
                sender.sendMessage("§a" + Bukkit.getOfflinePlayer(uuid).getName() + "§7's time on map §a" + map + "§7 is §a" + time + " ( " + Utils.formatTime(time) + " ) §7!");
            }
        };
        getTime(uuid, map, result);
    }

    private static void getTime(UUID uuid, int map, Result result) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Wipeout.get(), () -> {
            long time = Wipeout.get().getWipeoutDatabase().getTime(uuid, map);
            Bukkit.getServer().getScheduler().runTask(Wipeout.get(), () -> result.done(time));
        });
    }

    public static void updatePlayerTimeInDatabase(UUID uuid, int map, long currentTime) {
        Bukkit.broadcastMessage("test");
        Result result = new Result() {
            @Override
            public void done(long time) {
                if (currentTime < time || time == 0) {
                    Bukkit.getServer().getScheduler().runTaskAsynchronously(Wipeout.get(), ()
                            -> Wipeout.get().getWipeoutDatabase().setTime(uuid, map, currentTime));

                    Wipeout.get().getLogger().info("Saved new time to database!");
                }
            }
        };

        getTime(uuid, map, result);
    }
}
