package com.xauth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitScheduler;

import fr.xephi.authme.api.v3.AuthMeApi;
import com.xauth.xAuth;

public class onGUIClose implements Listener {

    private final xAuth plugin;
    private final String loginTitle;
    private final String registerTitle;

    public onGUIClose(xAuth plugin, String loginTitle, String registerTitle) {
        this.plugin = plugin;
        this.loginTitle = loginTitle;
        this.registerTitle = registerTitle;
    }

    // If the PIN GUI is closed and the player is not authenticated, reopen the GUI with a delay of 1 tick
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        String inventoryTitle = event.getView().getTitle();
        if (inventoryTitle.equals(loginTitle) || inventoryTitle.equals(registerTitle)) {
            Player player = (Player) event.getPlayer();
            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
                if (!plugin.getClickedSlots().isEmpty()) {
                    plugin.getClickedSlots().clear(); // Clear the recorded PIN digits using the plugin's clickedSlots list
                }
                if (!AuthMeApi.getInstance().isRegistered(player.getName())) {
                    plugin.openRegisterGUI(player);
                }
                else if (!AuthMeApi.getInstance().isAuthenticated(player)) {
                    plugin.openLoginGUI(player);
                }
                else {
                    return;
                }
            }, 6);
        }
    }
}
