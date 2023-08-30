package fr.holo795.wonskill;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import fr.holo795.wonskill.events.JobsEventListener;
import fr.holo795.wonskill.events.PluginEventListener;
import fr.holo795.wonskill.jobs.Job;
import fr.holo795.wonskill.jobs.JobsManager;
import fr.holo795.wonskill.jobs.actions.Action;
import fr.holo795.wonskill.jobs.skills.Skill;
import fr.holo795.wonskill.packets.PacketHelper;
import fr.holo795.wonskill.packets.PacketListener;
import fr.holo795.wonskill.users.UserData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public final class WonSkill extends JavaPlugin {

    public final Logger logger = Bukkit.getLogger();
    private final File jobFolder = new File(getDataFolder(), "jobs");
    @Getter
    private final File userDataFolder = new File(getDataFolder(), "data");
    @Getter
    private JobsManager jobsManager;
    @Getter
    private UserData userData;
    @Getter
    private PacketHelper packetHelper;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logger.info("WonSkill | Plugin stating...");
        initFolder();
        defaultConfig();
        initJobs();

        initUsersData();

        getServer().getPluginManager().registerEvents(new JobsEventListener(this), this);
        getServer().getPluginManager().registerEvents(new PluginEventListener(this), this);

        packetHelper = new PacketHelper(this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, packetHelper.CHANNEL_NAME);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, packetHelper.CHANNEL_NAME, new PacketListener(this));

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            userData.saveAllUsers();
            logger.info("WonSkill | Users data saved.");
        }, 0L, 20L * 60L * 2L);


        logger.fine("WonSkill | Plugin started.");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        userData.saveAllUsers();
        logger.info("WonSkill | Users data saved.");

        Bukkit.getMessenger().unregisterIncomingPluginChannel(this);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this);

        logger.info("WonSkill | Plugin stopped.");
    }

    private void initFolder() {
        getDataFolder().mkdirs();
        if (!jobFolder.exists()) {
            logger.info("WonSkill | Creating jobs folder...");
            jobFolder.mkdirs();
            logger.info("WonSkill | Jobs folder created.");
        }
        if (!userDataFolder.exists()) {
            logger.info("WonSkill | Creating data folder...");
            userDataFolder.mkdirs();
            logger.info("WonSkill | Data folder created.");
        }
    }

    private void initJobs() {
        logger.info("WonSkill | Loading jobs...");
        if (jobFolder.listFiles() == null) {
            logger.warning("WonSkill | No job found.");
            logger.warning("WonSkill | Disabling plugin due to no job found.");
            super.onDisable();
        } else {
            logger.info("WonSkill | " + Arrays.stream(Objects.requireNonNull(jobFolder.listFiles())).filter(file ->
                    file.getName().endsWith(".yaml")).count() + " job" +
                    (Objects.requireNonNull(jobFolder.listFiles()).length > 1 ? "s" : "") + " found.");
            jobsManager = new JobsManager(this, Objects.requireNonNull(jobFolder.listFiles()));
        }

    }

    private void initUsersData() {
        logger.info("WonSkill | Loading users data...");
        if (userDataFolder.listFiles() == null) {
            logger.warning("WonSkill | No user data found.");
        } else {
            logger.info("WonSkill | " + Arrays.stream(Objects.requireNonNull(userDataFolder.listFiles())).filter(file ->
                    file.getName().endsWith(".json")).count() + " user data" +
                    (Objects.requireNonNull(userDataFolder.listFiles()).length > 1 ? "s" : "") + " found.");
            userData = new UserData(this, Objects.requireNonNull(userDataFolder.listFiles()));
        }
    }

    public void defaultConfig() {
        Action action = new Action();
        action.setType("break");
        action.setAmount(200);
        action.setXp(2);
        action.setBlock(List.of("minecraft:dirt"));

        Skill skill = new Skill();
        skill.setId(0);
        skill.setName("Miner");
        skill.setDescription("You are a tiny miner.");
        skill.setEffects(List.of("speed 1"));
        skill.setPermissions_add(List.of("bukkit.command.plugins"));
        skill.setCost(1);

        Job job = new Job();
        job.setId(0);
        job.setName("Miner");
        job.setDescription("You are a miner.");
        job.setPermission("wonskill.job.miner");
        job.setSkills(List.of(skill));
        job.setActions(List.of(action));
        job.setExp_to_point(List.of(100, 200, 300, 400, 500, 600, 700, 800, 900, 1000));

        File exampleFile = new File(jobFolder, "miner.yaml");
        if (!exampleFile.exists()) {
            logger.info("WonSkill | Creating default job miner.yaml...");
            try {
                exampleFile.createNewFile();
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                mapper.writeValue(exampleFile, job);
                logger.info("WonSkill | Default job miner.yaml created.");
            } catch (Exception e) {
                logger.warning("WonSkill | Error while creating default job miner.yaml : " + e.getMessage());
            }
        }


    }
}



/*
### Config ###
Timer to save data
Database or file
Prefix for messages
##############
 */
