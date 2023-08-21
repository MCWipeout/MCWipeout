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
    private List<WipeoutPlayer> members;
    private String color;

    private transient List<UUID> finishedMembers;

    private transient Map<UUID, CheckPoint> checkPointMap;

    public Team(String id, List<WipeoutPlayer> members) {
        this.id = id;
        this.members = members;
        this.color = "#54504F";
        this.checkPointMap = new HashMap<>();
        this.finishedMembers = new ArrayList<>();
    }

    public boolean containsPlayer(OfflinePlayer player) {
        return members.stream().anyMatch(uuid -> uuid.getUuid().equals(player.getUniqueId()));
    }

    public void add(OfflinePlayer player) {
        members.add(new WipeoutPlayer(player.getUniqueId(), false));
    }

    public void remove(OfflinePlayer player) {
        for (int i = 0; i < members.size(); i++) {
            WipeoutPlayer wipeoutPlayer = members.get(i);
            if (wipeoutPlayer.getUuid().equals(player.getUniqueId())) {
                members.remove(wipeoutPlayer);
            }
        }
    }

    public void sendTeamMessage(String text) {
        for (WipeoutPlayer wipeoutPlayer : members) {
            Player target = Bukkit.getPlayer(wipeoutPlayer.getUuid());
            if (target != null) {
                target.sendMessage(text);
            }
        }
    }

    public void finish(Player player, CheckPoint checkPoint) {
        for (WipeoutPlayer wipeoutPlayer : members) {
            checkPointMap.put(wipeoutPlayer.getUuid(), checkPoint);
        }

        player.setGameMode(GameMode.SPECTATOR);
    }

    public List<UUID> getUUIDMembers() {
        List<UUID> array = new ArrayList<>();
        members.forEach(member -> array.add(member.getUuid()));

        return array;
    }
}
