package org.stormdev.mcwipeout.frame.io.impl;
/*
  Created by Stormbits at 10/25/2023
*/

import lombok.Getter;
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

    String dbname;

    public WipeoutDatabase(Wipeout instance) {
        super(instance);
        dbname = "wipeout_players";

        this.timedUsers = new HashMap<>();

        loadData();
    }

    private void loadData() {
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

    public String SQLiteCreateTable = "CREATE TABLE IF NOT EXISTS wipeout_players (" +
            "`player` varchar(32) NOT NULL," +
            "`map_1_time` BIGINT(32)," +
            "`map_2_time` BIGINT(32)," +
            "`map_3_time` BIGINT(32)," +
            "`map_4_time` BIGINT(32)," +
            "PRIMARY KEY (`player`)" +
            ");";


    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname + ".db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
            }
        }
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
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

    public void setTime(UUID player, int map, long time) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("REPLACE INTO " + table + " (player,map_1_time,map_2_time,map_3_time,map_4_time) VALUES(?,?,?,?,?)");
            ps.setString(1, player.toString());

            switch (map) {
                case 1:
                    ps.setLong(2, time);
                    break;
                case 2:
                    ps.setLong(3, time);
                    break;
                case 3:
                    ps.setLong(4, time);
                    break;
                case 4:
                    ps.setLong(5, time);
                    break;
            }

            timedUsers.replace(player, new TimedUser(player, getTime(player, 1), getTime(player, 2), getTime(player, 3), getTime(player, 4)));

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
}
