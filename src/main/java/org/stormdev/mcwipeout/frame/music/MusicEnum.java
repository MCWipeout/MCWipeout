package org.stormdev.mcwipeout.frame.music;/*
  Created by Stormbits at 10/25/2023
*/

import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.stormdev.mcwipeout.Wipeout;

public enum MusicEnum {


    MAP_1_LOOP("wipeout:mcw.music.map1loop", 84368, null), // 1:25:368

    MAP_2_LOOP("wipeout:mcw.music.map2loop", 87033, null), // 1:28:033
    MAP_3_LOOP("wipeout:mcw.music.map3loop", 73710, null), // 1:14:710

    MAP_1_INTRO("wipeout:mcw.music.map1intro", 97717, MAP_1_LOOP), // 1:38:717

    MAP_2_INTRO("wipeout:mcw.music.map2intro", 88051, MAP_2_LOOP), // 1:29:051

    MAP_3_INTRO("wipeout:mcw.music.map3intro", 75042, MAP_3_LOOP); // 1:16:042


    @Getter
    String url;

    @Getter
    long length;

    @Getter
    MusicEnum loop;

    MusicEnum(String url, long length, MusicEnum loop) {
        this.url = url;
        this.length = length;
        this.loop = loop;
    }

    public void send(Player player) {
        Wipeout.get().getAdventure().player(player).playSound(Sound.sound(Key.key(this.url), Sound.Source.AMBIENT, 1.0f, 1.0f));
    }

    public void stop(Player player) {
        player.stopAllSounds();
    }
}
