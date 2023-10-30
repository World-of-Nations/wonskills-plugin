package com.st0x0ef.wonskill.events;

import com.st0x0ef.wonskill.WonSkill;
import com.st0x0ef.wonskill.database.SkillsDb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PluginEventListener implements Listener {
    private final SkillsDb db;

    public PluginEventListener(WonSkill plugin) {
        this.db = WonSkill.db;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        if (db.verifyIfNewPlayer(uuid)) {
            db.createPlayerSkills(uuid);
        }
    }

}
