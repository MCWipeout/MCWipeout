package org.stormdev.mcwipeout.frame.leaderboards;
/*
  Created by Stormbits at 10/27/2023
*/

import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.io.impl.TimedTeam;
import org.stormdev.mcwipeout.frame.io.impl.TimedUser;
import org.stormdev.utils.Pair;

import java.util.*;

public class LeaderboardManager {

    private Wipeout plugin;

    public LeaderboardManager(Wipeout plugin) {
        this.plugin = plugin;
    }

    public List<Pair<UUID, Long>> getAllSortedScores(String mapName) {

        Map<UUID, Long> playerScores = new HashMap<>();

        for (TimedUser timedUser : plugin.getWipeoutDatabase().getTimedUsers().values()) {
            switch (mapName) {
                case "1":
                    if (timedUser.getMap1() != 0)
                        playerScores.put(timedUser.getUuid(), timedUser.getMap1());
                    break;
                case "2":
                    if (timedUser.getMap2() != 0)
                        playerScores.put(timedUser.getUuid(), timedUser.getMap2());
                    break;
                case "3":
                    if (timedUser.getMap3() != 0)
                        playerScores.put(timedUser.getUuid(), timedUser.getMap3());
                    break;
                case "4":
                    if (timedUser.getMap4() != 0)
                        playerScores.put(timedUser.getUuid(), timedUser.getMap4());
                    break;
            }
        }

        List<Map.Entry<UUID, Long>> sortedScores = new ArrayList<>(playerScores.entrySet());
        sortedScores.sort(Map.Entry.comparingByValue(Comparator.naturalOrder()));

        List<Pair<UUID, Long>> pairList = new ArrayList<>();
        for (Map.Entry<UUID, Long> sortedScore : sortedScores) {
            pairList.add(Pair.of(sortedScore.getKey(), sortedScore.getValue()));
        }

        return pairList;
    }

    public List<Pair<String, Long>> getAllSortedScoresTeams(String mapName) {

        Map<String, Long> playerScores = new HashMap<>();

        for (TimedTeam timedUser : plugin.getWipeoutDatabase().getTimedTeams().values()) {
            switch (mapName) {
                case "1":
                    if (timedUser.getMap_1() != 0)
                        playerScores.put(timedUser.getTeamId(), timedUser.getMap_1());
                    break;
                case "2":
                    if (timedUser.getMap_2() != 0)
                        playerScores.put(timedUser.getTeamId(), timedUser.getMap_2());
                    break;
                case "3":
                    if (timedUser.getMap_3() != 0)
                        playerScores.put(timedUser.getTeamId(), timedUser.getMap_3());
                    break;
                case "4":
                    if (timedUser.getMap_4() != 0)
                        playerScores.put(timedUser.getTeamId(), timedUser.getMap_4());
                    break;
            }
        }

        List<Map.Entry<String, Long>> sortedScores = new ArrayList<>(playerScores.entrySet());
        sortedScores.sort(Map.Entry.comparingByValue(Comparator.naturalOrder()));

        List<Pair<String, Long>> pairList = new ArrayList<>();
        for (Map.Entry<String, Long> sortedScore : sortedScores) {
            pairList.add(Pair.of(sortedScore.getKey(), sortedScore.getValue()));
        }

        return pairList;
    }
}
