package fr.holo795.wonskill.users;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.holo795.wonskill.WonSkill;
import fr.holo795.wonskill.jobs.Job;
import fr.holo795.wonskill.jobs.actions.Action;
import fr.holo795.wonskill.jobs.actions.ActionData;
import fr.holo795.wonskill.jobs.actions.ActionType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class UserData {


    private final WonSkill plugin;

    @Getter
    private HashMap<String, User> usersData = new HashMap<>();

    public UserData(WonSkill plugin, File[] files) {
        this.plugin = plugin;

        for (File userFile : files) {
            if (userFile.exists()) {
                ObjectMapper mapper = new ObjectMapper(new JsonFactory());
                mapper.findAndRegisterModules();
                try {
                    User user = mapper.readValue(userFile, User.class);
                    if (Objects.equals(user.getUuid(), userFile.getName().replace(".json", ""))) {
                        usersData.put(user.getUuid(), user);
                    } else {
                        plugin.logger.warning("WonSkill | User " + userFile.getName() + " has a wrong uuid.");
                        throw new Exception("Wrong uuid in file name or user.");
                    }
                } catch (Exception e) {
                    plugin.logger.warning("WonSkill | Error while loading userdata " + userFile.getName() + " : " + e.getMessage());
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
            }
        }

        plugin.logger.info("WonSkill | " + usersData.size() + " user" + (usersData.size() > 1 ? "s" : "") + " loaded.");

    }

    public void newUser(Player player) {

        User user = new User();
        user.setUuid(player.getUniqueId().toString());
        user.setJobID(-1);
        for (Job j : plugin.getJobsManager().jobs) {
            if (player.hasPermission(j.getPermission())) {
                user.setJobID(j.getId());
                break;
            }
        }
        usersData.put(user.getUuid(), user);
        saveUser(user);
    }

    public void saveUser(User user) {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.findAndRegisterModules();
        try {
            mapper.writeValue(new File(plugin.getUserDataFolder(), user.getUuid() + ".json"), user);
        } catch (Exception e) {
            plugin.logger.warning("WonSkill | Error while saving userdata " + user.getUuid() + " : " + e.getMessage());
        }
    }

    public void saveAllUsers() {
        for (User user : usersData.values()) {
            saveUser(user);
        }
    }

    public void updateUser(Player player, Action action) {
        updateUser(player, action, 1);
    }

    public void updateUser(Player player, Action action, int amount) {
        User user = getUser(player);
        if (user.getJobID() == -1) return;

        Job job = plugin.getJobsManager().getJob(user.getJobID());
        if (job == null) return;

        for (ActionData actionData : user.getActionData()) {
            if (actionData.getId() == action.getId()) {
                actionData.setAmount(actionData.getAmount() + 1);

                if (action.getAmount() <= actionData.getAmount()) {
                    user.setXp(user.getXp() + action.getXp());
                    actionData.setAmount(0);
                    Objects.requireNonNull(Bukkit.getServer().getPlayer(UUID.fromString(user.getUuid())))
                            .sendMessage("§aVous avez gagné " + action.getXp() + " xp.");
                }

                int point = (int) job.getExp_to_point().stream().filter(exp_to_point -> exp_to_point <= user.getXp()).count();
                if (point - user.getUsedPoint() > 0) {
                    user.setPoint(user.getPoint() + point - user.getUsedPoint());
                    Objects.requireNonNull(Bukkit.getServer().getPlayer(UUID.fromString(user.getUuid())))
                            .sendMessage("§aVous avez gagné " + (point - user.getUsedPoint()) + " point" + ((point - user.getUsedPoint()) > 1 ? "s" : "") + ".");
                }
                return;
            }
        }

        ActionData actionData = new ActionData();
        actionData.setId(action.getId());
        actionData.setAmount(amount);
        user.getActionData().add(actionData);

    }

    public List<Action> getActionsFromActionType(Player player, ActionType actionType) {
        User user = getUser(player);
        if (user.getJobID() == -1) return null;

        List<Action> actions = new ArrayList<>();

        Job job = plugin.getJobsManager().getJob(getUser(player).getJobID());

        for (Action action : job.getActions()) {
            if (action.getType().equalsIgnoreCase(actionType.toString())) {
                actions.add(action);
            }
        }

        return actions;

    }

    public User getUser(Player player) {
        User user = usersData.get(player.getUniqueId().toString());
        for (Job j : plugin.getJobsManager().jobs) {
            if (player.hasPermission(j.getPermission())) {
                user.setJobID(j.getId());
                return user;
            }
        }
        user.setJobID(-1);
        return user;
    }

}
