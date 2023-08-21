package org.stormdev.mcwipeout.frame.team;

import org.stormdev.mcwipeout.Wipeout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TeamManager {

    private Wipeout plugin;

    private List<Team> teamList;

    private List<WipeoutPlayer> wipeoutPlayers;

    public TeamManager(Wipeout plugin) {
        this.plugin = plugin;

        teamList = new ArrayList<>();
        wipeoutPlayers = new ArrayList<>();
    }

    public Team addTeam(String id) {
        Team team = new Team(id, new ArrayList<>());

        teamList.add(team);

        return team;
    }

    public boolean exists(String id) {
        for (Team team : teamList) {
            if (team.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public Team getTeamFromUUID(UUID uuid) {
        for (Team team : teamList) {
            if (team.getUUIDMembers().contains(uuid)) {
                return team;
            }
        }
        return null;
    }

    public Optional<Team> getTeam(String id) {
        return teamList.stream().filter(team -> team.getId().equalsIgnoreCase(id)).findAny();
    }

    public List<String> getTeamNames() {
        List<String> names = new ArrayList<>(teamList.stream().map(Team::getId).toList());
        return names;
    }

    public void addTeam(Team team) {
        teamList.add(team);
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public List<WipeoutPlayer> getWipeoutPlayers() {
        return wipeoutPlayers;
    }

    public WipeoutPlayer fromUUID(UUID uuid) {
        for (WipeoutPlayer wipeoutPlayer : wipeoutPlayers) {
            if (wipeoutPlayer.getUuid().equals(uuid)) {
                return wipeoutPlayer;
            }
        }
        return null;
    }
}
