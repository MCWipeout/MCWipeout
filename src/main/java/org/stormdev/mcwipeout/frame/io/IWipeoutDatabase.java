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
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + ";");
            ResultSet rs = ps.executeQuery();
            close(ps, rs);

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
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
