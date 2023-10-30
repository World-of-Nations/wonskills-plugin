package com.st0x0ef.wonskill.commands;

import com.st0x0ef.wonskill.WonSkill;
import com.st0x0ef.wonskill.database.SkillsDb;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("skills")) {
            if (sender instanceof Player player) {
                SkillsDb db = WonSkill.db;
                if (db == null) {
                    return false;
                }
                int point = db.getSkillPoints(player.getUniqueId().toString());
                if (point == 0) {
                    player.sendMessage("You don't have any point");
                }
                else {
                    player.sendMessage("You have " + point + " points");
                }
                return true;
            }
        }

        return false;
    }
}
