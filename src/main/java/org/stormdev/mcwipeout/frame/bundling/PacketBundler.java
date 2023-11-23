package org.stormdev.mcwipeout.frame.bundling;

/*
  Created by Stormbits at 11/8/2023
*/

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import lombok.Getter;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.stormdev.mcwipeout.Wipeout;

import java.util.*;

public class PacketBundler {

    @Getter
    private Map<UUID, List<Packet>> packets;

    private Wipeout plugin;

    public PacketBundler(Wipeout plugin) {
        this.plugin = plugin;

        this.packets = new HashMap<>();

        new PacketTask(this).runTaskTimerAsynchronously(plugin, 0L, 0L);
    }

    public void addPackets(Player player, Packet<?>... packets) {
        List<Packet> playerPackets = this.packets.getOrDefault(player.getUniqueId(), new ArrayList<>());
        playerPackets.addAll(Arrays.asList(packets));
        this.packets.put(player.getUniqueId(), playerPackets);
    }

    public void sendPackets(UUID uuid) {
        if (!packets.containsKey(uuid)) return;

        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            return;
        }

        ProtocolManager connection = plugin.getProtocolManager();

        List<PacketContainer> containeredPackets = new ArrayList<>();

        for (Packet<?> packet : packets.get(uuid)) {
            PacketContainer container = PacketContainer.fromPacket(packet);
            containeredPackets.add(container);
        }

        PacketContainer container = new PacketContainer(PacketType.Play.Server.BUNDLE);
        container.getPacketBundles().write(0, containeredPackets);

        connection.sendServerPacket(player, container);
    }
}
