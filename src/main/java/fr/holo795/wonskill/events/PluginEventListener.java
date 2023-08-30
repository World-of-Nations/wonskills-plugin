package fr.holo795.wonskill.events;

import fr.holo795.wonskill.WonSkill;
import fr.holo795.wonskill.users.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PluginEventListener implements Listener {


    private final UserData userData;

    public PluginEventListener(WonSkill plugin) {
        this.userData = plugin.getUserData();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!userData.getUsersData().containsKey(player.getUniqueId().toString())) {
            userData.newUser(player);
        }
    }

}
