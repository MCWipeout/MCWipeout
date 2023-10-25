package org.stormdev.mcwipeout.frame.music;
/*
  Created by Stormbits at 10/25/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.stormdev.mcwipeout.Wipeout;

public class MusicTask implements Runnable {

    private long timeStarted;
    private MusicEnum currentMusic;

    private int taskID;

    public MusicTask(MusicEnum musicEnum) {

        currentMusic = musicEnum;

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(musicEnum::send);
                timeStarted = System.currentTimeMillis();
            }
        }.runTaskLater(Wipeout.get(), 30L);

        taskID = Bukkit.getScheduler().runTaskTimer(Wipeout.get(), this, 30L, 0L).getTaskId();
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() - timeStarted >= currentMusic.getLength()) {
            MusicEnum old = currentMusic;
            if (old.getLoop() != null) {
                currentMusic = old.getLoop();
            }

            Bukkit.getOnlinePlayers().forEach(currentMusic::send);
            timeStarted = System.currentTimeMillis();
        }
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(taskID);

        Bukkit.getOnlinePlayers().forEach(currentMusic::stop);
    }
}
