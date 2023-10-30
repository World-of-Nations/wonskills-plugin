package com.st0x0ef.wonskill.jobs;

import com.st0x0ef.wonskill.WonSkill;
import com.st0x0ef.wonskill.jobs.actions.Action;
import com.st0x0ef.wonskill.jobs.actions.ActionType;
import com.st0x0ef.wonskill.jobs.skills.Skill;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JobsManager {

    private final WonSkill plugin;
    public static ArrayList<Job> jobs = new ArrayList<>();

    public JobsManager(WonSkill plugin, File[] files) {
        this.plugin = plugin;

        /**for (File jobFile : files) {
            if (jobFile.exists()) {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                mapper.findAndRegisterModules();
                try {
                    Job job = mapper.readValue(jobFile, Job.class);
                    if (idExists(job.id) || nameExists(job.name)) {
                        plugin.logger.warning("WonSkill | Job " + job.name + " has the same id or name as another job.");
                        plugin.logger.warning("WonSkill | Job " + job.name + " not loaded.");
                    } else {
                        plugin.logger.info("WonSkill | Job " + job.name + " loaded.");
                        jobs.add(job);
                    }
                } catch (Exception e) {
                    plugin.logger.warning("WonSkill | Error while loading jobs " + jobFile.getName() + " : " + e.getMessage());
                }
            }
        }

        if (jobs.size() == 0) {
            plugin.logger.warning("WonSkill | No job found.");
            plugin.logger.warning("WonSkill | Disabling plugin due to no job found.");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

        plugin.logger.info("WonSkill | Checking skills id...");
        checkSkillIdAlreadyExist();

        plugin.logger.info("WonSkill | " + jobs.size() + " jobs loaded.");
        plugin.logger.info("WonSkill | Checking skills permissions...");
        checkSkillPermission();

        int skillCount = jobs.stream().mapToInt(job -> job.skills.size()).sum();
        plugin.logger.info("WonSkill | " + skillCount + " skill" + (skillCount > 1 ? "s" : "") + " loaded.");

        plugin.logger.info("WonSkill | Checking actions id...");
        checkActionIdAlreadyExist();
        int actionCount = jobs.stream().mapToInt(job -> job.actions.size()).sum();
        plugin.logger.info("WonSkill | " + actionCount + " action" + (actionCount > 1 ? "s" : "") + " loaded.");

        plugin.logger.info("WonSkill | " + jobs.size() + " job" + (jobs.size() > 1 ? "s" : "") + " loaded.");
         */
    }

    public boolean idExists(int id) {
        for (Job job : jobs) {
            if (job.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean nameExists(String name) {
        for (Job job : jobs) {
            if (job.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void checkSkillPermission() {
        for (Job job : jobs) {
            for (Skill skill : job.getSkills()) {
                if (skill.getPermissions_add() != null) {
                    for (String permission : skill.getPermissions_add()) {
                        if (plugin.getServer().getPluginManager().getPermission(permission) == null) {
                            plugin.logger.warning("WonSkill | Permission " + permission + " not found for skill " +
                                    skill.getName() + ". | Job " + job.getName());
                        }
                    }
                }
                if (skill.getPermissions_remove() != null) {
                    for (String permission : skill.getPermissions_remove()) {
                        if (plugin.getServer().getPluginManager().getPermission(permission) == null) {
                            plugin.logger.warning("WonSkill | Permission " + permission + " not found for skill " +
                                    skill.getName() + ". | Job " + job.getName());
                        }
                    }
                }
            }
        }
    }

    public void checkSkillIdAlreadyExist() {
        for (Job job : jobs) {
            for (Skill skill : job.skills) {
                for (Skill skill1 : job.skills) {
                    if (skill.getId() == skill1.getId() && skill != skill1) {
                        plugin.logger.warning("WonSkill | Skill " + skill.getName() + " has the same id as another skill. | Job " + job.getName());
                        plugin.logger.warning("WonSkill | Skill " + skill.getName() + " not loaded.");
                        job.skills.remove(skill);
                    }
                }
            }
        }
    }

    public void checkActionIdAlreadyExist() {
        for (Job job : jobs) {
            for (Action action : job.actions) {
                for (Action action1 : job.actions) {
                    if (action.getId() == action1.getId() && action != action1) {
                        plugin.logger.warning("WonSkill | Action " + action.getType() + " has the same id as another action. | Job " + job.getName());
                        plugin.logger.warning("WonSkill | Action " + action.getType() + " not loaded.");
                        job.actions.remove(action);
                    }
                }
            }
        }
    }

    public List<Job> parseAction(ActionType action) {
        List<Job> jobs = new ArrayList<>();
        for (Job job : JobsManager.jobs) {
            for (Action jobAction : job.getActions()) {
                if (ActionType.valueOf(jobAction.getType().toUpperCase()) == action) {
                    jobs.add(job);
                }
            }
        }
        return jobs;
    }

    public Job getJob(int id) {
        for (Job job : jobs) {
            if (job.getId() == id) {
                return job;
            }
        }
        return null;
    }
}
