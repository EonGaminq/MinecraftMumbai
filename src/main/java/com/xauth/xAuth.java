package com.xauth;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;


import java.util.ArrayList;
import java.util.List;

import fr.xephi.authme.api.v3.AuthMeApi;

import com.xauth.listeners.onGUIClose;
import com.xauth.listeners.onLogoutCommand;
import com.xauth.listeners.onUnregisterCommand;


public class xAuth extends JavaPlugin implements CommandExecutor, Listener {

    private List<Integer> clickedSlots;

    @Override
    public void onEnable() {
        getLogger().info("xAuth plugin has been enabled.");
        Bukkit.getPluginManager().registerEvents(this, this);
        clickedSlots = new ArrayList<>();
        Bukkit.getPluginManager().registerEvents(new onGUIClose(this), this);
        Bukkit.getPluginManager().registerEvents(new onLogoutCommand(this), this);
        Bukkit.getPluginManager().registerEvents(new onUnregisterCommand(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("xAuth plugin has been disabled.");
    }


    //PIN GUI inventory Creation
    public void openDispenserGUI(Player player) {
        Inventory dispenserGUI = Bukkit.createInventory(null, InventoryType.DISPENSER, "PIN GUI");
        ItemStack diamond = new ItemStack(Material.DIAMOND);
        for (int i = 0; i < dispenserGUI.getSize(); i++) {
            dispenserGUI.setItem(i, diamond);
        }
        player.openInventory(dispenserGUI);
    }

    //Check to see if the player is logged in, If not, open login GUI. The task has 3 ticks of delay to let authMe have priority.
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.runTaskLater(this, () -> {
        if (!AuthMeApi.getInstance().isAuthenticated(player)) {
            openDispenserGUI(player);
            }
        }, 10);
    }

    // If Login GUI is open, start recording PIN into an array
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("PIN GUI")) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.DIAMOND) {
                int slot = event.getRawSlot() + 1;
                Player player = (Player) event.getWhoClicked();
                clickedSlots.add(slot);
                // Start combining all the numbers into a string
                if (clickedSlots.size() >= 4) {
                    StringBuilder pinBuilder = new StringBuilder();
                    for (int i = 0; i < 4; i++) {
                        pinBuilder.append(clickedSlots.get(i));
                    }
                    String pin = pinBuilder.toString();
                    clickedSlots.clear();
                    player.closeInventory(); // closeInventory once 4 digit PIN has been typed out.
                    // If player is already registered, use /login command, if not, use /register command
                    if (AuthMeApi.getInstance().isRegistered(player.getName())) {
                        Bukkit.dispatchCommand(player, "login " + pin);
                    } else {
                        Bukkit.dispatchCommand(player, "register " + pin + " " + pin);
                        player.sendMessage("Please note your pin is " + pin); // Send PIN Message
                    }
                }
            }
        }
    }
    public List<Integer> getClickedSlots() {
        return clickedSlots;
    }
}
