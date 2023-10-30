package com.st0x0ef.wonskill.users;

import com.st0x0ef.wonskill.WonSkill;
import com.st0x0ef.wonskill.jobs.Job;
import com.st0x0ef.wonskill.jobs.JobsManager;
import com.st0x0ef.wonskill.jobs.actions.Action;
import com.st0x0ef.wonskill.jobs.actions.ActionData;
import com.st0x0ef.wonskill.jobs.actions.ActionType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class UserData {
    private final WonSkill plugin;

    @Getter
    private final HashMap<String, User> usersData = new HashMap<>();

    public UserData(WonSkill plugin) {
        this.plugin = plugin;
    }

    public void newUser(Player player) {
        User user = new User();
        user.setUuid(player.getUniqueId().toString());
        user.setJobID(-1);
        for (Job j : JobsManager.jobs) {
            if (player.hasPermission(j.getPermission())) {
                user.setJobID(j.getId());
                break;
            }
        }
        usersData.put(user.getUuid(), user);
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
                            .sendMessage("§aYou've win " + action.getXp() + " xp.");
                }

                int point = (int) job.getExp_to_point().stream().filter(exp_to_point -> exp_to_point <= user.getXp()).count();
                if (point - user.getUsedPoint() > 0) {
                    user.setPoint(user.getPoint() + point - user.getUsedPoint());
                    Objects.requireNonNull(Bukkit.getServer().getPlayer(UUID.fromString(user.getUuid())))
                            .sendMessage("§aYou've win " + (point - user.getUsedPoint()) + " point" + ((point - user.getUsedPoint()) > 1 ? "s" : "") + ".");
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
        for (Job j : JobsManager.jobs) {
            if (player.hasPermission(j.getPermission())) {
                user.setJobID(j.getId());
                return user;
            }
        }
        user.setJobID(-1);
        return user;
    }

}
