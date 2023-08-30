package fr.holo795.wonskill.events;

import fr.holo795.wonskill.WonSkill;
import fr.holo795.wonskill.jobs.actions.Action;
import fr.holo795.wonskill.jobs.actions.ActionType;
import fr.holo795.wonskill.users.UserData;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/*****************A revoir pour simplifier******************/

public class JobsEventListener implements Listener {


    private final WonSkill plugin;
    private final UserData userData;

    public JobsEventListener(WonSkill plugin) {
        this.plugin = plugin;
        this.userData = plugin.getUserData();
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        List<Action> actions = userData.getActionsFromActionType(player, ActionType.BREAK);
        if (actions.size() == 0) return;

        Block block = event.getBlock();
        for (Action action : actions) {
            if (action == null) continue;

            for (String blockName : action.getBlock()) {
                if (action.getBlock() == null || action.getBlock().size() == 0) {
                    userData.updateUser(player, action);
                    return;
                }
                if (blockName.contains(":")) {
                    String[] blockNameSplit = blockName.split(":");
                    if (blockNameSplit.length != 2) continue;
                    if (blockNameSplit[0].equalsIgnoreCase(block.getType().getKey().getNamespace()) &&
                            blockNameSplit[1].equalsIgnoreCase(block.getType().getKey().getKey())) {
                        userData.updateUser(player, action);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        List<Action> actions = userData.getActionsFromActionType(player, ActionType.PLACE);
        if (actions.size() == 0) return;

        Block block = event.getBlock();
        for (Action action : actions) {
            if (action == null) continue;

            for (String blockName : action.getBlock()) {
                if (action.getBlock() == null || action.getBlock().size() == 0) {
                    userData.updateUser(player, action);
                    return;
                }
                if (blockName.contains(":")) {
                    String[] blockNameSplit = blockName.split(":");
                    if (blockNameSplit.length != 2) continue;
                    if (blockNameSplit[0].equalsIgnoreCase(block.getType().getKey().getNamespace()) &&
                            blockNameSplit[1].equalsIgnoreCase(block.getType().getKey().getKey())) {
                        userData.updateUser(player, action);
                        return;
                    }
                }
            }
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        List<Action> actions = userData.getActionsFromActionType(player, ActionType.INTERACT);
        if (actions.size() == 0) return;

        Block block = event.getClickedBlock();
        ItemStack item = event.getItem();

        for (Action action : actions) {
            if (action == null || action.getBlock() == null || action.getBlock().size() == 0) continue;

            for (String blockName : action.getBlock()) {
                if (blockName.contains(":")) {
                    String[] blockNameSplit = blockName.split(":");
                    if (blockNameSplit.length != 2) continue;
                    if (blockNameSplit[0].equalsIgnoreCase(block.getType().getKey().getNamespace()) &&
                            blockNameSplit[1].equalsIgnoreCase(block.getType().getKey().getKey())) {
                        userData.updateUser(player, action);
                        return;
                    }
                }
            }
        }

        for (Action action : actions) {
            if (action == null || action.getItem() == null || action.getItem().size() == 0) continue;

            for (String itemName : action.getItem()) {
                if (itemName.contains(":")) {
                    String[] itemNameSplit = itemName.split(":");
                    if (itemNameSplit.length != 2) continue;
                    if (itemNameSplit[0].equalsIgnoreCase(item.getType().getKey().getNamespace()) &&
                            itemNameSplit[1].equalsIgnoreCase(item.getType().getKey().getKey())) {
                        userData.updateUser(player, action);
                        return;
                    }
                }
            }
        }

    }

    @EventHandler
    public void onPlayerWalk(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.isFlying()) return;
        if (player.isGliding()) return;
        if (player.isSwimming()) return;
        if (player.isInsideVehicle()) return;

        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            int nb_block = (int) event.getFrom().distanceSquared(event.getTo());

            List<Action> actions = userData.getActionsFromActionType(player, ActionType.WALK);
            if (actions.size() == 0) return;

            for (Action action : actions) {
                if (action == null) continue;
                userData.updateUser(player, action, nb_block);
            }

        }
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        List<Action> actions = userData.getActionsFromActionType(player, ActionType.CRAFT);
        if (actions.size() == 0) return;

        ItemStack item = event.getRecipe().getResult();
        for (Action action : actions) {
            if (action == null) continue;

            for (String itemName : action.getItem()) {
                if (action.getItem() == null || action.getItem().size() == 0) {
                    userData.updateUser(player, action);
                    return;
                }
                if (itemName.contains(":")) {
                    String[] itemNameSplit = itemName.split(":");
                    if (itemNameSplit.length != 2) continue;
                    if (itemNameSplit[0].equalsIgnoreCase(item.getType().getKey().getNamespace()) &&
                            itemNameSplit[1].equalsIgnoreCase(item.getType().getKey().getKey())) {
                        userData.updateUser(player, action);
                        return;
                    }
                }
            }
        }

    }

    @EventHandler
    public void onPlayerSmelt(FurnaceExtractEvent event) {
        Player player = (Player) event.getPlayer();
        List<Action> actions = userData.getActionsFromActionType(player, ActionType.SMELT);
        if (actions.size() == 0) return;

        ItemStack item = event.getPlayer().getItemOnCursor();
        for (Action action : actions) {
            if (action == null) continue;

            for (String itemName : action.getItem()) {
                if (action.getItem() == null || action.getItem().size() == 0) {
                    userData.updateUser(player, action);
                    return;
                }
                if (itemName.contains(":")) {
                    String[] itemNameSplit = itemName.split(":");
                    if (itemNameSplit.length != 2) continue;
                    if (itemNameSplit[0].equalsIgnoreCase(item.getType().getKey().getNamespace()) &&
                            itemNameSplit[1].equalsIgnoreCase(item.getType().getKey().getKey())) {
                        userData.updateUser(player, action);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerEnchant(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        List<Action> actions = userData.getActionsFromActionType(player, ActionType.ENCHANT);
        if (actions.size() == 0) return;

        ItemStack item = event.getItem();
        for (Action action : actions) {
            if (action == null) continue;

            for (String itemName : action.getItem()) {
                if (action.getItem() == null || action.getItem().size() == 0) {
                    userData.updateUser(player, action);
                    return;
                }
                if (itemName.contains(":")) {
                    String[] itemNameSplit = itemName.split(":");
                    if (itemNameSplit.length != 2) continue;
                    if (itemNameSplit[0].equalsIgnoreCase(item.getType().getKey().getNamespace()) &&
                            itemNameSplit[1].equalsIgnoreCase(item.getType().getKey().getKey())) {
                        userData.updateUser(player, action);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;

        Player player = event.getPlayer();
        List<Action> actions = userData.getActionsFromActionType(player, ActionType.FISH);
        if (actions.size() == 0) return;

        Item item = (Item) event.getCaught();
        for (Action action : actions) {
            if (action == null) continue;

            for (String itemName : action.getItem()) {
                if (action.getItem() == null || action.getItem().size() == 0) {
                    userData.updateUser(player, action);
                    return;
                }
                if (itemName.contains(":")) {
                    String[] itemNameSplit = itemName.split(":");
                    if (itemNameSplit.length != 2) continue;
                    if (itemNameSplit[0].equalsIgnoreCase(item.getItemStack().getType().getKey().getNamespace()) &&
                            itemNameSplit[1].equalsIgnoreCase(item.getItemStack().getType().getKey().getKey())) {
                        userData.updateUser(player, action);
                        return;
                    }
                }
            }
        }

    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (player == null) return;

        List<Action> actions = userData.getActionsFromActionType(player, ActionType.KILL);
        if (actions.size() == 0) return;

        for (Action action : actions) {
            if (action == null) continue;
            userData.updateUser(player, action);
        }
    }

    @EventHandler
    public void onPlayerTame(EntityTameEvent event) {
        Player player = (Player) event.getOwner();
        List<Action> actions = userData.getActionsFromActionType(player, ActionType.TAME);
        if (actions.size() == 0) return;

        Entity entity = event.getEntity();

        for (Action action : actions) {
            if (action == null) continue;

            for (String entityName : action.getEntity()) {
                if (action.getEntity() == null || action.getEntity().size() == 0) {
                    userData.updateUser(player, action);
                    return;
                }
                if (entityName.contains(":")) {
                    String[] entityNameSplit = entityName.split(":");
                    if (entityNameSplit.length != 2) continue;
                    if (entityNameSplit[0].equalsIgnoreCase(entity.getType().getKey().getNamespace()) &&
                            entityNameSplit[1].equalsIgnoreCase(entity.getType().getKey().getKey())) {
                        userData.updateUser(player, action);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerHarvest(PlayerHarvestBlockEvent event) {
        Player player = event.getPlayer();
        List<Action> actions = userData.getActionsFromActionType(player, ActionType.HARVEST);
        if (actions.size() == 0) return;

        Block block = event.getHarvestedBlock();
        for (Action action : actions) {
            if (action == null) continue;

            for (String blockName : action.getBlock()) {
                if (action.getBlock() == null || action.getBlock().size() == 0) {
                    userData.updateUser(player, action);
                    return;
                }
                if (blockName.contains(":")) {
                    String[] blockNameSplit = blockName.split(":");
                    if (blockNameSplit.length != 2) continue;
                    if (blockNameSplit[0].equalsIgnoreCase(block.getType().getKey().getNamespace()) &&
                            blockNameSplit[1].equalsIgnoreCase(block.getType().getKey().getKey())) {
                        userData.updateUser(player, action);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        List<Action> actions = userData.getActionsFromActionType(player, ActionType.SHEAR);
        if (actions.size() == 0) return;

        Entity entity = event.getEntity();

        for (Action action : actions) {
            if (action == null) continue;

            for (String entityName : action.getEntity()) {
                if (action.getEntity() == null || action.getEntity().size() == 0) {
                    userData.updateUser(player, action);
                    return;
                }
                if (entityName.contains(":")) {
                    String[] entityNameSplit = entityName.split(":");
                    if (entityNameSplit.length != 2) continue;
                    if (entityNameSplit[0].equalsIgnoreCase(entity.getType().getKey().getNamespace()) &&
                            entityNameSplit[1].equalsIgnoreCase(entity.getType().getKey().getKey())) {
                        userData.updateUser(player, action);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        List<Action> actions = userData.getActionsFromActionType(player, ActionType.EAT);
        if (actions.size() == 0) return;

        ItemStack item = event.getItem();
        for (Action action : actions) {
            if (action == null) continue;

            for (String itemName : action.getItem()) {
                if (action.getItem() == null || action.getItem().size() == 0) {
                    userData.updateUser(player, action);
                    return;
                }
                if (itemName.contains(":")) {
                    String[] itemNameSplit = itemName.split(":");
                    if (itemNameSplit.length != 2) continue;
                    if (itemNameSplit[0].equalsIgnoreCase(item.getType().getKey().getNamespace()) &&
                            itemNameSplit[1].equalsIgnoreCase(item.getType().getKey().getKey())) {
                        userData.updateUser(player, action);
                        return;
                    }
                }
            }
        }


    }
}
