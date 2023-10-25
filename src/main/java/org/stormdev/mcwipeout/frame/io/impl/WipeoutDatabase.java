package org.stormdev.mcwipeout.frame.io.impl;
/*
  Created by Stormbits at 10/25/2023
*/

import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.io.IWipeoutDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class WipeoutDatabase extends IWipeoutDatabase {

    String dbname;
    public WipeoutDatabase(Wipeout instance){
        super(instance);
        dbname = "wipeout_players";
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
        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
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
}
