package com.st0x0ef.wonskill.database;

import com.st0x0ef.wonskill.jobs.Job;
import com.st0x0ef.wonskill.jobs.JobsManager;
import com.st0x0ef.wonskill.users.User;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SkillsDb {
    Database database;

    public void loadDatabase() {
        try {
            database = new Database();
            Statement statement = database.getConnection().createStatement();
            StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS player_skills(uuid varchar(36) primary key, points int, xp int, usedPoints int");
            for (Job job: JobsManager.jobs) {
                sql.append(", ").append(job.getName()).append(" int");
            }
            sql.append(")");

            statement.execute(sql.toString());
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyIfNewPlayer(String uuid) {
        try {
            if (database != null) {
                Statement statement = database.getConnection().createStatement();
                ResultSet result = statement.executeQuery("SELECT * FROM player_skills WHERE uuid = '" + uuid + "'");
                if (!result.next()) {
                    statement.close();
                    return true;
                }

                statement.close();
                return false;
            }

            return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createPlayerSkills(String uuid) {
        try {
            if (database != null) {
                PreparedStatement statement = database.getConnection().prepareStatement("INSERT INTO player_skills(uuid, points, xp) VALUES (?, ?, ?)");
                statement.setString(1, uuid);
                statement.setInt(2, 0);
                statement.setInt(3, 0);
                statement.executeUpdate();
                statement.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getSkillXp(String uuid) {
        try {
            if (database != null) {
                Statement statement = database.getConnection().createStatement();
                ResultSet result = statement.executeQuery("SELECT xp FROM player_skills WHERE uuid = " + uuid);
                int xp = -1;
                if (result.next()) {
                    xp = result.getInt("xp");
                }
                statement.close();
                return xp;
            }

            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getSkillPoints(String uuid) {
        try {
            if (database != null) {
                Statement statement = database.getConnection().createStatement();
                ResultSet result = statement.executeQuery("SELECT points FROM player_skills WHERE uuid = " + uuid);
                int points = -1;
                if (result.next()) {
                    points = result.getInt("points");
                }
                statement.close();
                return points;
            }

            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setSkillXp(String uuid, int amount) {
        try {
            if (database != null) {
                PreparedStatement statement = database.getConnection().prepareStatement("UPDATE player_skills SET xp = " + amount + "WHERE uuid = " + uuid);
                statement.executeUpdate();
                statement.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setSkillPoints(String uuid, int amount) {
        try {
            if (database != null) {
                PreparedStatement statement = database.getConnection().prepareStatement("UPDATE player_skills SET points = " + amount + "WHERE uuid = " + uuid);
                statement.executeUpdate();
                statement.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setSkillUsedPoints(String uuid, int amount) {
        try {
            if (database != null) {
                PreparedStatement statement = database.getConnection().prepareStatement("UPDATE player_skills SET usedPoints = " + amount + "WHERE uuid = " + uuid);
                statement.executeUpdate();
                statement.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initAllPlayer() {
        try {
            if (database != null) {
                Statement statement = database.getConnection().createStatement();
                ResultSet result = statement.executeQuery("SELECT * FROM player_skills");
                while (result.next()) {
                    User user = new User();
                    user.setUuid(result.getString("uuid"));
                    user.setPoint(result.getInt("points"));
                    user.setXp(result.getInt("xp"));
                    user.setUsedPoint(result.getInt("usedPoints"));

                    StringBuilder sql = new StringBuilder("SELECT");
                    for (Job job : JobsManager.jobs) {
                        sql.append(" ").append(job.getName());
                    }
                    sql.append(" FROM player_skills WHERE uuid = ").append(user.getUuid());
                    ResultSet result2 = statement.executeQuery(sql.toString());

                    List<Integer> unlockedSkills = new ArrayList<>();
                    for (Job job : JobsManager.jobs) {
                        unlockedSkills.add(result2.getInt(job.getName()));
                    }
                    user.setUnlockedSkills(unlockedSkills);

                    user.setJobID(-1);
                    for (Job j : JobsManager.jobs) {
                        if (Objects.requireNonNull(Bukkit.getPlayer(user.getUuid())).hasPermission(j.getPermission())) {
                            user.setJobID(j.getId());
                            break;
                        }
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeDatabase() {
        if (database != null) {
            try { database.getConnection().close(); }
            catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
