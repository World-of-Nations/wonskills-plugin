package com.st0x0ef.wonskill;

import fr.andross.banitem.items.BannedItem;
import com.st0x0ef.wonskill.database.SkillsDb;
import com.st0x0ef.wonskill.events.JobsEventListener;
import com.st0x0ef.wonskill.events.PluginEventListener;
import com.st0x0ef.wonskill.jobs.JobsManager;
import com.st0x0ef.wonskill.packets.PacketHelper;
import com.st0x0ef.wonskill.packets.PacketListener;
import com.st0x0ef.wonskill.users.UserData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    private UserData userData;
    @Getter
    private JobsManager jobsManager;
    @Getter
    private PacketHelper packetHelper;
    public static SkillsDb db;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logger.info("WonSkill | Plugin stating...");

        db = new SkillsDb();
        db.loadDatabase();
        db.initAllPlayer();

        initFolder();
        initJobs();

        initUsersData();
        defaultConfig();

        getServer().getPluginManager().registerEvents(new JobsEventListener(this), this);
        getServer().getPluginManager().registerEvents(new PluginEventListener(this), this);

        packetHelper = new PacketHelper(this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, packetHelper.CHANNEL_NAME);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, packetHelper.CHANNEL_NAME, new PacketListener(this));


        logger.fine("WonSkill | Plugin started.");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
        }

    }

    private void initUsersData() {
        userData = new UserData(this);

        logger.info("WonSkill | Loading users data...");
        if (userDataFolder.listFiles() == null) {
            logger.warning("WonSkill | No user data found.");
        } else {
            logger.info("WonSkill | " + Arrays.stream(Objects.requireNonNull(userDataFolder.listFiles())).filter(file ->
                    file.getName().endsWith(".json")).count() + " user data" +
                    (Objects.requireNonNull(userDataFolder.listFiles()).length > 1 ? "s" : "") + " found.");
        }
    }

    public void defaultConfig() {
        /**
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
        */

        File file = new File(jobFolder, "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            logger.info("WonSkill | Creating default job miner.yml...");

            try {
                file.createNewFile();
                logger.info("WonSkill | Default job miner.yml created.");

                config.addDefault("Skills", new String[] {"Miner", "Farmer", "Hunter"});
                config.addDefault("Miner", new int[] {1, 2, 3});
                config.addDefault("Farmer", new int[] {1, 2, 3});
                config.addDefault("Hunter", new int[] {1, 2, 3});
                config.addDefault("Miner.1.banitem", new String[] {"stone", "iron_ingot", "diamond_ore"});
                config.addDefault("Miner.1.effect.haste", 0);
                config.addDefault("Miner.1.effect.doubleloot", 10);

            } catch (Exception e) {
                logger.warning("WonSkill | Error while creating default job miner.yaml : " + e.getMessage());
            }
        }

        List<String> skills = config.getStringList("Skills");
        for (String skill : skills) {
            List<Integer> levels = config.getIntegerList(skill);
            for (int level : levels) {
                List<String> banItems = config.getStringList(skill + "." + level + ".banitem");
                for (String banItem : banItems) {
                    new BannedItem(Objects.requireNonNull(Material.getMaterial(banItem)));
                }
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
