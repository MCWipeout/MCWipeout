package org.stormdev.mcwipeout.frame.bundling;
/*
  Created by Stormbits at 11/8/2023
*/

import net.minecraft.network.protocol.Packet;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PacketTask extends BukkitRunnable {

    private PacketBundler bundler;

    public PacketTask(PacketBundler bundler) {
        this.bundler = bundler;
    }

    @Override
    public void run() {
        if (bundler.getPackets().isEmpty()) return;

        Iterator<Map.Entry<UUID, List<Packet>>> iterator = bundler.getPackets().entrySet().iterator();

        try {
            while (iterator.hasNext()) {
                Map.Entry<UUID, List<Packet>> entry = iterator.next();
                if (entry.getValue().isEmpty()) {
                    iterator.remove();
                    continue;
                }

                bundler.sendPackets(entry.getKey());
                iterator.remove();
            }
        } catch (Exception e) {
            //do nothing
        }
    }
}
