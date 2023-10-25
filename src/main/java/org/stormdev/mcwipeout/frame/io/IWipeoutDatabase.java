package org.stormdev.mcwipeout.frame.io;
/*
  Created by Stormbits at 9/11/2023
*/

import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.io.impl.Error;
import org.stormdev.mcwipeout.frame.io.impl.Errors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public abstract class IWipeoutDatabase {
    public Wipeout plugin;
    public Connection connection;

    public String table = "wipeout_players";

    public IWipeoutDatabase(Wipeout instance) {
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize() {
        connection = getSQLConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
            ResultSet rs = ps.executeQuery();
            close(ps, rs);

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
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

    public void closeConnection() {
        if (getSQLConnection() != null) {
            try {
                getSQLConnection().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }
}
