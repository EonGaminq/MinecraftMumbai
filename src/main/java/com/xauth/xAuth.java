package com.xauth;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;
import org.bukkit.Sound;




import java.util.ArrayList;
import java.util.List;

import fr.xephi.authme.api.v3.AuthMeApi;

import com.xauth.listeners.onGUIClose;
import com.xauth.listeners.onLogoutCommand;
import com.xauth.listeners.onUnregisterCommand;



public class xAuth extends JavaPlugin implements CommandExecutor, Listener {

    private List<Integer> clickedSlots;
    private ItemStack fillItem;
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        clickedSlots = new ArrayList<>();
        fillItem = createFillItem();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new onGUIClose(this, getConfig().getString("LoginTitle")), this);
        Bukkit.getPluginManager().registerEvents(new onLogoutCommand(this), this);
        Bukkit.getPluginManager().registerEvents(new onUnregisterCommand(this), this);
        getLogger().info("xAuth plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("xAuth plugin has been disabled.");
    }

    String loginTitle = getConfig().getString("LoginTitle");
    String registerTitle = getConfig().getString("RegisterTitle");


    // Create the fill item with custom model data and slot number as display name
    private ItemStack createFillItem() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ChatColor.RESET + "");
        item.setItemMeta(itemMeta);
        return item;
    }

    //Login PIN GUI inventory Creation
    public void openLoginGUI(Player player) {
        Inventory LoginGUI = Bukkit.createInventory(null, InventoryType.DROPPER, loginTitle);
        for (int i = 0; i < LoginGUI.getSize(); i++) {
            ItemStack fillItemClone = fillItem.clone();
            ItemMeta itemMeta = fillItemClone.getItemMeta();
            itemMeta.setDisplayName(ChatColor.RESET + String.valueOf(i + 1));
            fillItemClone.setItemMeta(itemMeta);
            LoginGUI.setItem(i, fillItemClone);
        }
        player.openInventory(LoginGUI);
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        Status status = event.getStatus();
        if (status == Status.SUCCESSFULLY_LOADED) {
            if (!AuthMeApi.getInstance().isAuthenticated(player)) {
                openLoginGUI(player);
            }
            if (AuthMeApi.getInstance().isAuthenticated(player)) {
                return;
            }
        } else if (status == Status.DECLINED || status == Status.FAILED_DOWNLOAD) {
            player.kickPlayer("Failed to load ResourcePack, Please log in again.");
        }
    }

    // If Login GUI is open, start recording PIN into an array
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(loginTitle)) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {
                int slot = event.getRawSlot() + 1;
                Player player = (Player) event.getWhoClicked();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                        clickedSlots.add(slot);
                // Start combining all the numbers into a string
                if (clickedSlots.size() >= 4) {
                    StringBuilder pinBuilder = new StringBuilder();
                    for (int i = 0; i < 4; i++) {
                        pinBuilder.append(clickedSlots.get(i));
                    }
                    String pin = pinBuilder.toString();
                    clickedSlots.clear();
                    player.closeInventory(); // Close inventory once 4-digit PIN has been typed out.
                    // If the player is already registered, use /login command; if not, use /register command
                    if (AuthMeApi.getInstance().isRegistered(player.getName())) {
                        Bukkit.dispatchCommand(player, "login " + pin);
                    } else {
                        Bukkit.dispatchCommand(player, "register " + pin + " " + pin);
                    }
                    if (AuthMeApi.getInstance().isAuthenticated(player)) {
                        player.sendMessage("码 Registered Successfully! Your PIN is §x§8§3§d§1§3§0" + pin); // Send PIN Message
                    }
                }
            }
        }
    }
    // xAuth command to open the GUI or reload the config
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                player.sendMessage("Config reloaded.");
                return true;
            }
            openLoginGUI(player);
            return true;
        }
        return false;
    }
    public List<Integer> getClickedSlots() {
        return clickedSlots;
    }
}