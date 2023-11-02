package org.stormdev.mcwipeout.frame.io.impl;
/*
  Created by Stormbits at 10/25/2023
*/

import lombok.Getter;
import net.minecraft.world.entity.schedule.Keyframe;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.io.IWipeoutDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class WipeoutDatabase extends IWipeoutDatabase {

    @Getter
    private Map<UUID, TimedUser> timedUsers;

    @Getter
    private Map<String, TimedTeam> timedTeams;

    String dbname;

    public WipeoutDatabase(Wipeout instance) {
        super(instance);
        dbname = "wipeout_players";

        this.timedUsers = new HashMap<>();
        this.timedTeams = new HashMap<>();

        instance.getServer().getScheduler().runTaskLaterAsynchronously(instance, this::loadPlayerData, 20L);
        instance.getServer().getScheduler().runTaskLaterAsynchronously(instance, this::loadTeamsData, 20L);

        for (Player player : Bukkit.getOnlinePlayers()) {
            addPlayer(player.getUniqueId());
        }
    }

    private void loadTeamsData() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + "wipeout_teams" + ";");

            rs = ps.executeQuery();
            while (rs.next()) {
                String teamId = rs.getString("teamId");
                long map1 = rs.getLong("map_1_time");
                long map2 = rs.getLong("map_2_time");
                long map3 = rs.getLong("map_3_time");
                long map4 = rs.getLong("map_4_time");

                timedTeams.put(teamId, new TimedTeam(teamId, map1, map2, map3, map4));
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }

        plugin.getLogger().log(Level.INFO, "Loaded " + timedTeams.size() + " timed teams");
    }

    private void loadPlayerData() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + ";");

            rs = ps.executeQuery();
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("player"));
                long map1 = rs.getLong("map_1_time");
                long map2 = rs.getLong("map_2_time");
                long map3 = rs.getLong("map_3_time");
                long map4 = rs.getLong("map_4_time");

                timedUsers.put(uuid, new TimedUser(uuid, map1, map2, map3, map4));
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }

        plugin.getLogger().log(Level.INFO, "Loaded " + timedUsers.size() + " timed users");
    }

    public String SQLCreateTable = "CREATE TABLE IF NOT EXISTS wipeout_players (" +
            "`player` varchar(36) NOT NULL," +
            "`map_1_time` BIGINT(32)," +
            "`map_2_time` BIGINT(32)," +
            "`map_3_time` BIGINT(32)," +
            "`map_4_time` BIGINT(32)," +
            "PRIMARY KEY (`player`)" +
            ");";

    public String SQLCreateTableTeams = "CREATE TABLE IF NOT EXISTS wipeout_teams (" +
            "`teamId` varchar(32) NOT NULL," +
            "`map_1_time` BIGINT(32)," +
            "`map_2_time` BIGINT(32)," +
            "`map_3_time` BIGINT(32)," +
            "`map_4_time` BIGINT(32)," +
            "PRIMARY KEY (`teamId`)" +
            ");";


    public Connection getSQLConnection() {

        final String username = "server_1091109";
        final String password = "Y94c6qF2aK96uTsW2cG";
        final String url = "jdbc:mysql://66.85.144.162:3306/server_1091109_7c220a92";

        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            PreparedStatement statementPlayers = connection.prepareStatement(SQLCreateTable);
            PreparedStatement statementTeams = connection.prepareStatement(SQLCreateTableTeams);

            statementPlayers.executeUpdate();
            statementTeams.executeUpdate();

            statementPlayers.close();
            statementTeams.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }

    public void addPlayer(UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("INSERT IGNORE INTO " + table + " (player,map_1_time,map_2_time,map_3_time,map_4_time) VALUES(?,?,?,?,?)");

            ps.setString(1, uuid.toString());
            ps.setLong(2, 0L);
            ps.setLong(3, 0L);
            ps.setLong(4, 0L);
            ps.setLong(5, 0L);

            ps.executeUpdate();

            timedUsers.put(uuid, new TimedUser(uuid, getTime(uuid, 1), getTime(uuid, 2), getTime(uuid, 3), getTime(uuid, 4)));
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    public void insertTeam(String teamId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("INSERT IGNORE INTO " + "wipeout_teams" + " (teamId,map_1_time,map_2_time,map_3_time,map_4_time) VALUES(?,?,?,?,?)");

            ps.setString(1, teamId);
            ps.setLong(2, 0L);
            ps.setLong(3, 0L);
            ps.setLong(4, 0L);
            ps.setLong(5, 0L);

            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    public Long getTime(UUID player, int map) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '" + player.toString() + "';");

            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("player").equalsIgnoreCase(player.toString())) {
                    return (long) rs.getInt("map_" + map + "_time");
                }
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return 0L;
    }
    public Long getTime(String teamId, int map) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + "wipeout_teams" + " WHERE teamId = '" + teamId + "';");

            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("teamId").equalsIgnoreCase(teamId)) {
                    return (long) rs.getInt("map_" + map + "_time");
                }
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return 0L;
    }

    public void setTime(String teamId, int map, long time) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            plugin.getLogger().info("map: " + map);
            ps = conn.prepareStatement("UPDATE " + "wipeout_teams" + " SET map_" + map + "_time = ? WHERE teamId = ?");

            ps.setLong(1, time);
            ps.setString(2, teamId);

            ps.executeUpdate();

            timedTeams.put(teamId, new TimedTeam(teamId, getTime(teamId, 1), getTime(teamId, 2), getTime(teamId, 3), getTime(teamId, 4)));
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    public void setTime(UUID player, int map, long time) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            plugin.getLogger().info("map: " + map);
            ps = conn.prepareStatement("UPDATE " + table + " SET map_" + map + "_time = ? WHERE player = ?");

            ps.setLong(1, time);
            ps.setString(2, player.toString());

            ps.executeUpdate();

            timedUsers.put(player, new TimedUser(player, getTime(player, 1), getTime(player, 2), getTime(player, 3), getTime(player, 4)));
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }
}
