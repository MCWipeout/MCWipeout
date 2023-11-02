package org.stormdev.mcwipeout.frame.team;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.stormdev.mcwipeout.frame.game.CheckPoint;

import java.util.*;

@Data
public class Team {

    private String id;
    private List<UUID> members;
    private String color;

    private transient List<UUID> finishedMembers;

    private transient Map<UUID, CheckPoint> checkPointMap;

    public Team(String id, List<UUID> members) {
        this.id = id;
        this.members = members;
        this.color = "#54504F";
        this.checkPointMap = new HashMap<>();
        this.finishedMembers = new ArrayList<>();
    }

    public boolean containsPlayer(OfflinePlayer player) {
        return members.stream().anyMatch(uuid -> uuid.equals(player.getUniqueId()));
    }

    public void add(OfflinePlayer player) {
        members.add(player.getUniqueId());
    }

    public void remove(OfflinePlayer player) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).equals(player.getUniqueId())) {
                members.remove(player.getUniqueId());
            }
        }
    }

    public void sendTeamMessage(String text) {
        for (UUID uuid : members) {
            Player target = Bukkit.getPlayer(uuid);
            if (target != null) {
                target.sendMessage(text);
            }
        }
    }

    public void finish(Player player, CheckPoint checkPoint) {
        for (UUID uuid : members) {
            checkPointMap.put(player.getUniqueId(), checkPoint);
        }

        player.setGameMode(GameMode.SPECTATOR);
    }

    public List<UUID> getUUIDMembers() {

        return members;
    }
}
