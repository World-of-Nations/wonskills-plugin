package com.st0x0ef.wonskill.database;

import com.st0x0ef.wonskill.WonSkill;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection connection;
    public Connection getConnection() throws SQLException {
        if (this.connection != null) return this.connection;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String databaseUrl = "jdbc:mysql://51.75.145.173:3306/mc77581";
        String databaseName = "mc77581";
        String databasePasword = "dfb20c091d";

        try {
            this.connection = DriverManager.getConnection(databaseUrl, databaseName, databasePasword);
            return this.connection;
        }
        catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(WonSkill.getProvidingPlugin(WonSkill.class));
            return null;
        }




    }
}
