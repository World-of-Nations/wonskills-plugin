package com.st0x0ef.wonskill.commands;

import com.st0x0ef.wonskill.WonSkill;
import com.st0x0ef.wonskill.database.SkillsDb;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("skill")) {
            SkillsDb db = WonSkill.db;
            command.tabComplete(sender, "skill", new String[] {"addpoint", "removepoint", "setpoint", "addxp", "removexp", "setxp"});

            if (sender instanceof Player player) {
                if (args.length == 0) {
                    error("/skill <addpoint, removepoint, setpoint, addxp, removexp, setxp> <param>", ChatColor.YELLOW, player);
                }

                else {
                    if (args[0] == "addpoint") {
                        if (args.length == 1 || args.length > 2) {
                            error("/skill addpoint <amount>", ChatColor.YELLOW, player);
                        }
                        else {
                            int amount = 0;
                            try {
                                amount = Integer.parseInt(args[1]);
                            }
                            catch (NumberFormatException e) {
                                error("this is not a number", ChatColor.YELLOW, player);
                            }
                            amount += db.getSkillPoints(player.getUniqueId().toString());
                            if (amount >= 0) db.setSkillPoints(player.getUniqueId().toString(), amount);
                            else error("The number of point can't be inferior to 0", ChatColor.YELLOW, player);
                        }
                    }
                    else if (args[0] == "removepoint") {
                        if (args.length == 1 || args.length > 2) {
                            error("/skill removepoint <amount>", ChatColor.YELLOW, player);
                        }
                        else {
                            int amount = 0;
                            try {
                                amount = -Integer.parseInt(args[1]);
                            }
                            catch (NumberFormatException e) {
                                error("this is not a number", ChatColor.YELLOW, player);
                            }
                            amount += db.getSkillPoints(player.getUniqueId().toString());
                            if (amount >= 0) db.setSkillPoints(player.getUniqueId().toString(), amount);
                            else error("The number of point can't be inferior to 0", ChatColor.YELLOW, player);
                        }
                    }
                    else if (args[0] == "setpoint") {
                        if (args.length == 1 || args.length > 2) {
                            error("/skill setpoint <amount>", ChatColor.YELLOW, player);
                        }
                        else {
                            int amount = -1;
                            try {
                                amount = Integer.parseInt(args[1]);
                            }
                            catch (NumberFormatException e) {
                                error("this is not a number", ChatColor.YELLOW, player);
                            }
                            if (amount >= 0) db.setSkillPoints(player.getUniqueId().toString(), amount);
                        }
                    }
                    else if (args[0] == "addxp") {
                        if (args.length == 1 || args.length > 2) {
                            error("/skill addxp <amount>", ChatColor.YELLOW, player);
                        }
                        else {
                            int amount = 0;
                            try {
                                amount = Integer.parseInt(args[1]);
                            }
                            catch (NumberFormatException e) {
                                error("this is not a number", ChatColor.YELLOW, player);
                            }
                            amount += db.getSkillXp(player.getUniqueId().toString());
                            if (amount >= 0) db.setSkillXp(player.getUniqueId().toString(), amount);
                            else error("The number of xp can't be inferior to 0", ChatColor.YELLOW, player);
                        }
                    }
                    else if (args[0] == "removexp") {
                        if (args.length == 1 || args.length > 2) {
                            error("/skill removexp <amount>", ChatColor.YELLOW, player);
                        }
                        else {
                            int amount = 0;
                            try {
                                amount = -Integer.parseInt(args[1]);
                            }
                            catch (NumberFormatException e) {
                                error("this is not a number", ChatColor.YELLOW, player);
                            }
                            amount += db.getSkillXp(player.getUniqueId().toString());
                            if (amount >= 0) db.setSkillXp(player.getUniqueId().toString(), amount);
                            else error("The number of xp can't be inferior to 0", ChatColor.YELLOW, player);
                        }
                    }
                    else if (args[0] == "setxp") {
                        if (args.length == 1 || args.length > 2) {
                            error("/skill setxp <amount>", ChatColor.YELLOW, player);
                        }
                        else {
                            int amount = -1;
                            try {
                                amount = Integer.parseInt(args[1]);
                            }
                            catch (NumberFormatException e) {
                                error("this is not a number", ChatColor.YELLOW, player);
                            }
                            if (amount >= 0) db.setSkillXp(player.getUniqueId().toString(), amount);
                        }
                    }
                    else {
                        error("/skill <addpoint, removepoint, setpoint, addxp, removexp, setxp> <param>", ChatColor.YELLOW, player);
                    }
                }
            }
        }
        return true;
    }

    private void error(String message, ChatColor color, Player player) {
        player.sendMessage(ChatColor.RED + "Incorect usage of the command");
        player.sendMessage(color + message);
    }
}
