package org.stormdev.mcwipeout.frame.io.sheets;
/*
  Created by Stormbits at 10/25/2023
*/

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.team.Team;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SheetsManager {

    private List<SheetTeam> sheetTeamList;

    public SheetsManager(Wipeout plugin) {
        this.sheetTeamList = new ArrayList<>();

        SheetResult result = new SheetResult() {
            @Override
            public void done(List<SheetTeam> teams) {
                sheetTeamList = teams;

                for (SheetTeam team : teams) {
                    if (team.getShortHand().equals("Shorthand")) continue;

                    if (plugin.getTeamManager().getTeam(team.getShortHand()).isEmpty()) {
                        Team playerTeam = plugin.getTeamManager().addTeam(team.getShortHand());
                        playerTeam.setColor(team.getColor());
                        for (String playerName : team.getPlayerNames()) {
                            if (playerName.equalsIgnoreCase("CEST") || playerName.equalsIgnoreCase("PST")) continue;
                            try {
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                                if (offlinePlayer == null) {
                                    continue;
                                }

                                playerTeam.add(offlinePlayer);
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        };

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {

            List<SheetTeam> list = new ArrayList<>();

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://docs.google.com/spreadsheets/d/10oEuhGCOPmjdPG1uu3JTpSkxqBZac5AKlXhS1si3XlY/export?format=csv").openConnection().getInputStream()));

                String line;
                int counter = 0;
                while ((line = reader.readLine()) != null) {
                    counter++;
                    String[] content;
                    try {
                        content = line.split(",");
                        if (content.length < 24) {
                            continue;
                        }
                        String shortHand = content[19];

                        if (shortHand.equalsIgnoreCase("4")) {
                            continue;
                        }

                        String color = content[1];
                        List<String> playerNames = getPlayerNames(content);

                        list.add(new SheetTeam(shortHand, color, playerNames));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                plugin.getLogger().info("Loaded " + list.size() + " teams from sheets.");

                result.done(list);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    @NotNull
    private static List<String> getPlayerNames(String[] content) {
        String player1 = content[5];
        String player2 = content[9];
        String player3 = content[13];

        List<String> playerNames = new ArrayList<>();
        if (!player1.isEmpty()) {
            playerNames.add(player1);
        }
        if (!player2.isEmpty()) {
            playerNames.add(player2);
        }
        if (!player3.isEmpty()) {
            playerNames.add(player3);
        }
        return playerNames;
    }
}
