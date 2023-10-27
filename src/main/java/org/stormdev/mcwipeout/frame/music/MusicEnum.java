package org.stormdev.mcwipeout.frame.music;/*
  Created by Stormbits at 10/25/2023
*/

import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.stormdev.mcwipeout.Wipeout;

public enum MusicEnum {


    MAP_1_LOOP("wipeout:mcw.music.map1loop", 85368, null, 0), // 1:25:368

    MAP_2_LOOP("wipeout:mcw.music.map2loop", 87033, null, 1000), // 1:28:033
    MAP_3_LOOP("wipeout:mcw.music.map3loop", 74710, null, 500), // 1:14:710

    MAP_1_INTRO("wipeout:mcw.music.map1intro", 98717, MAP_1_LOOP, 0), // 1:38:717

    MAP_2_INTRO("wipeout:mcw.music.map2intro", 89051, MAP_2_LOOP, 0), // 1:29:051

    MAP_3_INTRO("wipeout:mcw.music.map3intro", 76042, MAP_3_LOOP, 0); // 1:16:042


    @Getter
    String url;

    @Getter
    long length;

    @Getter
    MusicEnum loop;

    @Getter
            long timeAfterLoop;

    MusicEnum(String url, long length, MusicEnum loop, long timeAfterLoop) {
        this.url = url;
        this.length = length;
        this.loop = loop;
        this.timeAfterLoop = timeAfterLoop;
    }

    public void send(Player player) {
        Wipeout.get().getAdventure().player(player).playSound(Sound.sound(Key.key(this.url), Sound.Source.AMBIENT, 1.0f, 1.0f));
    }

    public void stop(Player player) {
        player.stopAllSounds();
    }
}
